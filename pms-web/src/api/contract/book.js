import request from '@/utils/request'

// 查询
export function listBook(query) {
  return request({
    url: '/contract/contractBook/list',
    method: 'get',
    params: query
  })
}
