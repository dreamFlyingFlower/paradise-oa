import service from '../utils/service';

export default {
  login,
  logout
}

/**
 * 注意,该方法是因为springsecutiry的登录不支持application/json的请求头
 * @param params 登录的参数
 * @returns {AxiosPromise}
 */
function login(params) {
  return service.service({
    url: "/user/login",
    method: "post",
    params: params
  });
}

/**
 * 用户正常登出
 * @param userId 用户编号
 * @returns {Promise<unknown>}
 */
function logout(userId) {
  return service.get(`user/logout/${userId}`)
}
