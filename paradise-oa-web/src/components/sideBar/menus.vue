<template>
  <div v-if="!item.hidden" class="menu-wrapper">
    <template v-if="!hasChild(item.children,item)">
      <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
        <svg-icon icon-class="user" />
        <span>{{item.meta.title}}</span>
      </el-menu-item>
    </template>

    <el-submenu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template slot="title">
        <svg-icon icon-class="user" />
        <span>{{item.meta.title}}</span>
      </template>
      <menus v-for="child in item.children" :key="child.path" :is-nest="true" :item="child"
        :base-path="resolvePath(child.path)" class="nest-menu" />
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'

export default {
  name: 'menus',
  props: {
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data () {
    this.onlyOneChild = null;
    return {};
  },
  methods: {
    hasChild (children = [], parent) {
      if (children.length === 0 && parent.hidden === 0) {
        this.onlyOneChild = { ...parent, path: '' }
        return false;
      }
      return true;
    },
    resolvePath (routePath) {
      if (this.$isExternal(routePath)) {
        return routePath;
      }
      if (this.$isExternal(this.basePath)) {
        return this.basePath;
      }
      return path.resolve(this.basePath, routePath);
    }
  }
}
</script>