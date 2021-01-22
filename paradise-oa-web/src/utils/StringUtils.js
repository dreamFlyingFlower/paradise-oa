export default class StringUtils {
  constructor() {

  }

  /**
   * 判断是否为字符串
   * @param {Object} param 参数
   * @return true->是,false->不是
   */
  static isString(param) {
    return typeof param === 'string' || param instanceof String ? true : false;
  }

  /**
   * 判断字符串是否为空:"","   ",null都会被判空
   * @param {Object} param 参数
   * @return true->空,false->非空
   */
  static isBlank(param) {
    return this.isString(param) && param.trim().length === 0;
  }

  /**
   * 判断字符串是否非空:"","   ",null都会被判空
   * @param {Object} param 参数
   * @return true->非空,false->空
   */
  static isNotBlank(param) {
    return !this.isBlank(param);
  }
}
