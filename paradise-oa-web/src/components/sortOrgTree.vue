<template>
  <div>
    <el-popover
      ref="popover"
      placement="bottom-start"
      trigger="click"
      @show="onShowPopover"
      @hide="onHidePopover">
      <el-tree
        ref="tree"
        accordion
        node-key="groupId"
        :style="`min-width: ${treeWidth}`"
        class="select-tree"
        :data="options"
        :props="props"
        :expand-on-click-node="false"
        :default-expand-all="false"
        @node-click="onClickNode">
      </el-tree>
      <el-input
        slot="reference"
        ref="input"
        v-model="labelModel"
        suffix-icon="el-icon-arrow-down">
      </el-input>
    </el-popover>
  </div>
</template>

<script>
  import apiGroup from '@/api/group'
  export default {
    props:["GroupID","pageGroup"],
    created(){
      this.refresh()
      this.$nextTick(() => {
        // 获取输入框宽度同步至树状菜单宽度
        this.treeWidth = `${(this.width || this.$refs.input.$refs.input.clientWidth) - 24}px`;
      });
    },
    methods: {
      refresh(){
        if(Object.keys(this.$store.state.common.groupTree).length > 0){
          this.initTree(this.$store.state.common.groupTree);
        }else{
          this.$store.dispatch("GROUPTREE",this.$store.state.user.info.groupId).then((resp)=>{
            this.initTree(resp.data);
          });
        }
      },
      initTree(treeData){
        this.options = treeData;
        this.centerGroup = treeData[0]
        this.$emit("fetchMain",this.centerGroup)
        if (this.GroupID) {
          this.labelModel = this.queryTree(this.options, this.GroupID);
        }
      },
      // 搜索树状数据中的 ID
      queryTree(tree, id) {
        let stark = [];
        stark = stark.concat(tree);
        while (stark.length) {
          const temp = stark.shift();
          if (temp[this.props.children]) {
            stark = stark.concat(temp[this.props.children]);
          }
          if (temp[this.props.value] === id) {
            return temp[this.props.label];
          }
        }
        return '';
      },
      // 单击节点
      onClickNode(node) {
        // 若是来自group页面
        if(this.pageGroup){
          this.$getById('group',node.GroupID).then((res)=>{
            if(res.data.ParentTeamID){
              this.$message("该站点下不能添加站点");
              return;
            }else{
              this.relationData(node);
            }
          });
        }else{
          this.relationData(node);
        }
      },
      relationData(node){
        this.labelModel = node[this.props.label];
        this.valueModel = node[this.props.value];
        this.onCloseTree();
        this.$emit('clickNode',node)
      },
      // 隐藏树状菜单
      onCloseTree() {
        this.$refs.popover.showPopper = false;
      },
      // 显示时触发
      onShowPopover() {
        this.showStatus = true;
      },
      // 隐藏时触发
      onHidePopover() {
        this.showStatus = false;
        this.$emit('selected', this.valueModel);
      },
    },
    data() {
      return {
        // 树状菜单显示状态
        showStatus: false,
        // 菜单宽度
        treeWidth: 'auto',
        labelModel:'',
        valueModel:this.groupId,
        options: [],
        props:{
          children: 'Children',
          label: 'GroupName',
          value: 'GroupID'
        }
      };
    }
  };
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  .select-tree {
    max-height: 350px;
    overflow-y: scroll;
  }
  /* 菜单滚动条 */
  .select-tree::-webkit-scrollbar {
    z-index: 11;
    width: 6px;
  }
  .select-tree::-webkit-scrollbar-track,
  .select-tree::-webkit-scrollbar-corner {
    background: #fff;
  }
  .select-tree::-webkit-scrollbar-thumb {
    border-radius: 5px;
    width: 6px;
    background: #b4bccc;
  }
  .select-tree::-webkit-scrollbar-track-piece {
    background: #fff;
    width: 6px;
  }
</style>
