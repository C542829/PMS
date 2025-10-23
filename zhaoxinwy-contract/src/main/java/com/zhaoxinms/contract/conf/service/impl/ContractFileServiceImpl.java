package com.zhaoxinms.contract.conf.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhaoxinms.common.config.RuoYiConfig;
import com.zhaoxinms.common.constant.Constants;
import com.zhaoxinms.common.utils.DateUtils;
import com.zhaoxinms.common.utils.StringUtils;
import com.zhaoxinms.common.utils.file.FileUploadUtils;
import com.zhaoxinms.common.utils.uuid.IdUtils;
import com.zhaoxinms.contract.conf.domain.ContractFile;
import com.zhaoxinms.contract.conf.domain.bo.ContractFileBo;
import com.zhaoxinms.contract.conf.domain.vo.ContractFileVo;
import com.zhaoxinms.contract.conf.exception.ServiceException;
import com.zhaoxinms.contract.conf.mapper.ContractFileMapper;
import com.zhaoxinms.contract.conf.service.IContractFileService;
import com.zhaoxinms.contract.conf.util.JsonUtil;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;
import com.zhaoxinms.framework.config.ServerConfig;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;

/**
 * 文件上传 服务层实现
 *
 */
@RequiredArgsConstructor
@Service
public class ContractFileServiceImpl implements IContractFileService {

    private final ContractFileMapper baseMapper;
    @Autowired
    private ServerConfig serverConfig;

    @Override
    public TableDataInfo<ContractFileVo> queryPageList(ContractFileBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ContractFile> lqw = buildQueryWrapper(bo);
        Page<ContractFileVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<ContractFileVo> listByIds(Collection<Long> fileIds) {
        LambdaQueryWrapper<ContractFile> lqw = Wrappers.lambdaQuery();
        lqw.in(ContractFile::getId, fileIds);
        List<ContractFile> list = baseMapper.selectList(lqw);
        List<ContractFileVo> result = JsonUtil.getJsonToList(list, ContractFileVo.class);
        return result;
    }

    private LambdaQueryWrapper<ContractFile> buildQueryWrapper(ContractFileBo bo) {
        LambdaQueryWrapper<ContractFile> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), ContractFile::getFileName, bo.getFileName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), ContractFile::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), ContractFile::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), ContractFile::getUrl, bo.getUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getCreateBy()), ContractFile::getCreateBy, bo.getCreateBy());
        return lqw;
    }

    @Override
    public ContractFile getById(Long fileId) {
        return baseMapper.selectById(fileId);
    }

    @Override
    public ContractFile upload(MultipartFile file, String module) throws IOException {
        String originalfileName = file.getOriginalFilename();
        String suffix =
            StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath() + File.separator + module;
        // 上传并返回新文件名称
        String fileName = FileUploadUtils.upload(filePath, file);
        String url = serverConfig.getUrl() + fileName;
        ContractFile ContractFile = new ContractFile();
        ContractFile.setUrl(url);
        ContractFile.setFileSuffix(suffix);
        ContractFile.setOriginalName(file.getOriginalFilename());
        ContractFile.setFileName(fileName);
        ContractFile.setModule(module);
        this.baseMapper.insert(ContractFile);
        return ContractFile;
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<ContractFile> list = baseMapper.selectBatchIds(ids);
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public ContractFile copyFile(File file, String fileName, String module) throws IOException {
        String suffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath() + File.separator + module;
        String uuid = DateUtils.datePath() + "/" + IdUtils.fastUUID();
        File dest = FileUploadUtils.getAbsoluteFile(filePath, uuid + suffix);

        String saveFile = FileUploadUtils.getPathFileName(filePath, uuid + suffix);
        FileUtil.copy(file.getAbsolutePath(), dest.getAbsolutePath(), true);

        String url = serverConfig.getUrl() + saveFile;
        ContractFile ContractFile = new ContractFile();
        ContractFile.setUrl(url);
        ContractFile.setFileSuffix(suffix);
        ContractFile.setOriginalName(fileName);
        ContractFile.setFileName(saveFile);

        ContractFile.setModule(module);
        this.baseMapper.insert(ContractFile);
        return ContractFile;
    }

    @Override
    public ContractFile changeKey(Long fileId) {
        String key = IdUtil.getSnowflake().nextIdStr();
        ContractFile ContractFile = this.baseMapper.selectById(fileId);
        ContractFile.setOnlyofficeKey(key);
        this.baseMapper.updateById(ContractFile);
        return ContractFile;
    }

    @Override
    public String getFilePath(Long fileId) {
        ContractFile ContractFile = this.getById(fileId);
        if (ObjectUtil.isNull(ContractFile)) {
            throw new ServiceException("文件数据不存在!");
        }
        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(ContractFile.getUrl(), Constants.RESOURCE_PREFIX);
        return downloadPath;
    }

    @Override
    public String getFilePathInAllTenant(Long fileId) {
        ContractFile ContractFile = this.baseMapper.selectByIdInAllTenant(fileId.toString());
        if (ObjectUtil.isNull(ContractFile)) {
            throw new ServiceException("文件数据不存在!");
        }
        // 本地资源路径
        String localPath = RuoYiConfig.getProfile();
        // 数据库资源地址
        String downloadPath = localPath + StringUtils.substringAfter(ContractFile.getUrl(), Constants.RESOURCE_PREFIX);
        return downloadPath;
    }

    @Override
    public ContractFile copyFileInStock(File file, String fileName, String module) throws IOException {
        String suffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());
        // 上传文件路径
        String filePath = RuoYiConfig.getUploadPath() + File.separator + module;
        String uuid = DateUtils.datePath() + "/" + IdUtils.fastUUID();
        File dest = FileUploadUtils.getAbsoluteFile(filePath, uuid + suffix);

        String saveFile = FileUploadUtils.getPathFileName(filePath, uuid + suffix);
        FileUtil.copy(file.getAbsolutePath(), dest.getAbsolutePath(), true);

        String url = serverConfig.getUrl() + saveFile;
        ContractFile ContractFile = new ContractFile();
        ContractFile.setUrl(url);
        ContractFile.setFileSuffix(suffix);
        ContractFile.setOriginalName(fileName);
        ContractFile.setFileName(saveFile);
        String ContractFileId = IdUtil.getSnowflake().nextIdStr();
        ContractFile.setModule(module);
        ContractFile.setId(Long.valueOf(ContractFileId));
        return ContractFile;
    }
}
