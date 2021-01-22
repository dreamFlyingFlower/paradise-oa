// 国际化
import Vue from 'vue';
import VueI18n from 'vue-i18n';
import Cookies from 'js-cookie';
import elementEnLocale from 'element-ui/lib/locale/lang/en';
import elementZhLocale from 'element-ui/lib/locale/lang/zh-CN';
import enLocale from './en';
import zhLocale from './zh';

/**
 * 对语言进行选择,做成可选择,但是本项目只做成中文,
 * @example http://element-cn.eleme.io/#/zh-CN/component/i18n
 */
Vue.use(VueI18n);

const messages = {
  en: {
    ...enLocale,
    ...elementEnLocale
  },
  zh: {
    ...zhLocale,
    ...elementZhLocale
  }
}

const i18n = new VueI18n({
  // 设置本地语言,可从messsges中选
  locale: Cookies.get('language') || 'zh',
  messages
})

export default i18n