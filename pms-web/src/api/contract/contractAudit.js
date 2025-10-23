import request from "@/utils/request";

// 查询合同草拟列表
export function listContractAudit(query) {
  return request({
    url: "/contract/contractAudit/list",
    method: "get",
    params: query,
  });
}

// 查询合同审批单详细
export function getcontractAudit(id) {
  return request({
    url: "/contract/contractAudit/" + id,
    method: "get",
  });
}

// 查询合同合成数据
export function getContractMergeData(id) {
  return request({
    url: "/contract/contractAudit/contractMergeData/" + id,
    method: "get",
  });
}

// 查询项目
export function searchProject(data) {
  return request({
    url: "/contract/contractAudit/searchProject",
    method: "post",
    data: data,
  });
}

// 查询合作方
export function searchPartner(data) {
  return request({
    url: "/contract/contractAudit/searchPartner",
    method: "post",
    data: data,
  });
}

// 查询签约主体
export function searchSubject(data) {
  return request({
    url: "/contract/contractAudit/searchSubject",
    method: "post",
    data: data,
  });
}

// 通过id查询合作方和签约主体
export function searchMerchantById(data) {
  return request({
    url: "/contract/contractAudit/searchMerchantById",
    method: "post",
    data: data,
  });
}

// 新增合同审批单
export function addContractAudit(data) {
  return request({
    url: "/contract/contractAudit",
    method: "post",
    data: data,
  });
}

// 发起审批
export function beginAudit(data) {
  return request({
    url: "/contract/contractAudit/beginAudit",
    method: "post",
    data: data,
  });
}

// 审批通过
export function auditSuccess(data) {
  return request({
    url: "/contract/contractAudit/auditSuccess",
    method: "post",
    data: data,
  });
}

// 审批失败
export function auditFail(data) {
  return request({
    url: "/contract/contractAudit/auditFail",
    method: "post",
    data: data,
  });
}

// backToDraft
export function backToDraft(data) {
  return request({
    url: "/contract/contractAudit/backToDraft",
    method: "post",
    data: data,
  });
}

// 查询用章明细
export function getSeal(id) {
  return request({
    url: "/contract/contractAudit/getSeal/" + id,
    method: "get",
  });
}

// 查询备注明细
export function getRemarks(id) {
  return request({
    url: "/contract/contractAudit/getRemarks/" + id,
    method: "get",
  });
}

// 提交用印申请
export function applyStamp(data) {
  return request({
    url: "/contract/contractAudit/applyStamp",
    method: "post",
    data: data,
  });
}

// 提交备注
export function addRemark(data) {
  return request({
    url: "/contract/remarks",
    method: "post",
    data: data,
  });
}

// 提交作废备注
export function addCancelRemark(data) {
  return request({
    url: "/contract/contractAudit/cancelContract",
    method: "post",
    data: data,
  });
}

// 删除备注
export function deleteRemark(id) {
  return request({
    url: "/contract/remarks/" + id,
    method: "delete",
  });
}

// 查询部门下人拉树结构
export function deptTreeSelect() {
  return request({
    url: "/conf/treeSelect/userSelect",
    method: "get",
  });
}

// 上传双章合同
export function uploadSealContract(data) {
  return request({
    url: "/contract/contractAudit/uploadSealContract",
    method: "post",
    data: data,
  });
}

// 变更双章合同
export function changeSealContract(data) {
  return request({
    url: "/contract/contractAudit/changeSealContract",
    method: "post",
    data: data,
  });
}

// 创建审批中的修订版
export function createNewVersion(auditId, versionName, taskId) {
  return request({
    url: "/contract/contractAudit/createVersion/" + auditId,
    method: "post",
    data: { fromVersion: versionName, taskId: taskId },
  });
}

export function chargeIsNewVersion(query) {
  return request({
    url: "/contract/contractAudit/chargeIsNewVersion",
    method: "get",
    params: query,
  });
}

export function notify(id) {
  return request({
    url: "/contract/contractAudit/notifyContract/" + id,
    method: "post",
  });
}
