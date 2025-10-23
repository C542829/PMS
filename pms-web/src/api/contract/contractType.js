import request from "@/utils/request";

// 合同类型树
export function getContractType() {
  return request({
    url: "/conf/treeSelect/contractTypeSelect",
    method: "get",
  });
}

// 合同类型树
export function getContractTypeEnable() {
    return request({
      url: "/conf/treeSelect/contractTypeSelect",
      method: "get",
      params: {status:0},
    });
  }
