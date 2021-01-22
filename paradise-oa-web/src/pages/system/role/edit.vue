<template>
  <el-dialog :title="title" :visible.sync="showEditDialog" width="30%" :before-close="beforeClose">
    <el-form ref="formData" :rules="rules" :model="formData" label-width="100px" class="demo-ruleForm">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="formData.roleName" placeholder="请输入角色名" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色描述">
        <el-input v-model="formData.roleDesc" type="textarea" placeholder="请输入角色描述"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取 消</el-button>
      <el-button type="primary" @click="handlerData">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import mixinEdit from '../../../mixin/mixinEdit';
export default {
  name: 'edit',
  props: ['showEditDialog', 'editData', 'editType'],
  data() {
    return {
      api: 'role',
      formData: {
        roleId: null,
        roleName: null,
        roleDesc: null
      },
      rules: {
        // roleName: { required: true, message: '角色名不能为空', trigger: 'blur' }
      }
    };
  },
  mixins: [mixinEdit]
};
</script>
