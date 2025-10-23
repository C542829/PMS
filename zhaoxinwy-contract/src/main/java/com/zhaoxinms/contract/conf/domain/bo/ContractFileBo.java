package com.zhaoxinms.contract.conf.domain.bo;

import com.zhaoxinms.common.core.mybatisplus.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("文件存储分页查询对象")
public class ContractFileBo extends BaseEntity {

    /**
     * 文件名
     */
    @ApiModelProperty("文件名")
    private String fileName;
    /**
     * 原名
     */
    @ApiModelProperty("原名")
    private String originalName;
    /**
     * 文件后缀名
     */
    @ApiModelProperty("文件后缀名")
    private String fileSuffix;
    /**
     * URL地址
     */
    @ApiModelProperty("URL地址")
    private String url;
    /**
     * 服务商
     */
    @ApiModelProperty("服务商")
    private String service;

    /**
     * 服务商
     */
    @ApiModelProperty("模块")
    private String module;

    private String onlyofficeKey;
}
