import Cookies from 'js-cookie'

export default {
  get,
  set,
  remove,
  getUser,
  setUser,
  removeUser,
  getToken,
  setToken,
  removeToken,
  getMenus,
  setMenus,
  removeMenus
}
const prefix = 'paradise_';// 通用cookie前缀
const userKey = 'USER';// 通用user存储cookie的key
const menusKey = 'MENUS';// 通用menus存储cookie的key
const tokenKey = 'Admin-Token';// token存储cookie的key

function createKey(key) {
  return prefix + key;
}

function get(key) {
  let value = Cookies.get(createKey(key));
  return value && value !== 'undefined' ? value : '';
}

function set(key, param) {
  return Cookies.set(createKey(key), param);
}

function remove(key) {
  return Cookies.remove(createKey(key));
}

function getUser() {
  let user = Cookies.get(createKey(userKey));
  return user && user !== "undefined" ? JSON.parse(Cookies.get(createKey(userKey))) : {};
}

function setUser(param) {
  return Cookies.set(createKey(userKey), JSON.stringify(param));
}

function removeUser() {
  return Cookies.remove(createKey(userKey));
}

function getToken() {
  let token = Cookies.get(createKey(tokenKey));
  return token && token !== 'undefined' ? token : '';
}

function setToken(token) {
  return Cookies.set(createKey(tokenKey), token);
}

function removeToken() {
  return Cookies.remove(createKey(tokenKey));
}

function getMenus() {
  let menus = Cookies.get(createKey(menusKey));
  return menus && menus !== "undefined" ?
      JSON.parse(Cookies.get(createKey(menusKey))) : {};
}

function setMenus(param) {
  return Cookies.set(createKey(menusKey), JSON.stringify(param));
}

function removeMenus() {
  return Cookies.remove(createKey(menusKey));
}