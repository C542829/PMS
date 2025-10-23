import request from "@/utils/request";

// 合同任务
export function myContractList(query) {
  return request({
    url: "/contract/contractDraft/myContractList",
    method: "get",
    params: query,
  });
}

// 查询关联合同列表
export function relatedContractList(query) {
  return request({
    url: "/contract/contractDraft/relatedContractList",
    method: "get",
    params: query,
  });
}

// 查询合同草拟列表
export function listContractDraft(query) {
  return request({
    url: "/contract/contractDraft/list",
    method: "get",
    params: query,
  });
}

// 查询合同草拟详细
export function getContractDraft(id) {
  return request({
    url: "/contract/contractDraft/" + id,
    method: "get",
  });
}

// 通过合同编号查询合同信息
export function getContractByCode(contractCode) {
  return request({
    url: "/contract/contractDraft/getContractByCode",
    method: "get",
    params: { contractCode: contractCode },
  })
}

// 新增合同草拟
export function addContractDraft(data) {
  return request({
    url: "/contract/contractDraft",
    method: "post",
    data: data,
  });
}

// 新增合同草拟
export function addContractDraftBatch(data) {
  return request({
    url: "/contract/contractDraft/batchCreate",
    method: "post",
    data: data,
  });
}

// 修改合同草拟
export function updateContractDraft(data) {
  return request({
    url: "/contract/contractDraft",
    method: "put",
    data: data,
  });
}

// 删除合同草拟
export function delContractDraft(id) {
  return request({
    url: "/contract/contractDraft/" + id,
    method: "delete",
  });
}

// 增加新版本
export function addNewVersion(data) {
  return request({
    url: "/contract/contractDraft/newVersion",
    method: "post",
    data: data,
  });
}

//查看合同文件历史版本
export function contractFilesList(contractId) {
  return request({
    url: "/contract/contractDetail/contractFilesList",
    method: "get",
    params: { contractId: contractId },
  });
}
// 查询部门下人拉树结构
export function deptTreeSelect() {
  return request({
    url: "/conf/treeSelect/userSelect",
    method: "get",
  });
}
