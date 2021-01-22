import cookie from '../utils/cookie'

export default {
  config:{},
  user: {},
  userphoto: cookie.getUser() ? cookie.getUser().userphoto : null,
  roles: cookie.getUser() && Object.keys(cookie.getUser()).length > 0 ? cookie.getUser().roles : [],
  routes: [],
  buttons: [],
  // 是否刷新当前页面
  fresh: false,
  // 若当前页面中有树形结构,是否刷新
  freshTree: false,
  language: cookie.get('language') || 'cn',
  pageIndex: 1,
  pageSize: 10,
  total: 0,
  // 若是页面中树形结构数据,且需要默认展开树时的节点key
  expandKey: null,
  sidebar: {
    opened: cookie.get('sidebarStatus') ? !!+cookie.get('sidebarStatus') : true,
    withoutAnimation: false
  },
  sidebarLogo: false,
  device: "desktop",
  size: cookie.get('size') || 'medium',
  showSettings: true,
  needTagsView: true,
  fixedHeader: false,
  errorLogs: [],
  visitedViews: [],
  cachedViews: []
}