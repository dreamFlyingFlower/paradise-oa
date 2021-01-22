import Cookies from 'js-cookie'

/**
 * 对state中的数据进行修改,但是该方式是静态改变,即不能在axios中使用,会有异步问题,在vue上下文中使用如下:
 * this.$store.commit('TOKEN',"token字符串")
 * this.$store.commit('TOKEN',{token:"token字符串"})
 * this.$store.commit({type:'TOKEN',token:"token字符串"})
 */
export default {
  CONFIG(state, config){
    state.config = config;
  },
  USER(state, user) {
    state.user = user;
  },
  ROLES(state, roles) {
    state.roles = roles
  },
  ROUTES(state, routes) {
    state.routes = routes;
  },
  BUTTONS(state, buttons) {
    state.buttons = buttons;
  },
  FRESH(state, fresh) {
    state.fresh = fresh;
  },
  FRESH_TREE(state, freshTree) {
    state.freshTree = freshTree;
  },
  LANGUAGE(state, language) {
    state.language = language;
  },
  PAGE_INDEX(state, pageIndex) {
    state.pageIndex = pageIndex;
  },
  PAGE_SIZE(state, pageSize) {
    state.pageSize = pageSize;
  },
  TOTAL(state, total) {
    state.total = total;
  },
  EXPAND_KEY(state, expandKey) {
    state.expandKey = expandKey;
  },
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened;
    state.sidebar.withoutAnimation = false;
    if (state.sidebar.opened) {
      Cookies.set('sidebarStatus', 1)
    } else {
      Cookies.set('sidebarStatus', 0)
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set('sidebarStatus', 0);
    state.sidebar.opened = false;
    state.sidebar.withoutAnimation = withoutAnimation
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  SET_SIZE: (state, size) => {
    state.size = size;
    Cookies.set('size', size)
  },
  VISITED_VIEW: (state, view) => {
    if (state.visitedViews.some(v => v.path === view.path)) {
      return;
    }
    state.visitedViews.push(
        Object.assign({}, view, {
          title: view.meta.title || 'no-name'
        })
    )
  },
  CACHED_VIEW: (state, view) => {
    if (state.cachedViews.includes(view.name)) return
    if (!view.meta.noCache) {
      state.cachedViews.push(view.name)
    }
  },
  DEL_VISITED_VIEW: (state, view) => {
    for (const [i, v] of state.visitedViews.entries()) {
      if (v.path === view.path) {
        state.visitedViews.splice(i, 1);
        break
      }
    }
  },
  DEL_CACHED_VIEW: (state, view) => {
    for (const i of state.cachedViews) {
      if (i === view.name) {
        const index = state.cachedViews.indexOf(i)
        state.cachedViews.splice(index, 1)
        break
      }
    }
  },
  DEL_OTHERS_VISITED_VIEWS: (state, view) => {
    state.visitedViews = state.visitedViews.filter(v => {
      return v.meta.affix || v.path === view.path
    })
  },
  DEL_OTHERS_CACHED_VIEWS: (state, view) => {
    for (const i of state.cachedViews) {
      if (i === view.name) {
        const index = state.cachedViews.indexOf(i)
        state.cachedViews = state.cachedViews.slice(index, index + 1)
        break
      }
    }
  },
  DEL_ALL_VISITED_VIEWS: state => {
    // keep affix tags
    const affixTags = state.visitedViews.filter(tag => tag.meta.affix)
    state.visitedViews = affixTags
  },
  DEL_ALL_CACHED_VIEWS: state => {
    state.cachedViews = []
  },
  UPDATE_VISITED_VIEW: (state, view) => {
    for (let v of state.visitedViews) {
      if (v.path === view.path) {
        v = Object.assign(v, view)
        break
      }
    }
  }
}