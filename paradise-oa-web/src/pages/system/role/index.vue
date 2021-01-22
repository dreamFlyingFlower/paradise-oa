<template>
  <div>
    <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection"
                  @deletes="deletes">
    </nav-operates>
    <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange"
              style="width: 100%;" :highlight-current-row="true">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="roleId" label="角色编号" width="100px"></el-table-column>
      <el-table-column prop="roleName" label="角色名称"></el-table-column>
      <el-table-column prop="roleDesc" label="描述"></el-table-column>
      <el-table-column prop="setting" width="250" label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" icon="el-icon-edit" @click="openEditDialog(scope.row,2)">编辑
          </el-button>
          <el-button type="text" size="small" icon="el-icon-delete" @click="$remove(api,scope.row[primaryKey])">删除
          </el-button>
          <router-link :to="{name: 'assign', params: {roleId: scope.row.roleId}}" tag="a" style="margin-left: 10px">
            <el-button type="text" size="small" icon="el-icon-star-off">权限分配</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>
    <pagination></pagination>
    <edit v-if="showEditDialog" :show-edit-dialog="showEditDialog" :edit-data="editData" :edit-type="editType"
          @closeDialog="closeDialog"></edit>
  </div>
</template>

<script>
  import mixinIndex from '@/mixin/mixinIndex';
  import edit from './edit';
  import {pagination, navOperates} from '@is';

  export default {
    name: "role",
    data() {
      return {
        api: 'role',
        primaryKey: 'roleId'
      }
    },
    components: {
      pagination,
      navOperates,
      edit
    },
    mixins: [mixinIndex],
    created() {
    },
    methods: {
      refresh() {
        this.getTableDatas();
      },
      getTableDatas() {
        this.$getPage('role', {pageIndex: this.pageIndex, pageSize: this.pageSize}).then(resp => {
          this.tableDatas = resp.data;
          this.$store.commit('TOTAL', resp.total);
        });
      }
    }
  }
</script>

<style scoped>

</style>