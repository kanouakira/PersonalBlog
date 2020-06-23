import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/tokens',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/tokens',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/tokens/logout',
    method: 'post'
  })
}

export function getUsers(params) {
  return request({
    url: '/users',
    method: 'get',
    params
  })
}

export function updateUser(user_id, data) {
  return request({
    url: `/users/${user_id}`,
    method: 'put',
    data
  })
}

export function deleteUser(params) {
  return request({
    url: `/users/${params}`,
    method: 'delete'
  })
}
