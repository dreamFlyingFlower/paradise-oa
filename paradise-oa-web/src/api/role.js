import service from '@utils/service'

export default {
  getRoutes,
  getRoleMenu,
  getRoleMenus,
  savePermissions,
  saveMenus
}

/**
 * 获得角色的菜单路由
 * @param roleId 角色编号
 */
function getRoutes(roleId) {
  return service.get(`role/getRoutes/${roleId}`)
}

/**
 * 保存角色菜单以及按钮权限
 * @param roleId 角色编号
 * @param params 权限{menus:List<Integer>,halfMenus:List<Integer>,buttons:List<Integer>}
 * @returns {*}
 */
function savePermissions(roleId,params){
  return service.post(`role/savePermissions/${roleId}`,params);
}

/**
 * 保存角色的菜单权限,包括按钮
 * @param params 菜单以及按钮对象
 */
function saveMenus(roleId, params) {
  return service.post(`role/saveMenus/${roleId}`, params);
}