import request from '@/utils/request'

export function getPosts(params) {
  return request({
    url: '/posts',
    method: 'get',
    params
  })
}

export function createPost(data) {
  return request({
    url: '/posts',
    method: 'post',
    data
  })
}

export function updatePost(post_id, data) {
  return request({
    url: `/posts/${post_id}`,
    method: 'put',
    data
  })
}

export function addTagToPost(post_id, data) {
  return request({
    url: `/posts/${post_id}/tags`,
    method: 'post',
    data
  })
}

export function updateTagToPost(post_id, data) {
  return request({
    url: `/posts/${post_id}/tags`,
    method: 'put',
    data
  })
}

export function getPostTags(post_id) {
  return request({
    url: `/posts/${post_id}/tags`,
    method: 'get'
  })
}

export function deletePost(params) {
  return request({
    url: `/posts/${params}`,
    method: 'delete'
  })
}
