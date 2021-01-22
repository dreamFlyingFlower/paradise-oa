<template>
  <el-dialog :title="title" :visible.sync="showEditDialog" width="30%" :before-close="beforeClose">
    <el-form ref="formData" :model="formData" :rules="rules" label-width="100px" class="demo-ruleForm">
      <el-form-item label="菜单名称" prop="menuName">
        <el-input v-model="formData.menuName" auto-complete="off" placeholder="请输入菜单名"></el-input>
      </el-form-item>
      <el-form-item label="上级菜单" prop="pid">
        <el-select v-model="formData.pid" placeholder="请选择上级菜单" style="width: 100%;">
          <el-option v-for="item in menus" :key="item.menuId" :label="item.menuName" :value="item.menuId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="菜单地址" prop="menuPath">
        <el-input v-model="formData.menuPath" auto-complete="off" placeholder="请输入菜单地址"></el-input>
      </el-form-item>
      <el-form-item label="菜单视图" prop="menuView">
        <el-input v-model="formData.menuView" auto-complete="off" placeholder="请输入菜单视图"></el-input>
      </el-form-item>
      <el-form-item label="是否隐藏" prop="hidden">
        <el-select v-model="formData.hidden" placeholder="请选择菜单是否隐藏" style="width: 100%;">
          <el-option v-for="item in hiddens" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="菜单路由名">
        <el-input v-model="formData.routerName" auto-complete="off" placeholder="请输入菜单视图"></el-input>
      </el-form-item>
      <el-form-item label="菜单图标">
        <el-input v-model="formData.menuIcon" auto-complete="off" placeholder="请输入菜单图标"></el-input>
      </el-form-item>
      <el-form-item label="国际化名称">
        <el-input v-model="formData.menuI18n" auto-complete="off" placeholder="请输入国际化名称"></el-input>
      </el-form-item>
      <el-form-item label="跳转地址">
        <el-input v-model="formData.redirect" auto-complete="off" placeholder="请输入跳转地址"></el-input>
      </el-form-item>
      <el-form-item label="排序">
        <el-input v-model="formData.sort" auto-complete="off" placeholder="请输入排序"></el-input>
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
  import {getSelfChildren} from "../../../api/menu";

  export default {
    name: "edit",
    props: ["showEditDialog", "editData", 'editType'],
    data() {
      return {
        api: 'menu',
        formData: {
          menuId: null,
          menuName: null,
          pid: null,
          menuPath: null,
          menuView: null,
          hidden:1,
          routerName: null,
          menuIcon: null,
          menuI18n: null,
          redirect: null,
          sort: null
        },
        rules: {
          menuName: [{required: true, validator: this.hasValue}],
          pid: [{required: true, message: "上级菜单不能为空"}],
          menuPath: [{required: true, message: "菜单地址不能为空"}],
          menuView: [{required: true, message: "菜单视图地址不能为空"}],
          hidden:[{required:true,message:"菜单是否隐藏不能为空"}]
        },
        // 菜单树
        menus: [],
        // 菜单是否隐藏
        hiddens:[{label:"隐藏",value:0},{label:"显示",value:1}]
      }
    },
    // 通用属性和方法
    mixins: [mixinEdit],
    created() {
      this.init();
    },
    methods: {
      init() {
        this.getMenus();
      },
      getMenus() {
        getSelfChildren(1).then(resp => {
          this.menus = resp.data;
          if(this.menus && this.menus.length > 0 && this.editType === 1){
            this.formData.pid = this.menus[0].menuId;
          }
        })
      }
    }
  }
</script>