import service from '@utils/service';

export {
  getTreeByRoleId,
  getPermissionByRoleId,
  getSelfChildren

}

/**
 * 根据角色查询菜单树,不查相应权限
 * @param roleId 角色编号
 */
function getTreeByRoleId(roleId) {
  return service.get(`menu/getTreeByRoleId/${roleId}`);
}

/**
 * 根据角色查询菜单树,以及相应权限
 * @param roleId 角色编号
 */
function getPermissionByRoleId(roleId) {
  return service.get(`menu/getPermissionByRoleId/${roleId}`);
}

function getSelfChildren(menuId) {
  return service.get(`menu/getSelfChildren/${menuId}`);
}
