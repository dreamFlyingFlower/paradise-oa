import apiUser from '@api/user'
import {getTreeByRoleId} from '@api/menu'
import cookie from '../utils/cookie'

/**
 * 对store中的数据进行修改,可通过axios来动态修改,和mutation配合使用
 */
export default {

  /**
   * 登录
   * @param params 包括用户名和密码,密码使用aes加密
   */
  LOGIN({commit}, params) {
    return new Promise((resolve, reject) => {
      apiUser.login(params).then(resp => {
        let data = resp.data;
        commit('USER', resp.data);
        cookie.setUser(resp.data);
        cookie.setToken(data.token);
        resolve(resp.code);
      }).catch(error => {
        reject(error);
      });
    })
  },
  LOGOUT({commit, state}) {
    return new Promise((resolve, reject) => {
      apiUser.logout(state.user.userId).then(resp => {
        commit('USER', null);
        cookie.removeToken();
        cookie.removeUser();
        resetRouter();
        resolve();
      }).catch(error => {
        reject(error);
      });
    });
  },
  /**
   * 获得用户角色权限
   * @param params 参数
   */
  ROUTES({commit}, roleId) {
    return new Promise(resolve => {
      getTreeByRoleId(roleId).then(resp => {
        resolve(resp.data);
      });
    })
  },
  TOGGLE_SIDEBAR({commit}) {
    commit('TOGGLE_SIDEBAR')
  },
  CLOSE_SIDEBAR({commit}, {withoutAnimation}) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  TOGGLE_DEVICE({commit}, device) {
    commit('TOGGLE_DEVICE', device)
  },
  SET_SIZE({commit}, size) {
    commit('SET_SIZE', size)
  },
  ADD_VIEWS({dispatch}, view) {
    dispatch('VISITED_VIEW', view)
    dispatch('CACHED_VIEW', view)
  },
  VISITED_VIEW({commit}, view) {
    commit('VISITED_VIEW', view)
  },
  CACHED_VIEW({commit}, view) {
    commit('CACHED_VIEW', view)
  },
  DEL_VIEW({dispatch, state}, view) {
    return new Promise(resolve => {
      dispatch('DEL_VISITED_VIEW', view)
      dispatch('DEL_CACHED_VIEW', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  DEL_VISITED_VIEW({commit, state}, view) {
    return new Promise(resolve => {
      commit('DEL_VISITED_VIEW', view)
      resolve([...state.visitedViews])
    })
  },
  DEL_CACHED_VIEW({commit, state}, view) {
    return new Promise(resolve => {
      commit('DEL_CACHED_VIEW', view)
      resolve([...state.cachedViews])
    })
  },
  DEL_OTHERS_VIEWS({dispatch, state}, view) {
    return new Promise(resolve => {
      dispatch('DEL_OTHERS_VISITED_VIEWS', view)
      dispatch('DEL_OTHERS_CACHED_VIEWS', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  DEL_OTHERS_VISITED_VIEWS({commit, state}, view) {
    return new Promise(resolve => {
      commit('DEL_OTHERS_VISITED_VIEWS', view)
      resolve([...state.visitedViews])
    })
  },
  DEL_OTHERS_CACHED_VIEWS({commit, state}, view) {
    return new Promise(resolve => {
      commit('DEL_OTHERS_CACHED_VIEWS', view)
      resolve([...state.cachedViews])
    })
  },
  DEL_ALL_VIEWS({dispatch, state}, view) {
    return new Promise(resolve => {
      dispatch('DEL_ALL_VISITED_VIEWS', view)
      dispatch('DEL_ALL_CACHED_VIEWS', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  DEL_ALL_VISITED_VIEWS({commit, state}) {
    return new Promise(resolve => {
      commit('DEL_ALL_VISITED_VIEWS')
      resolve([...state.visitedViews])
    })
  },
  DEL_ALL_CACHED_VIEWS({commit, state}) {
    return new Promise(resolve => {
      commit('DEL_ALL_CACHED_VIEWS')
      resolve([...state.cachedViews])
    })
  },
  UPDATE_VISITED_VIEW({commit}, view) {
    commit('UPDATE_VISITED_VIEW', view)
  }
};
