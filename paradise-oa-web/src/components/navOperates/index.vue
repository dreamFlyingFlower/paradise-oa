<template>
  <div class="index-operate">
    <el-form :inline="true">
      <el-form-item v-if="ifSearch">
        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
      </el-form-item>
      <el-form-item v-if="ifImportExcel">
        <el-button type="primary" icon="el-icon-upload2" @click="importExcel">导入</el-button>
      </el-form-item>
      <el-form-item v-if="ifExportExcel">
        <el-button type="danger" icon="el-icon-download" @click="exportExcel">导出
        </el-button>
      </el-form-item>
      <el-form-item v-if="!ifEdit">
        <el-button type="primary" icon="el-icon-plus" @click="openEditDialog">新增</el-button>
      </el-form-item>
      <el-form-item v-if="!ifDeletes">
        <el-button type="danger" icon="el-icon-delete" @click="deletes"
                   :disabled="multipleSelection.length === 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  export default {
    name: "index",
    props: ['ifSearch', 'ifImportExcel', 'ifExportExcel', 'ifEdit', 'ifDeletes', 'multipleSelection'],
    methods: {
      search() {
        if(this.ifSearch){
          this.$emit("search",q);
        }
      },
      importExcel() {
        if(this.ifImportExcel){
          this.$emit("importExcel");
        }
      },
      exportExcel() {
        if (this.ifExportExcel) {
          this.$emit("exportExcel");
        }
      },
      openEditDialog() {
        if (!this.ifEdit) {
          this.$emit("openEditDialog", null, 1);
        }
      },
      deletes() {
        if (!this.ifDeletes) {
          this.$emit("deletes");
        }
      }
    }
  }
</script>
<style>
  .index-operate .el-form .el-form-item {
    margin-bottom: 5px;
  }
</style>
<style scoped>
  .index-operate {
    margin-top: 10px;
    margin-right: 0;
    text-align: right;
  }
</style>