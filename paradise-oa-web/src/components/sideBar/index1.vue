<template>
  <div :class="{'has-logo':showLogo}">
    <logo v-if="$store.state.sidebarLogo" :collapse="isCollapse"/>
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu :default-active="activeMenu" :collapse="isCollapse"
               :background-color="variables.menuBg" :text-color="variables.menuText"
               :unique-opened="false" :active-text-color="variables.menuActiveText"
               :collapse-transition="false" mode="vertical">
        <!--  <side-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path"/>-->

        <!--        横版菜单-->
        <!--        <template v-for="(item,index) in routes">-->
        <!--          <el-menu-item v-if="!item.children || item.children.length === 0" :index="index" :key="item.path">-->
        <!--            <i :class="item.iconInfo"></i>{{item.meta}}-->
        <!--          </el-menu-item>-->
        <!--          <el-submenu v-else :class="'menu-'+item.path.replace('/','')" :index="item.path"-->
        <!--                      @mouseenter.native="handleEnter" @mouseleave.native="handleLeave">-->
        <!--            <template slot="title"><i :class="item.iconInfo"></i>{{item.meta}}</template>-->
        <!--            <iframe class="layFrame" :class="'frame-'+item.path.replace('/','')" src=""-->
        <!--                    style="display:none;position:absolute;background-color: transparent;height: 100%;width: 100%;"-->
        <!--                    allowtransparency="true" frameborder="0"></iframe>-->
        <!--            <el-menu-item v-for="(obj,index) in item.children" :index="obj.path" :key="obj.path"-->
        <!--                          @mouseenter.native.stop="handleItemEnter" @mouseleave.native.stop="handleItemLeave">-->
        <!--              {{obj.meta}}-->
        <!--            </el-menu-item>-->
        <!--          </el-submenu>-->
        <!--        </template>-->
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
    import {mapGetters} from 'vuex'
    import logo from './logo'

    import variables from '@css/variables.scss'

    export default {
        name: "index1",
        components: {
            logo
        },
        data() {
            return {}
        },
        computed: {
            ...mapGetters([
                'routes'
            ]),
            isCollapse() {
                return !this.sidebar.opened
            }
        },
        created() {

        }
    }
</script>

<style scoped>

</style>