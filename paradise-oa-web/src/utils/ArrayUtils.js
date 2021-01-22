export default class ArrayUtils {
  constructor() {

  }

  /**
   * 判断是否是数组
   * @param {Array} param 参数
   * @return true->是,false->不是
   */
  static isArray(param) {
    return typeof Array.isArray === 'undefined' ?
      Object.prototype.toString.call(param) === "[object Array]" :
      Array.isArray(param);
  }

  /**
   * 判断是数组,同时判断是否为空,必须和isArray同时使用
   * 因为没有强制类型检查,若传过来的是一个字符串,直接在isBlank中使用isArray最终会返回false,
   * 根据false无法判断该参数是一个非数组还是非空,故必须分开调用isArray和isBlank,同理isNotBlank也是
   * @param {Array} param 参数
   * @return true->空,false->非空
   */
  static isBlank(param) {
    return param.length === 0;
  }

  /**
   * 判断是数组,同时判断是否非空,必须和isArray同时使用
   * @param {Array} param 参数
   * @return true->非空,false->空
   */
  static isNotBlank(param) {
    return param.length > 0;
  }
}
