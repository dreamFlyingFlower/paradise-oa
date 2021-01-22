<template>
  <div class="el-container">
    <tree-common style="width: 19%;" @getTreeData="getTreeData" :api="'menu'" :id="1" ref="treeCommon"></tree-common>
    <div style="width: 80%">
      <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection" @deletes="deletes">
      </nav-operates>
      <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="buttonId" label="按钮编号"></el-table-column>
        <el-table-column prop="menuName" label="菜单名称"></el-table-column>
        <el-table-column prop="buttonName" label="按钮名称"></el-table-column>
        <el-table-column prop="setting" width="150" label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="openEditDialog(scope.row,2)">编辑
            </el-button>
            <el-button type="text" size="small" icon="el-icon-delete" @click="$remove(api,scope.row[primaryKey])">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination></pagination>
      <edit v-if="showEditDialog" :show-edit-dialog="showEditDialog" @closeDialog="closeDialog"
            :edit-data="editData" :edit-type="editType" :treeData="currentNode"></edit>
    </div>
  </div>
</template>

<script>
  import {navOperates, pagination, treeCommon} from '@is';
  import edit from './edit';

  import {mixinIndex} from "../../../mixin";

  export default {
    // 该页面可不用name,用则不可用button,会和html标签重名
    name: "buttons",
    data() {
      return {
        api:"button",
        primaryKey:"buttonId",
        currentNode:{},
        menuId:null
      }
    },
    components:{
      navOperates,
      pagination,
      treeCommon,
      edit
    },
    mixins:[mixinIndex],
    methods:{
      refresh() {
        if(!this.menuId){
          return;
        }
        this.$getPage(this.api,{pageIndex:this.pageIndex,pageSize:this.pageSize,menuId:this.menuId}).then(resp=>{
          this.tableDatas = resp.data;
          this.$store.commit('TOTAL', resp.total);
        });
      },
      getTreeData(treeData){
        this.currentNode = treeData;
        this.menuId = treeData.treeId;
        this.refresh();
      }
    }
  }
</script>

<style scoped>

</style>