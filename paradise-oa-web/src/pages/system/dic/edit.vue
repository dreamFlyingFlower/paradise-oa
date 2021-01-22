<template>
  <el-dialog :title="title" :visible.sync="showEditDialog" width="30%" :before-close="beforeClose">
    <el-form ref="formData" :model="formData" :rules="rules" class="demo-ruleForm" label-width="100px">
      <el-form-item label="字典名称" prop="dicName">
        <el-input v-model="formData.dicName" auto-complete="off" placeholder="请输入字典名称"></el-input>
      </el-form-item>
      <el-form-item label="上级字典" prop="pid">
        <el-select v-model="formData.pid" auto-complete="off" placeholder="请选择上级字典" style="width:100%">
          <el-option v-for="item in dics" :key="item.dicId" :label="item.dicName" :value="item.dicId"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="字典编码" prop="dicCode">
        <el-input v-model="formData.dicCode" auto-complete="off" placeholder="请出入字典编码"></el-input>
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
    import apiDic from "../../../api/dic";

    export default {
        name: "edit",
        props: ["showEditDialog", "editData", 'editType','dics','treeData'],
        data() {
            return {
                api: 'dic',
                formData: {
                    dicId: null,
                    dicName: null,
                    dicCode:null,
                    pid: null
                },
                rules: {
                    dicName: [{required: true, validator: this.hasValue, trigger: "blur"}],
                    pid: [{required: true, message: "上级字典不能为空", trigger: "change"}],
                    dicCode: [{required: true, validator: this.hasValue, trigger: "blur"}]
                }
            }
        },
        // 通用属性和方法
        mixins: [mixinEdit],
        created() {
            this.init();
        },
        methods: {
            init() {
                this.initDics();
            },
            // 初始化下拉框
            initDics(){
                if(this.dics && this.dics.length > 0){
                    if(this.editType === 1){
                        this.formData.pid = this.treeData.treeId;
                    }
                }
            }
        }
    }
</script>
