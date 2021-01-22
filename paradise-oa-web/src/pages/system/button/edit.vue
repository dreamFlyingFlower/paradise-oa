<template>
  <el-dialog :title="title" :visible.sync="showEditDialog" width="30%" :before-close="beforeClose">
    <el-form ref="formData" :model="formData" :rules="rules" class="demo-ruleForm" label-width="100px">
      <el-form-item label="菜单名称" prop="menuId" >
        <el-input v-model="formData.menuName" auto-complete="off" disabled></el-input>
      </el-form-item>
      <el-form-item label="按钮名称" prop="buttonName">
        <el-input v-model="formData.buttonName" auto-complete="off" placeholder="请输入按钮名称"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog(false)">取 消</el-button>
      <el-button type="primary" @click="handlerData">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import mixinEdit from "../../../mixin/mixinEdit";

  export default {
    name: "edit",
    props: ["showEditDialog", "editData", 'editType', 'treeData'],
    data() {
      return {
        api: 'button',
        formData: {
          menuId: null,
          menuName: null,
          buttonId: null,
          buttonName: null
        },
        rules: {
          menuId: [{required: true, message:"所属菜单不能为空"}],
          buttonName: [{required: true, message: "按钮名称不能为空"}]
        }
      }
    },
    mixins: [mixinEdit],
    created() {
      this.init();
    },
    methods: {
      init() {
        if(this.editType === 1){
          this.formData.menuId = this.treeData.treeId;
          this.formData.menuName = this.treeData.treeName;
        }
      }
    }
  }
</script>