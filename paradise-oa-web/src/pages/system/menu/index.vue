<template>
  <div>
    <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection"
                  @deletes="deletes">
    </nav-operates>
    <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange"
              style="width: 100%;">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="menuName" label="菜单名称"></el-table-column>
      <el-table-column prop="menuPath" label="菜单地址"></el-table-column>
      <el-table-column prop="menuIcon" label="菜单图标"></el-table-column>
      <el-table-column prop="menuI18n" label="国际化名称"></el-table-column>
      <el-table-column prop="redirect" label="跳转地址"></el-table-column>
      <el-table-column prop="sort" label="排序" width="100"></el-table-column>
      <el-table-column prop="setting" label="操作" width="250">
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
    name: "index",
    data() {
      return {
        "api": "menu",
        "primaryKey": "menuId"
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
