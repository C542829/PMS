package com.zhaoxinms.contract.conf.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.zhaoxinms.contract.conf.domain.ContractFile;
import com.zhaoxinms.contract.conf.domain.bo.ContractFileBo;
import com.zhaoxinms.contract.conf.domain.vo.ContractFileVo;
import com.zhaoxinms.contract.conf.util.PageQuery;
import com.zhaoxinms.contract.conf.util.TableDataInfo;

/**
 * 文件上传 服务层
 *
 */
public interface IContractFileService {
    
    /**
     * 分页查询
     * @param sysOss
     * @param pageQuery
     * @return
     */
    TableDataInfo<ContractFileVo> queryPageList(ContractFileBo sysOss, PageQuery pageQuery);

    /**
     * 通过ids查询文件
     * @param fileIds
     * @return
     */
    List<ContractFileVo> listByIds(Collection<Long> fileIds);

    /**
     * 查询明细
     * @param fileId
     * @return
     */
    ContractFile getById(Long fileId);

    /**
     * 文件上传
     * @param file
     * @param module
     * @return
     * @throws IOException
     */
    ContractFile upload(MultipartFile file, String module) throws IOException;

    /**
     * 拷贝本地文件到上传目录
     * 
     * @throws IOException
     */
    ContractFile copyFile(File file, String fileName, String module) throws IOException;

    /**
     * 拷贝合同文件，用于存量合同批量使用
     * @param file
     * @param fileName
     * @param module
     * @return
     * @throws IOException
     */
    ContractFile copyFileInStock(File file, String fileName, String module) throws IOException;
    
    /**
     * 删除文件
     * @param ids
     * @param isValid
     * @return
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
    /**
     * 变更onlyoffice编辑key
     * @param fileId
     * @return
     */
    ContractFile changeKey(Long fileId);
    /**
     * 获取文件路径
     * @param fileId
     * @return
     */
    String getFilePath(Long fileId);
    /**
     * 在全部租户中查找文件
     * @param fileId
     * @return
     */
    String getFilePathInAllTenant(Long fileId);
}
