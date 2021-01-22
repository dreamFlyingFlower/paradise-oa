<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key"/>
      </keep-alive>
    </transition>
  </section>
</template>

<script>
  export default {
    name: "appConent",
    computed: {
      cachedViews() {
        return this.$store.state.cachedViews;
      },
      key() {
        return this.$route.path;
      }
    }
  }
</script>
<style lang="scss">
  .el-popup-parent--hidden {
    .fixed-header {
      padding-right: 15px;
    }
  }
</style>
<style scoped>
@import "./_index.scss";
</style>