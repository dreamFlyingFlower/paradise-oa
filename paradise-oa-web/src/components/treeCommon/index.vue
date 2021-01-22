<template>
  <div>
    <el-tree
            accordion
            node-key="treeId"
            ref="tree"
            :data="treeDatas"
            :props="defaultProps"
            :highlight-current="true"
            :default-expand-all="expandAll"
            :show-checkbox="checkbox"
            :default-expanded-keys="defaultExpandedKeys"
            :default-checked-keys="defaultCheckedKeys"
            @node-click="handleNodeClick"></el-tree>
  </div>
</template>

<script>
  export default {
    name: "treeCommon",
    props: {
      // 必传,根据不同类型选用不同的api
      api: {
        type: String,
        required: true
      },
      // 必传,查询条件
      id: {
        type: [String, Number],
        required: true
      },
      // 默认是否展开
      expandAll: {
        type: Boolean,
        default: false
      },
      // 是否默认展开节点,只能展开一个节点
      ifExpandKey:{
        type:Boolean,
        default:true
      },
      // 节点是否可被选择
      checkbox: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        treeDatas: [],
        defaultProps: {
          children: "children",
          label: "treeName"
        },
        // 默认展开的节点,数组值为treeId的值
        defaultExpandedKeys: [],
        // 默认选中的节点,数组值为treeId的值
        defaultCheckedKeys: []
      }
    },
    created() {
      this.init();
    },
    methods: {
      /**
       * 获得树形结构数据
       */
      init() {
        this.$getTree(this.api, this.id).then(resp => {
          if (resp.data && resp.data.length > 0) {
            this.treeDatas = resp.data;
            this.$emit("getTreeData", resp.data[0]);
            if(this.ifExpandKey && this.$store.getters.expandKey){
              this.defaultExpandedKeys = [];
              this.defaultExpandedKeys.push(this.$store.getters.expandKey);
            }
          }
        })
      },
      /**
       * 点击节点触发事件
       * @param data 节点数据
       */
      handleNodeClick(data) {
        this.$emit("getTreeData", data);
        if(this.ifExpandKey){
          this.$store.commit("EXPAND_KEY",data.treeId);
        }
      },
      /**
       * 当checkbox为true时,返回当前被选中节点组成的数组
       * @param leafOnly 是否只是叶子节点
       * @param includeHalfChecked 是否包含半选节点
       */
      getCheckNodes(leafOnly = false, includeHalfChecked = false) {
        return this.$refs.tree.getCheckNodes(leafOnly, includeHalfChecked);
      },
      /**
       * 当checkbox为true时,返回当前被选中节点的key组成的数组
       * @param leafOnly true返回被选中的叶子节点的key,默认false
       */
      getCheckedKeys(leafOnly = false) {
        return this.$refs.tree.getCheckedKeys(leafOnly);
      },
      /**
       * 当checkbox为true时,返回当前半选中节点组成的数组
       */
      getHalfCheckedNodes() {
        return this.$refs.tree.getHalfCheckedNodes();
      },
      /**
       * 当checkbox为true时,返回当前半选中节点的key组成的数组
       */
      getHalfCheckedKeys() {
        return this.$refs.tree.getHalfCheckedKeys();
      },
      /**
       * 获取当前被选中节点的node,若没有节点被选中则返回 null
       */
      getCurrentNode() {
        return this.$refs.tree.getCurrentNode();
      },
      /**
       * 获取当前被选中节点的 key,使用此方法必须设置 node-key 属性,若没有节点被选中则返回 null
       */
      getCurrentKey() {
        return this.$refs.tree.getCurrentKey();
      },
      /**
       * 设置当前勾选的节点,必须设置node-key
       * @param nodes
       */
      setCheckedNodes(nodes = []) {
        this.$refs.tree.setCheckedNodes(nodes);
      },
      /**
       * 通过key设置当前勾选的节点,必须设置node-key
       * @param keys 勾选节点的key数组
       * @param leafOnly true则只设置叶子节点的选中状态,默认false
       */
      setCheckedKeys(keys = [], leafOnly = false) {
        this.$refs.tree.setCheckedKeys(keys, leafOnly);
      },
      /**
       * 通过key设置某个节点的当前选中状态,使用此方法必须设置node-key 属性
       * @param key 需要进行设置的key值
       */
      setCurrentKey(key) {
        this.$refs.tree.setCurrentKey(key);
      }
    },
    destroyed() {
      this.$store.commit("EXPAND_KEY",null);
    }
  }
</script>

<style scoped>

</style>