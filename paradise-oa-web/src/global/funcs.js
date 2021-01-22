/**
 * Vue环境中全局通用的方法
 */
import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token';

export default {
  install: function(Vue) {
    /**
     * 存储临时session,不可跨域,浏览器关闭则session失效
     * @param key 存储的key
     * @param val 需要存储的值
     */
    Vue.prototype.$setSession = (key, val) => {
      if (!typeof key === "string") {
        console.log("key only string type");
        return;
      }
      if (typeof val === "string") {
        sessionStorage.setItem(key, val);
      } else {
        sessionStorage.setItem(key, JSON.stringify(val));
      }
    };
    /**
     * 获得session中的值,先json.parse下,若是解析成功,表明是对象,直接返回;若是解析失败,则直接返回
     */
    Vue.prototype.$getSession = (key) => {
      try {
        return JSON.parse(sessionStorage.getItem(key));
      } catch (error) {
        return sessionStorage.getItem(key);
      }
    };
    /**
     * 存储永久session,不可跨域,浏览器关闭session不失效
     * @param key 存储的key
     * @param val 需要存储的值
     */
    Vue.prototype.$setLocalSession = (key, val) => {
      if (!typeof key === "string") {
        console.log("key only string type");
        return;
      }
      if (typeof val === "string") {
        localStorage.setItem(key, val);
      } else {
        localStorage.setItem(key, JSON.stringify(val));
      }
    };
    /**
     * 获得session中的值,先json.parse下,若是解析成功,表明是对象,直接返回;若是解析失败,则直接返回
     */
    Vue.prototype.$getLocalSession = (key) => {
      try {
        return JSON.parse(localStorage.getItem(key));
      } catch (error) {
        return localStorage.getItem(key);
      }
    };
    /**
     * 判断对象,数组或字符串是否为空,'   '字符串都是空格也是空,返回true
     * @param param 参数
     * @returns {boolean}
     */
    Vue.prototype.$isBlank = function(param) {
      if (new Vue().$isString(param)) {
        return param.trim().length === 0 ? true : false;
      }
      if (param && Object.keys(param) && Object.keys(param).length > 0) {
        return false;
      }
      return true;
    };
    Vue.prototype.$nonBlank = function(param) {
      return !new Vue().$isBlank(param);
    };
    /**
     * 对参数进行逻辑判断.==和===不一样,===必须类型和值完全一样,内存地址要一样
     * 在js中0,'',undefined的逻辑判断为false.
     * es6(可能es5)之前的版本中,0==undefined,''==undefined为true,但是es6之后为false
     * 0==''仍判断为true,0===''为false,es6之前'0'也判断为false,es6后判断为true
     * '0','   ','undefined'的逻辑判断中为true,都是字符串,即便字符画中是空格
     * 本方法中将剔除逻辑判断0为false的情况,''逻辑判断仍为false
     * 注意:[],{}仍然是存在的,只是为null,判断结果仍为true
     * @param param 参数
     */
    Vue.prototype.$exist = function(param) {
      // 必须用===,因为''==0也为true
      if (param === 0 || param === '0') {
        return true;
      } else {
        return param ? true : false;
      }
    };
    Vue.prototype.$isString = function(param) {
      return typeof param === 'string' || param instanceof String ? true : false;
    };
    /**
     * 判断是否是数组
     * @param {Array} param 参数
     * @return true->是,false->不是
     */
    Vue.prototype.$isArray = function(param) {
      return typeof Array.isArray === 'undefined' ?
        Object.prototype.toString.call(param) === "[object Array]" :
        Array.isArray(param);
    };
    /**
     * 判断是数组,同时判断是否为空
     * @param {Object} param
     * @return true->空,false->非空
     */
    Vue.prototype.$isArrayBlank = function(param) {
      if(new Vue().$isArray(param)){
        return param.length > 0;
      }
      return false;
    };
    /**
     * 判断是数组,同时判断是否非空
     * @param {Object} param
     * @return true->非空,false->空
     */
    Vue.prototype.$isArrayNotBlank = function(param) {
      return !new Vue().$isArrayBlank(param);
    };
    Vue.prototype.$isExternal = function(path) {
      return /^(https?:|mailto:|tel:)/.test(path)
    };
    Vue.prototype.$getToken = function() {
      return Cookies.get(TokenKey);
    };
    Vue.prototype.$setToken = function(token) {
      return Cookies.set(TokenKey, token);
    };
    Vue.prototype.$removeToken = function() {
      return Cookies.remove(TokenKey);
    };
    Vue.prototype.$isFunc = (param) => {
      return typeof params === 'function' ||
        Object.prototype.toString.call(param) === '[object Function]';
    };
  }
};
