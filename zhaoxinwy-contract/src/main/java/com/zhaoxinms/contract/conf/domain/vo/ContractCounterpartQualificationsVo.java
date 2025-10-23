package com.zhaoxinms.contract.conf.domain.vo;
import java.util.Date;

import lombok.Data;

@Data
public class ContractCounterpartQualificationsVo{
	
    private String name;
    private String remarks;
    private String realName;
    private Date startDate;
    private Date endDate;
    private String forever;
    private String licenseNumber;
    
	
}