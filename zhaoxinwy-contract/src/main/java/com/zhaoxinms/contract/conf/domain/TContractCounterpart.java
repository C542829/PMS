package com.zhaoxinms.contract.conf.domain;

import java.util.Date;

import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhaoxinms.common.core.domain.BaseEntity;
import com.zhaoxinms.contract.conf.util.compare.Compare;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合作方对象 t_contract_counterpart
 *
 * @author ruoyi
 * @date 2022-07-18
 */
@Data 
@EqualsAndHashCode(callSuper = true)
@TableName("contract_counterpart")
public class TContractCounterpart extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 合作方名称
     */
    @Compare("合作方名称")
    private String contractCounterpartName;

//    /**
//     * 合作方编号
//     */
//    @Compare("合作方编码")
//    private String contractCounterpartCode;
    /**
     * 统一社会信用代码
     */
    @Compare("统一社会信用代码")
    private String uscc;
    /**
     * 经营状态
     */
    @Compare(value="经营状态", dict="BUSINESS_STATUS")
    private String businessStatus;
    /**
     * 法人姓名
     */
    @Compare("法人姓名")
    private String legalPersonName;
    /**
     * 法人身份证
     */
    @Compare("法人身份证")
    private String legalPersonIdCard;

    /**
     * 合作方类型
     */
    @Compare(value="合作方类型", dict="PARTNER_TYPE")
    private String companyType;

    /**
     * 注册日期开始时间
     */
    @Compare(value="注册开始时间", dateFormat="yyyy-MM-dd")
    private Date businessStartDate;
    /**
     * 注册日期结束时间
     */
    @Compare(value="注册结束时间", dateFormat="yyyy-MM-dd")
    private Date businessEndDate;
    /**
     * 是否长期 = 0； = 1长期
     */
    @Compare(value="是否长期", dict="FOREVER")
    private Integer forever = 0;
    /**
     * 注册资本
     */
    @Compare("注册资本")
    private String registeredCapital;
    /**
     * 电话
     */
    @Compare("电话")
    private String tel;
    /**
     * 传真
     */
    @Compare("传真")
    private String fax;
    /**
     * 地区编号
     */
    private String areaCode;
    /**
     * 地区名
     */
    private String areaName;
    /**
     * 注册地址
     */
    @Compare("注册地址")
    private String address;
    /**
     * 联系人
     * [{name：”张三“,caller: "先生",depart: "财务部",position: "岗位",tel: "13698989898",email: "sss@qq.com"}]
     */
    @Size(min = 0,max = 4000,message = "联系人信息过多，请不要填写过多的联系人")
    @Compare(value="联系人", showDetail = false)
    private String contacts;

    /**
     * 资质信息
     * [{"uid":"1629772356679385090","name":"山西肇新科技-源码授权合同 - 模板.docx","from":"template","type":"0","url":"http://121.5.233.137:180/profile/upload/contract/62a249f0-f558-4aca-a12f-e015894c541e.docx"}]
     */
    @Size(min = 0,max = 4000,message = "资质信息过多，请不要填写过多的资质信息")
    @Compare(value="资质信息", showDetail = false)
    private String attachments;

    /**
     * 银行账号 数组[]，包含所属银行、开户行、账号
     * [{bankName:"中国银行",bankDeposit: "中国银行复兴路支行"cardNo: "122992929292"}]
     */
    @Size(min = 0,max = 4000,message = "银行信息过多，请不要填写过多的银行信息")
    @Compare(value ="银行账号", showDetail = false)
    private String bankJson;
    /**
     * 0 未删除 1 已删除
     */
    @TableLogic
    private String delFlag;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 租户id
     */
    private String tenantId;
    /**
      * 可用状态 0可用 1不可用
     */
    @Compare("是否禁用")
    private Integer status = 1;
    /**
     *  合作次数
     */
    private Integer collTimes = 0;
    /**
     * 最后一次信用查询时间
     */
    private Date lastCredit;
    /**
     * 信用状态
     */
    @Compare("信用状态")
    private String creditStatus;
    
    /**
     * 合作方负责人
     */
    //@Compare("合作方负责人")
    private String contractCounterpartHead;
    
    /**
     * 营业范围
     */
    @Compare("营业范围")
    private String businessScope;

}
