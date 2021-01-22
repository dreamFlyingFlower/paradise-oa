<template>
  <div>
    <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection" @deletes="deletes">
    </nav-operates>
    <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange" style="width: 100%;">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="departId" label="部门编号" width="80"></el-table-column>
      <el-table-column prop="departName" label="部门名称"></el-table-column>
      <el-table-column prop="departNo" label="部门编码"></el-table-column>
      <el-table-column prop="pid" label="上级部门"></el-table-column>
      <el-table-column prop="departDesc" label="描述"></el-table-column>
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
</template>

<script>
import { navOperates, pagination } from '../../../components';
import edit from './edit';
import mixinIndex from '../../../mixin/mixinIndex';

export default {
  name: 'depart',
  data() {
    return {
      api: 'depart',
      primaryKey: 'departId'
    };
  },
  components: {
    navOperates,
    pagination,
    edit
  },
  mixins: [mixinIndex],
  methods: {
    refresh() {
      this.$listByEntity(this.api, { pageIndex: this.pageIndex, pageSize: this.pageSize }).then(resp => {
        if (this.$isArray(resp.data) && resp.data.length > 0) {
          this.tableDatas = resp.data;
          this.$store.commit('TOTAL', resp.total);
        }
      });
    }
  }
};
</script>

<style scoped>
</style>
