package com.zhaoxinms.contract.main.model;
import lombok.Data;

@Data
public class ContractDraftFilesModel implements Comparable<ContractDraftFilesModel>{
	public static final String MAIN_FILE = "0"; //正文
	public static final String ATTACHMENT_FILE = "1"; //附件
	
	public static final String FROM_UPLOAD = "upload"; //正文
	public static final String FROM_TEMPLATE = "template"; //附件
	
    private String uid;
    private String name;
    private String from;
    private String type;
    private String url;
    
	@Override
	public int compareTo(ContractDraftFilesModel o) {
		//正文优先级最高
		if (!o.getType().equals(this.getType())) {
			if(o.getType().equals(MAIN_FILE)) {
				return 1;
			}else {
				return -1;
			}
		} 
		
		//模板排序靠前
		if(!o.getFrom().equals(this.getFrom())) {
			if(o.getFrom().equals(FROM_TEMPLATE)) {
				return 1;
			}else {
				return -1;
			}
		}
		
		return 0;
	}
}