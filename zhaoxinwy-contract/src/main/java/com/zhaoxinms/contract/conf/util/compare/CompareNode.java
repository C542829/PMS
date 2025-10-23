package com.zhaoxinms.contract.conf.util.compare;

/**
 * @author zyqok
 * @since 2022/05/05
 */
public class CompareNode {
 
    /**
     * 字段
     */
    private String fieldKey;
 
    /**
     * 字段值
     */
    private Object fieldValue;
    
    private boolean showDetail;
    
    private String dict;
    
    private String dateFormat;
 
    /**
     * 字段名称
     */
    private String fieldName;
 
    public String getFieldKey() {
        return fieldKey;
    }
 
    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }
 
    public Object getFieldValue() {
        return fieldValue;
    }
 
    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
 
    public String getFieldName() {
        return fieldName;
    }
 
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

	public boolean isShowDetail() {
		return showDetail;
	}

	public void setShowDetail(boolean showDetail) {
		this.showDetail = showDetail;
	}

	public String getDict() {
		return dict;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
