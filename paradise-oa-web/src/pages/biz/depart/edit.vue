<template>
  <el-dialog :title="title" center :visible.sync="showEditDialog" width="30%" :before-close="beforeClose">
    <el-form ref="formData" :model="formData" :rules="rules" class="demo-ruleForm" label-width="100px">
      <el-form-item label="部门名称" prop="departName">
        <el-input v-model="formData.departName" placeholder="请输入部门名称" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="部门编码" prop="departNo">
        <el-input v-model="formData.departNo" placeholder="请选择上级部门" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="上级部门" prop="pid">
        <el-select v-model="formData.pid" placeholder="请输入部门编码" style="width:100%;">
          <el-option v-for="item in departs" :key="item.departId" :label="item.departName" :value="item.departId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="部门描述">
        <el-input type="textarea" v-model="formData.departDesc" placeholder="请输入部门描述" auto-complete="off"></el-input>
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
        props: ["showEditDialog", "editData", 'editType'],
        data() {
            return {
                api: 'depart',
                formData: {
                    departId: null,
                    departName: null,
                    departNo:null,
                    pid: null,
                    departDesc: null
                },
                rules: {
                    departName: [{required: true, validator: this.hasValue, trigger: "blur"}],
                    departNo: [{required: true, message: "菜单地址不能为空", trigger: "blur"}],
                    pid: [{required: true, message: "上级菜单不能为空", trigger: "blur"}]
                },
                // 部门树
                departs:[]
            }
        },
        // 通用属性和方法
        mixins: [mixinEdit],
        created(){
            this.init();
        },
        methods:{
            init(){

            }
        }
    }
</script>