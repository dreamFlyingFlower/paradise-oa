class dateUtils {
  defaultFormat = "yyyy-MM-dd HH:mm:ss";

  formatterNumber = formatter => {
    let strFormatter = formatter.toString();
    return strFormatter.length >= 2 ? strFormatter : '0' + strFormatter;
  };

  constructor() {

  }

  constructor(date) {

  }

  static formatDateTime(date = new Date()) {
    let ymd = formatDate(date);
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    return [year, month, day].map(this.formatterNumber).join("-") + " "
        + [hour, minute, second].map(this.formatterNumber).join(":");
  }

  static formatDate(date = new Date()) {
    let year = date.getFullYear();
    let month = date.getMonth() + 1;
    let day = date.getDate();
    return [year, month, day].map(this.formatterNumber).join("-");
  }

  static formatTime(date = new Date()) {
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    return [hour, minute, second].map(this.formatterNumber).join(":");
  }

  static parseDateTime() {
  }

  static parseDateTime(strDate) {

  }

  static parseDateTime(strDate, format) {

  }

  static parseDate() {

  }

  static parseDate(strDate) {

  }

  static parseDate(strDate, format) {

  }

  static parseTime() {

  }

  static parseTime(strDate) {

  }

  static parseTime(strDate, format) {

  }
}