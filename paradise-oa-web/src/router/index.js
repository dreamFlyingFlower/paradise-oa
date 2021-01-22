import Vue from 'vue';
import Router from 'vue-router';
import Layout from '../components/layout';

Vue.use(Router);

/**
 * 路由配置,除了公共页面外,其他用户根据角色来从数据库读取路由数据
 * hidden:页面是否显示在侧边栏
 * name:必须填写,且必须唯一,否则使用keep-alive标签会出现各种问题
 * meta:
 *  title:在侧边栏的名称显示
 *  icon:设置该路由的图标,对应路径src/icons/svg
 *  breadcrumb:如果设置为false,则不会在breadcrumb面包屑中显示
 */
export default new Router({
  routes: [
    {
      path: '/login',
      component: () => import('@pages/login'),
      meta: {title: "登录"}
    },
    {
      path: "/",
      component: Layout,
      redirect: "/home",
      children: [
        {
          path: "home",
          name: "home",
          component: () => import('@pages/home'),
          meta: {title: "首页"}
        }
      ]
    },
    {
      path: '/icon',
      component: Layout,
      children: [
        {
          path: 'index',
          component: () => import('@pages/icon/index'),
          meta: {title: 'icon', icon: 'icon', noCache: true}
        }
      ]
    },
    {
      path: '/404',
      component: () => import('@pages/error/404'),
      hidden: true
    },
    {
      path: '/401',
      component: () => import('@pages/error/401'),
      hidden: true
    }
    // {
    //     path: '/system',
    //     redirect: '/system/dic',
    //     component: layout,
    //     meta: {title: "系统设置"},
    //     children: [
    //         {
    //             path: "dic",
    //             component: () => import('@pages/system/dic'),
    //             meta: {title: "字典设置"}
    //         },
    //         {
    //             path: "role",
    //             component: () => import('@pages/system/role'),
    //             meta: {title: "角色设置"}
    //         },
    //         {
    //             path: "menu",
    //             component: () => import('@pages/system/menu'),
    //             meta: {title: "菜单设置"}
    //         },
    //     ]
    // },
    // {
    //     path:"/biz",
    //     component:layout,
    //     children:[
    //         {
    //             path: "depart",
    //             component: () => import('@pages/biz/depart'),
    //             meta: {title: "部门设置"}
    //         },
    //         {
    //             path: "user",
    //             component: () => import('@pages/biz/user'),
    //             meta: {title: "用户设置"}
    //         }
    //     ]
    // },
  ]
})
