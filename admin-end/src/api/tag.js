import request from '@/utils/request'

export function getTags(params) {
  return request({
    url: '/tags',
    method: 'get',
    params
  })
}

export function createTag(data) {
  return request({
    url: '/tags',
    method: 'post',
    data
  })
}

export function updateTag(tag_id, params) {
  return request({
    url: `/tags/${tag_id}`,
    method: 'put',
    params
  })
}

export function deleteTag(params) {
  return request({
    url: `/tags/${params}`,
    method: 'delete'
  })
}
