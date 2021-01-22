// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store';
import i18n from './lang';
import ElementUI from 'element-ui';
import Api from './global/api';
import Funcs from './global/funcs';
import axios from 'axios';

import './icons';
import 'element-ui/lib/theme-chalk/index.css';
import 'normalize.css/normalize.css';
import '@css/index.scss';
import '@css/element-variables.scss';
// 每次跳转页面检查token,role是否存在
import './router/filter'

//https://panjiachen.github.io/vue-element-admin/#/i18n/index
// 此处使用i18n后的t不可更改,否则报错.在vue中使用$t("key"),类似于map
Vue.use(ElementUI, {
  i18n: (key, value) => i18n.t(key, value)
});
// 注入全局通用API
Vue.use(Api);
// 注入全局通用函数
Vue.use(Funcs);

// 取消vue的所有日志和警告,true取消,false不取消
Vue.config.silent = false;
Vue.config.productionTip = false;
// 避免用控制台操作vue,在正式环境会自动编译为false
Vue.config.devtools = process.env.NODE_ENV === "development";

/* eslint-disable no-new */
// 不需要每次修改了配置文件之后重新打包上传
axios.get('http://localhost:12346/static/config.json').then(resp => {
  store.commit("CONFIG", resp.data[process.env.NODE_ENV]);
  new Vue({
    el: '#app',
    router,
    store,
    i18n,
    components: {
      App
    },
    template: '<App/>',
  });
});
