<template>
  <div>
    <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection"
                  @deletes="deletes">
    </nav-operates>
    <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange"
              style="width: 100%;">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="applyId" label="申请编号"></el-table-column>
      <el-table-column prop="applyName" label="申请人"></el-table-column>
      <el-table-column prop="applyDate" label="申请日期"></el-table-column>
      <el-table-column prop="begintime" label="请假开始时间"></el-table-column>
      <el-table-column prop="endtime" label="请假结束时间"></el-table-column>
      <el-table-column prop="applyType" label="请假类型"></el-table-column>
      <el-table-column prop="reason" label="请假原因"></el-table-column>
      <el-table-column prop="applyState" label="请假状态"></el-table-column>
      <el-table-column prop="approveName" label="批准人"></el-table-column>
      <el-table-column prop="approveDate" label="批准日期"></el-table-column>
      <el-table-column prop="remark" label="说明"></el-table-column>
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
          :edit-data="editData" :edit-type="editType"></edit>
  </div>
</template>

<script>
  import {mixinIndex} from "../../../mixin";
  import {navOperates, pagination} from '@is';
  import edit from './edit';

  export default {
    name: "approve",
    data() {
      return {
        api: "approve",
        primaryKey: "approveId"
      }
    },
    components: {
      navOperates,
      pagination,
      edit
    },
    mixins: [mixinIndex],
    methods: {
      refresh() {
        this.$listByEntity(this.api, {pageIndex: this.pageIndex, pageSize: this.pageSize}).then(resp => {
          if (this.$isArray(resp.data) && resp.data.length > 0) {
            this.tableDatas = resp.data;
            this.$store.commit('TOTAL', resp.total);
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>
