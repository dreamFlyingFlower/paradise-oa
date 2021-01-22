<template>
  <div class="el-container">
    <tree-common style="width: 19%;overflow: hidden;" :api="'depart'" :id="$store.getters.user.departId"
      @getTreeData="getTreeData"></tree-common>
    <div style="width: 80%;overflow: hidden;">
      <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection" @deletes="deletes">
      </nav-operates>
      <el-table :data="tableDatas" style="width: 100%">
        <el-table-column type="selection"></el-table-column>
        <el-table-column label="用户编号" prop="userId" width="80"></el-table-column>
        <el-table-column label="用户名" prop="username"></el-table-column>
        <el-table-column label="真实姓名" prop="realname"></el-table-column>
        <el-table-column label="所属部门" prop="departId" width="80"></el-table-column>
        <el-table-column label="性别" prop="sex" width="50"></el-table-column>
        <el-table-column label="薪资" prop="salary"></el-table-column>
        <el-table-column label="手机号" prop="userphone"></el-table-column>
        <el-table-column label="入职时间" prop="createtime"></el-table-column>
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
      <edit v-if="showEditDialog" :show-edit-dialog="showEditDialog" @closeDialog="closeDialog" :edit-data="editData"
            :edit-type="editType"></edit>
    </div>
  </div>
</template>

<script>
import { navOperates, pagination, treeCommon } from '@is';
import edit from './edit';
import { mixinIndex } from "../../../mixin";

export default {
  name: "user",
  data() {
    return {
      api: "user",
      primaryKey: "userId",
      // 当前树选中的节点数据
      currentNode: {}
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
    "currentNode.treeId": function () {
      this.refresh();
    }
  },
  methods: {
    refresh() {
      if (!this.currentNode.treeId) {
        return;
      }
      let params = {
        departId: this.currentNode.treeId,
        pageIndex: this.pageIndex,
        pageSize: this.pageSize
      };
      this.$listByEntity('user', params).then(resp => {
        this.tableDatas = resp.data;
        this.$store.commit('TOTAL', resp.total);
      })
    },
    getTreeData(nodeData) {
      this.currentNode = nodeData;
    }
  }
}
</script>

<style scoped>
</style>
