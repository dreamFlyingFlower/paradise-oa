<template>
  <div class="navbar">
    <!-- 显示隐藏侧边栏按钮 -->
    <hamburger id="hamburger-container" :isActive="sidebar.opened" class="hamburger-container"
               @toggleClick="toggleSideBar"/>
    <!-- 显示面包屑 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" class="app-breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in breadcrumbs" :key="item.path">
        <span v-if="item.redirect==='noRedirect'||index==breadcrumbs.length-1" class="no-redirect">{{ item.meta.title
          }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </el-breadcrumb>

    <div class="right-menu">
      <!-- <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item"/>
        <error-log class="errLog-container right-menu-item hover-effect"/>
        <full-screen id="screenfull" class="right-menu-item hover-effect" />
      </template> -->

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="$store.getters.user.avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/">
            <el-dropdown-item>{{$t('navBar.home')}}</el-dropdown-item>
          </router-link>
          <a target="_blank" href="https://github.com/mygodness100/paradise-oa-web/">
            <el-dropdown-item>Github</el-dropdown-item>
          </a>
          <a target="_blank" href="https://github.com/mygodness100/paradise-oa-web">
            <el-dropdown-item>{{$t("navBar.docs")}}</el-dropdown-item>
          </a>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">{{$t('navBar.logout')}}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import hamburger from "../hamburger";
// import fullScreen from "../fullScreen";
// import search from "../search";
// import errorLog from '../errorLog'

/**
 * 上方导航栏
 */
export default {
  name: "navBar",
  data() {
    return {
      breadcrumbs: null
    };
  },
  components: {
    hamburger
  },
  watch: {
    $route(route) {
      if (route.path.startsWith("/redirect/")) {
        return;
      }
      this.getBreadcrumb();
    }
  },
  computed: {
    ...mapGetters(["sidebar", "device"])
  },
  created() {
    this.getBreadcrumb();
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("TOGGLE_SIDEBAR");
    },
    async logout() {
      await this.$store.dispatch("user/logout");
      this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    },
    getBreadcrumb() {
      let matched = this.$route.matched.filter(
        item => item.meta && item.meta.title
      );
      let first = matched[0];
      if (!this.isHome(first)) {
        matched = [{ path: "/home", meta: { title: "首页" } }].concat(matched);
      }
      this.breadcrumbs = matched.filter(
        item => item.meta && item.meta.title && item.meta.breadcrumb !== false
      );
    },
    isHome(route) {
      let name = route && route.name;
      if (!name) {
        return false;
      }
      return name.trim().toLocaleLowerCase() === "home".toLocaleLowerCase();
    },
    pathCompile(path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route;
      let toPath = pathToRegexp.compile(path);
      return toPath(params);
    },
    handleLink(item) {
      const { redirect, path } = item;
      if (redirect) {
        this.$router.push(redirect);
        return;
      }
      this.$router.push(this.pathCompile(path));
    }
  }
};
</script>

<style lang="scss" scoped>
@import './_index.scss';
</style>