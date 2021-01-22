<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <side-bar class="sidebar-container" />
    <div :class="{ hasTagsView: needTagsView }" class="main-container">
      <div :class="{ 'fixed-header': fixedHeader }">
        <nav-bar />
<!--        <nav-tags v-if="needTagsView" />-->
      </div>
      <app-content />
      <!--     <right-panel v-if="showSettings">-->
      <!--           <setting/>-->
      <!--      </right-panel>-->
    </div>
  </div>
</template>

<script>
import { appContent, navBar, sideBar } from '@is'
import { resize } from '@mixin'
import { mapState } from 'vuex'

export default {
  name: 'layout-index',
  components: {
    // 主体router-view
    appContent,
    // 上方导航栏
    navBar,
    // 侧边菜单栏
    sideBar,
    // navTags
    // rightPanel,
    // setting,
  },
  mixins: [resize],
  computed: {
    ...mapState([
      'sidebar',
      'device',
      'showSettings',
      'needTagsView',
      'fixedHeader'
    ]),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('CLOSE_SIDEBAR', {
        withoutAnimation: false
      })
    }
  }
}
</script>

<style scoped>
</style>