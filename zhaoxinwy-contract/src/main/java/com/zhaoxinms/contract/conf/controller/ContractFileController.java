package com.zhaoxinms.contract.conf.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhaoxinms.common.annotation.Log;
import com.zhaoxinms.common.config.RuoYiConfig;
import com.zhaoxinms.common.constant.Constants;
import com.zhaoxinms.common.core.controller.BaseController;
import com.zhaoxinms.common.enums.BusinessType;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.common.utils.file.FileUtils;
import com.zhaoxinms.contract.conf.domain.ContractFile;
import com.zhaoxinms.contract.conf.domain.vo.ContractFileVo;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.service.IContractFileService;
import com.zhaoxinms.contract.conf.util.ZipDownloadUtil;
import com.zhaoxinms.contract.type.util.R;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

/**
 * 文件上传 控制层
 *
 */
@Validated
@Api(value = "文件管理", tags = {"文件管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/conf/file")
public class ContractFileController extends BaseController {

    private final IContractFileService iContractFileService;
    private static final String[] OFFICE_EXTENSIONS = {".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx"};

    // 判断文件是否为Office文件的方法
    public static boolean isOfficeFile(String extension) {
        // 检查扩展名是否为Office文件类型
        for (String officeExt : OFFICE_EXTENSIONS) {
            if (officeExt.equals(extension)) {
                return true; // 如果匹配，返回true
            }
        }
        return false; // 否则返回false
    }

    @ApiOperation("文件上传")
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "文件", paramType = "query", dataTypeClass = File.class,
        required = true)})
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload/{module}")
    public R<Map<String, Object>> upload(@RequestPart("file") MultipartFile file,
        @PathVariable("module") String module) {
        if (ObjectUtil.isNull(file)) {
            throw new ServiceException("上传文件不能为空");
        }

        try {
            ContractFile ContractFile = iContractFileService.upload(file, module);
            Map<String, Object> map = new HashMap<>(2);
            map.put("url", ContractFile.getUrl());
            map.put("fileName", ContractFile.getOriginalName());
            map.put("fileId", ContractFile.getId());

            return R.ok(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.fail();
    }

    @ApiOperation("文件下载")
    @Log(title = "文件下载", businessType = BusinessType.OTHER)
    @RequestMapping(value="/download/{fileId}" ,method = {RequestMethod.POST, RequestMethod.GET})
    public void download(@ApiParam("文件id") @PathVariable Long fileId, HttpServletResponse response) throws IOException {
        ContractFile ContractFile = iContractFileService.getById(fileId);
        if (ObjectUtil.isNull(ContractFile)) {
            throw new ServiceException("文件数据不存在!");
        }

        // 如果是office文档，并且是合同模块，提示错误。这种类型的文档请走zipDownload方法
        if (StringUtils.isNotEmpty(ContractFile.getModule()) && ContractFile.getModule().equals("contract")) {
            if (isOfficeFile(ContractFile.getFileSuffix())) {
                throw new ServiceException("没有权限访问该文件");
            }
        }

        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(ContractFile.getUrl(), Constants.RESOURCE_PREFIX);
        // 下载名称
        String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, downloadName);
        FileUtils.writeBytes(downloadPath, response.getOutputStream());
    }

   

    @ApiOperation("合同文件打包下载")
    @Log(title = "合同文件打包下载", businessType = BusinessType.OTHER)
    @PostMapping("/downloadZip")
    public void downloadZip(@RequestBody List<Long> files, 
        HttpServletResponse response) throws IOException {

        List<ContractFileVo> downloadFiles = this.iContractFileService.listByIds(files);
        String zipFile = RuoYiConfig.getTempPath();
        String path = ZipDownloadUtil.batchDownload(zipFile, downloadFiles);

        // 下载名称
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        FileUtils.setAttachmentResponseHeader(response, "合同文件.zip");
        FileUtils.writeBytes(path, response.getOutputStream());
    }
  
}
