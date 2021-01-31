export default class DateUtils {
  defaultFormat = "yyyy-MM-dd HH:mm:ss";

  formatterNumber = formatter => {
    let strFormatter = formatter.toString();
    return strFormatter.length >= 2 ? strFormatter : '0' + strFormatter;
  };

  constructor() {

  }

  constructor(date) {

  }

  static formatDateTime (date = new Date()) {
    return this.formatDate(date) + " " + this.formatTime(date);
  }

  static formatDate (date = new Date()) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    return [year, month, day].map(this.formatterNumber).join("-");
  }

  static formatTime (date = new Date()) {
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    return [hour, minute, second].map(this.formatterNumber).join(":");
  }

  static parseDateTime () {
  }

  static parseDateTime (strDate) {

  }

  static parseDateTime (strDate, format) {
    if (arguments.length === 0) {
      return null;
    }
    const format = format || '{y}-{m}-{d} {h}:{i}:{s}';
    let date;
    if (typeof strDate === 'object') {
      date = strDate;
    } else {
      if ((typeof strDate === 'string') && (/^[0-9]+$/.test(strDate))) {
        strDate = parseInt(strDate);
      }
      if ((typeof strDate === 'number') && (strDate.toString().length === 10)) {
        strDate = strDate * 1000;
      }
      date = new Date(strDate);
    }
    const formatObj = {
      y: date.getFullYear(),
      m: date.getMonth() + 1,
      d: date.getDate(),
      h: date.getHours(),
      i: date.getMinutes(),
      s: date.getSeconds(),
      a: date.getDay()
    }
    const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
      let value = formatObj[key];
      if (key === 'a') { return ['日', '一', '二', '三', '四', '五', '六'][value]; }
      if (result.length > 0 && value < 10) {
        value = '0' + value;
      }
      return value || 0;
    })
    return time_str;
  }

  static parseDate () {

  }

  static parseDate (strDate) {

  }

  static parseDate (strDate, format) {

  }

  static parseTime () {

  }

  static parseTime (strDate) {

  }

  static parseTime (strDate, format) {

  }
}