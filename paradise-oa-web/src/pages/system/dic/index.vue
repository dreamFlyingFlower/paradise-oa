<template>
  <div class="el-container">
    <tree-common style="width: 19%;" @getTreeData="getTreeData" :api="api" :id="1" ref="treeCommon" ></tree-common>
    <div style="width: 80%">
      <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection" @deletes="deletes">
      </nav-operates>
      <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="dicId" label="字典编号"></el-table-column>
        <el-table-column prop="dicName" label="字段名称"></el-table-column>
        <el-table-column prop="dicCode" label="字典编码"></el-table-column>
        <el-table-column prop="pid" label="上级编号"></el-table-column>
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
            :edit-data="editData" :edit-type="editType" :dics="dics" :treeData="currentData"></edit>
    </div>
  </div>
</template>

<script>
  import {mixinIndex} from "../../../mixin";
  import {navOperates, pagination, treeCommon} from '@is';
  import edit from './edit';
  import apiDic from '../../../api/dic'

  export default {
    name: "dic",
    data() {
      return {
        api: "dic",
        primaryKey: "dicId",
        // 当前树选中的节点数据
        currentData: {},
        // 当前树选中节点的dicId
        dicId: null,
        // 最顶级的字典以及子字典列表
        dics: [],
        // 是否存在树形结构
        existTree: true
      }
    },
    components: {
      navOperates,
      pagination,
      treeCommon,
      edit
    },
    mixins: [mixinIndex],
    watch: {
      dicId: function () {
        this.refresh();
      }
    },
    created() {
      this.getDics();
    },
    methods: {
      refresh() {
        this.getTableDatas();
      },
      refreshTree() {
        this.$refs.treeCommon.init();
      },
      getTreeData(nodeData) {
        this.currentData = nodeData;
        this.dicId = nodeData.treeId;
      },
      getTableDatas() {
        if (!this.dicId) {
          return;
        }
        let param = {
          pageIndex: this.pageIndex,
          pageSize: this.pageSize,
          pid: this.dicId
        };
        this.$listByEntity('dic', param).then(resp => {
          this.tableDatas = resp.data;
          this.$store.commit('TOTAL', resp.total);
        })
      },
      getDics() {
        apiDic.getSelfChildren(1).then(resp => {
          this.dics = resp.data;
        });
      }
    }
  }
</script>

<style scoped>

</style>
