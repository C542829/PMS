import request from "@/utils/request";

// 查询合作方列表
export function listContractCounterpart(query) {
  return request({
    url: "/conf/contractCounterpart/list",
    method: "get",
    params: query,
  });
}

// 查询合作方详细
export function getContractCounterpart(id) {
  return request({
    url: "/conf/contractCounterpart/" + id,
    method: "get",
  });
}

// 新增合作方
export function addContractCounterpart(data) {
  return request({
    url: "/conf/contractCounterpart",
    method: "post",
    data: data,
  });
}

// 修改合作方
export function updateContractCounterpart(data) {
  return request({
    url: "/conf/contractCounterpart",
    method: "put",
    data: data,
  });
}

// 删除合作方
export function delContractCounterpart(id) {
  return request({
    url: "/conf/contractCounterpart/" + id,
    method: "delete",
  });
}

// 可用状态修改
export function changeStatus(id, status) {
  const data = {
    id,
    status,
  };
  return request({
    url: "/conf/contractCounterpart/changeStatus",
    method: "put",
    data: data,
  });
}

