package com.zhaoxinms.contract.conf.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhaoxinms.common.core.mybatisplus.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("contract_file")
public class ContractFile extends BaseEntity {

    /**
     * 对象存储主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原名
     */
    private String originalName;

    /**
     * 文件后缀名
     */
    private String fileSuffix;

    /**
     * URL地址
     */
    private String url;

    /**
     * 文件模块
     */
    private String module;

    //onlyoffice文档预览使用
    private String onlyofficeKey;
}
