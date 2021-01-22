// 通用增删改查分页
import service from '@utils/service';
import {Message, MessageBox} from 'element-ui';
import store from '@/store';

export default {
  install: function (Vue) {
    // 通用增删改查等,api是对应接口的前缀.如user/login,则api为user.非共用方法写在src/api的各自js中
    /**
     * 通用调用方法,只支持get和post
     * @param url 请求地址
     * @param method get或post
     * @param msg 通用结果处理的提示信息.若不传,则不提示任何信息
     * @param handler 是否通用处理,判断为false时为通用处理,true时返回Promise.默认通用结果处理
     * @param callback 回调函数,handler判断为false时才会调用.默认不调用
     * @param fresh 是否刷新当前页面的数据展示,判断为false时刷新,true不刷新
     * @param freshTree 是否刷新树形结构,判断为false时刷新,true不刷新
     * @returns {*}
     */
    Vue.prototype.$common = function (url, params, method, msg, handler, callback, fresh,
                                      freshTree) {
      if (!handler) {
        if (callback) {
          if (method.toLowerCase() === 'get') {
            service.get(url, params).then((resp) => {
              callback(resp);
            });
          } else if (method.toLowerCase() === 'post') {
            service.post(url, params).then((resp) => {
              callback(resp);
            });
          }
        } else {
          if (method.toLowerCase() === 'get') {
            service.get(url, params).then((resp) => {
              if (resp.code === 1 && msg) {
                Message.success(msg);
              }
              store.commit('FRESH', !fresh);
              store.commit('FRESH_TREE', !freshTree);
            });
          } else if (method.toLowerCase() === 'post') {
            service.post(url, params).then((resp) => {
              if (resp.code === 1 && msg) {
                Message.success(msg);
              }
              store.commit('FRESH', !fresh);
              store.commit('FRESH_TREE', !freshTree);
            });
          }
        }
      } else {
        return method.toLowerCase() === 'get' ? service.get(url, params)
          : service.post(url, params);
      }
    };
    /**
     * 通用新增
     * @param api 后台API接口前缀
     * @param params 对象参数
     * @param handler 是否对promise结果进行通用处理,若不传或传false则使用通用结果处理,true则返回Promise
     * @param callback 回调方法.该参数只能是handler判断为false时使用,将通用结果处理的结果传入回调方法的参数中
     * @param fresh 是否修改store中的fresh为刷新状态,默认或不传则刷新,传true或其他非false参数则刷新
     * @param freshTree 是否修改store中的freshTree为状态,默认或不传则刷新,传true或其他非false参数则刷新
     */
    Vue.prototype.$create = function (api, params, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/create`, params, "post", '新增成功', handler, callback, fresh, freshTree);
    };
    /**
     * 通用单条数据删除,id只能是数字,不可是对象或数组等
     */
    Vue.prototype.$remove = function (api, id, handler, callback, fresh, freshTree) {
      MessageBox.confirm('此操作将删除所选数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return new Vue().$common(`${api}/remove/${id}`, {}, 'get', '删除成功', handler, callback, fresh, freshTree);
      }).catch((error) => {
        console.log(error);
        Message.info('已取消删除');
      });
    };
    /**
     * 通用批量删除,id都必须是数字类型
     * @param ids 是一个单纯的数组,而不是一个对象,虽然数组也是对象.例如[1,2],而不是{ids:[1,2]}
     */
    Vue.prototype.$removes = function (api, ids, handler, callback, fresh, freshTree) {
      MessageBox.confirm('此操作将删除所选数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return new Vue().$common(`${api}/removes`, ids, 'post', '删除成功', handler, callback, fresh, freshTree);
      }).catch(() => {
        Message.info('已取消删除');
      });
    };
    /**
     * 通用全量字段修改
     */
    Vue.prototype.$edit = function (api, params, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/edit`, params, 'post', '修改成功', handler, callback, fresh, freshTree);
    };
    /**
     * 通用查询表中是否有重复字段
     */
    Vue.prototype.$hasValue = function (api, params, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/hasValue`, params, 'post', null,
        handler === undefined ? true : false, callback, fresh, freshTree);
    };
    /**
     * 通用根据主键编号查询详情,主键必须是数字类型
     */
    Vue.prototype.$getById = function (api, id, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/getById/${id}`, {}, 'get', null, handler, callback, fresh, freshTree);
    };
    /**
     * 通用根据主键编号查询树形结构数据,主键为数字类型
     */
    Vue.prototype.$getTree = function (api, id, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/getTree/${id}`, {}, 'get', null,
        handler === undefined ? true : false, callback, fresh, freshTree);
    };
    /**
     * 通用单表数据分页/不分页查询,参数是表中非null字段的值,且条件只能是相等
     */
    Vue.prototype.$listByEntity = function (api, params, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/listByEntity`, params, 'get', null,
        handler === undefined ? true : false, callback, fresh, freshTree);
    };
    /**
     * 通用联表数据分页/不分页查询,参数根据后台接口而定,参数可是任意类型,暂未使用,FIXME
     */
    Vue.prototype.$listByPage = function (api, params, handler, callback, fresh, freshTree) {
      return new Vue().$common(`${api}/listByPage`, params, 'post', null,
        handler === undefined ? true : false, callback, fresh, freshTree);
    };
    // 文件上传,未测试
    Vue.prototype.$upload = (api, files, params) => {
      let formData = new FormData();
      formData.append("file", files);
      if (!typeof params === "object") {
        console.log("参数错误,只能是对象");
      }
      let entrys = Object.entries(params);
      for (let [key, val] of entrys) {
        formData.append(key, val);
      }
      return new Promise((resolve, reject) => {
        service.put({
          url: `${api}/upload`,
          method: "post",
          contentType: false,
          data: formData
        }).then(response => {
          resolve(response);
        }, err => {
          reject(err)
        })
      })
    };
    // 数据导出
    Vue.prototype.$exportExcel = (api, params) => {
      let queryString = [];
      for (let key in params) {
        queryString.push(key + "=" + params[key]);
      }
      window.location.href = `${process.env.API_URL}export/${api}` + (queryString.length > 0
        ? "?" + queryString.join("&") : "");
    }
  }
};
