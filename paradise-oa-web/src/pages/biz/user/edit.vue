<template>
  <el-dialog :title="title" center :visible.sync="showEditDialog" width="30%" :before-close="beforeClose">
    <el-form ref="formData" :model="formData" :rules="rules" class="demo-ruleForm" label-width="100px">
      <el-form-item label="所属部门" prop="departName">
        <el-input v-model="formData.departName" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="formData.username" placeholder="请输入用户名" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="formData.password" placeholder="请输入密码" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="真实姓名">
        <el-input v-model="formData.realname" placeholder="请输入真实姓名" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-input v-model="formData.sex" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="userphone">
        <el-input v-model="formData.userphone" placeholder="请输入手机号" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="身份证号">
        <el-input v-model="formData.idcard" placeholder="请输入身份证号" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="生日">
        <el-input v-model="formData.birthday" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="年龄">
        <el-input v-model="formData.age" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="住址">
        <el-input v-model="formData.address" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮件">
        <el-input v-model="formData.email" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="薪资">
        <el-input v-model="formData.salary" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="用户状态">
        <el-input v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="教育程度">
        <el-input v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="毕业学校">
        <el-input v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="工作经历">
        <el-input v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="兴趣">
        <el-input v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="离职时间">
        <el-input v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog(false)">取 消</el-button>
      <el-button type="primary" @click="handlerData">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "edit",
    data() {
      return {}
    },
    methods: {
      // 当动态改变某些校验条件时,会遗留上一次的检验结果,可用以下方法清除上次的校验结果
      // 清除所有的检验结果
      // this.$refs.formData.resetFields();
      // 主动校验某个字段
      // this.$refs.formData.validateField('fieldName');
      // 清除某个字段的校验结果
      // this.$refs.formData.forEach(e =>{if(e.prop === 'fieldName'){do something}})
    }
  }
</script>

<style scoped>

</style>