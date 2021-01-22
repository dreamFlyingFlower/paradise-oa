<template>
  <div>
    <nav-operates @openEditDialog="openEditDialog" :multipleSelection="multipleSelection"
                  @deletes="deletes">
    </nav-operates>
    <el-table :data="tableDatas" stripe @selection-change="handleSelectionChange"
              style="width: 100%;">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="tos" label="收件人"></el-table-column>
      <el-table-column prop="copys" label="抄送人"></el-table-column>
      <el-table-column prop="content" label="内容"></el-table-column>
      <el-table-column prop="craetetime" label="发送时间"></el-table-column>
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
    name: "email",
    data() {
      return {
        "api": "email",
        "primaryKey": "id"
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
