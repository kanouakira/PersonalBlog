import request from '@/utils/request'

export function getComments(params) {
  return request({
    url: '/comments',
    method: 'get',
    params
  })
}

export function updateComment(comment_id, data) {
  return request({
    url: `/comments/${comment_id}`,
    method: 'put',
    data
  })
}

export function deleteComment(params) {
  return request({
    url: `/comments/${params}`,
    method: 'delete'
  })
}
