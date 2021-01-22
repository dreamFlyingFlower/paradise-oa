<template>
  <div>
    <div class="search" v-if="!isEdit">
      <el-input
        placeholder="输入关键字进行过滤"
        v-model="filterText">
      </el-input>
    </div>
    <el-tree
      accordion
      node-key="GroupID"
      :highlight-current="true"
      :show-checkbox="isEdit"
      class="filter-tree"
      :data="data2"
      :props="defaultProps"
      :default-expand-all=false
      :default-checked-keys="defaultCheackedKey"
      :default-expanded-keys="defaultExpandedKey"
      :filter-node-method="filterNode"
      @node-click="handleNodeClick"
      ref="tree2">
    </el-tree>
  </div>
</template>

<script>
  import apiGroup from '@/api/group'
  export default {
    props:["isEdit","checkedGroupData","expandAll"],
    watch: {
      filterText(val) {
        this.$refs.tree2.filter(val);
      },
      checkedGroupData(val){
        this.defaultCheackedKey = []
        Object.assign(this.defaultCheackedKey, val)
      }
    },
    computed:{
      checkedNode(){
        return this.$refs.tree2.getCheckedNodes()
      },
      checkedKey(){
        return this.$refs.tree2.getCheckedKeys()
      },
      halfCheckedNodes(){
        return this.$refs.tree2.getHalfCheckedNodes()
      },
      halfCheckedKeys(){
        return this.$refs.tree2.getHalfCheckedKeys()
      },
    },
    created(){
      this.refresh()
      //放入选中的数据
      if(this.checkedGroupData){
        this.defaultCheackedKey = this.checkedGroupData
      }
    },
    methods: {
      refresh(flag){
        if(flag){
          // flag为true时表示立刻请求后台刷新
          this.getTree();
        }else{
          if(Object.keys(this.$store.state.common.groupTree).length > 0){
            this.data2 = this.$store.state.common.groupTree;
          }
          if(this.data2.length === 0){
            this.getTree();
          }else{
            this.centerGroup = this.data2[0];
            this.$emit("fetchMain",this.centerGroup);
          }
          if(this.data2.length>0){
            this.defaultExpandedKey.push(this.data2[0].GroupID)
          }
        }
      },
      getTree(){
        this.$store.dispatch("GROUPTREE",this.$store.state.user.info.groupId).then((resp)=>{
          this.data2=resp.data;
          this.centerGroup = this.data2[0];
          this.defaultExpandedKey.push(this.centerGroup.GroupID)
          this.$emit("fetchMain",this.centerGroup);
        });
      },
      handleNodeClick(data){
        this.$emit("getGroupData",data)
      },
      filterNode(value, data) {
        if (!value) return true;
        return data.GroupName.indexOf(value) !== -1;
      }
    },
    data() {
      return {
        filterText: '',
        data2: [],
        centerGroup:{},
        defaultCheackedKey:[],
        defaultExpandedKey:[],
        defaultProps: {
          children: 'Children',
          label: 'GroupName'
        }
      };
    }
  };
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>

</style>
