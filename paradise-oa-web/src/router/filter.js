import store from '../store';
import router from './index';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import cookie from '../utils/cookie';
import getPageTitle from '@utils/getPageTitle';
import layout from '@is/layout';
import {
  Message
} from 'element-ui';
import ArrayUtils from "@utils/ArrayUtils";

NProgress.configure({
  showSpinner: false
});

let whiteNames = ["/login", "/auth-redirect"];
router.beforeEach(async (to, from, next) => {
  NProgress.start();
  // 设置页面标题
  document.title = getPageTitle(to.meta.title);
  // 是否检查token
  if (store.getters.config.USE_TOKEN) {
    const token = cookie.getToken();
    if (token) {
      checkRole(to,next);
    } else {
      // 没有token
      if (whiteNames.indexOf(to.path) !== -1) {
        // 在白名单中
        next();
      } else {
        // 不在白名单中,重定向到登录页面,并带上当前想去的页面地址,方便登录之后直接重定向到页面
        next(`/login?redirect=${to.path}`);
        NProgress.done();
      }
    }
  } else {
    checkRole(to,next);
  }
});

/**
 * 检查是否有角色信息,无角色信息,不可登录
 */
function checkRole(to,next) {
  if (whiteNames.indexOf(to.path) !== -1) {
    next();
    return;
  }
  let hasRoutes = ArrayUtils.isArray(store.getters.roles) && ArrayUtils.isNotBlank(store.getters.roles) &&
    ArrayUtils.isArray(store.getters.routes) && ArrayUtils.isNotBlank(store.getters
      .routes);
  if (hasRoutes) {
    if (to.path === '/login') {
      clearCookie();
      next();
      NProgress.done();
    } else {
      next();
    }
  } else {
    try {
      store.dispatch('ROUTES', store.getters.roles[0].roleId).then(data => {
        handlerRoutes(data);
        next({ ...to,
          replace: true
        })
      });
    } catch (error) {
      clearCookie();
      Message.error(error || 'Has Error');
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
}

/**
 * 清除cookie中的用户信息和token信息
 */
function clearCookie() {
  cookie.removeUser();
  cookie.removeToken();
}

router.afterEach(() => {
  NProgress.done()
});

/**
 * 构建router
 * @param {Array} menus 菜单信息
 */
function handlerRoutes(menus) {
  let routes = buildRoutes(menus);
  router.options.routes.push(routes);
  router.addRoutes(routes);
  store.commit('ROUTES', routes);
}

function buildRoutes(menus) {
  let routes = [];
  for (let menu of menus) {
    let children = menu.children;
    let route = {
      path: menu.menuPath,
      name: menu.routerName,
      redirect: menu.redirect,
      iconInfo: menu.menuIcon,
      hidden: menu.hidden,
      meta: {
        title: menu.menuName
      }
    };
    if (children && children.length > 0) {
      children = buildRoutes(children);
      route.component = layout;
      route.children = children;
    } else {
      // import中的参数不能用在变量,也不能用``,要么是一个完成的字符换,要么就字符串拼接
      route.component = () => import('@/pages' + menu.menuView);
    }
    routes.push(route);
  }
  return routes;
}
