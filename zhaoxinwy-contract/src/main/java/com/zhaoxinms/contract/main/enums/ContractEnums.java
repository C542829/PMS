package com.zhaoxinms.contract.main.enums;

public class ContractEnums {

	// 合同状态
	public enum ContractStatusEnums {
		draft("0", "拟定中"), canceled("2", "已作废"), audit("3", "审批中"), signing("4", "待签署"), signed("5", "已签署"), fulfil("6", "履行中"), suspend("7", "中止"), done("8", "已结束");

		private String index;
		private String name;

		ContractStatusEnums(String index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	// 合同履行状态
	public enum FulfilStatusEnums {
		none("0", ""), notEffective("1", "未生效"), effective("2", "已生效"), expired("3", "已截止"), change("4", "变更中");
		private String index;
		private String name;

		FulfilStatusEnums(String index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public static FulfilStatusEnums findEnumByindex(String index) {
            for (FulfilStatusEnums statusEnum : FulfilStatusEnums.values()) {
                if (statusEnum.getIndex().equals(index)) {
                    return statusEnum;
                }
            }
            throw new IllegalArgumentException("index is invalid");
        }
	}
	
	public enum inoutType {
		none("0","非收支"), income("1","收入类"), expense("2","支出类");
		private String index;
		private String name;
		inoutType(String index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	// 审批状态
	public enum ApproveStatusEnums {
		none("0", "未审批"), progress("1", "审批中"), pass("2", "审批通过"), reject("3", "审批不通过"), cancel("4", "撤销审批");

		private String index;
		private String name;

		ApproveStatusEnums(String index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	public enum FileTypeEnums {
		mainFile("0", "主文件"), other("1", "附件");

		private String index;
		private String name;

		FileTypeEnums(String index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
