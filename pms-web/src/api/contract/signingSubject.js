import request from '@/utils/request'

// 查询签约主体列表
export function listSigningSubject(query) {
  return request({
    url: '/conf/signingSubject/list',
    method: 'get',
    params: query
  })
}

// 查询签约主体详细
export function getSigningSubject(id) {
  return request({
    url: '/conf/signingSubject/' + id,
    method: 'get'
  })
}

// 新增签约主体
export function addSigningSubject(data) {
  return request({
    url: '/conf/signingSubject',
    method: 'post',
    data: data
  })
}

// 修改签约主体
export function updateSigningSubject(data) {
  return request({
    url: '/conf/signingSubject',
    method: 'put',
    data: data
  })
}

// 删除签约主体
export function delSigningSubject(id) {
  return request({
    url: '/conf/signingSubject/' + id,
    method: 'delete'
  })
}

// 可用状态修改
export function changeStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/conf/signingSubject/changeStatus',
    method: 'put',
    data: data
  })
}
