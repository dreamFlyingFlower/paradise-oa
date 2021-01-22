import {mapGetters} from 'vuex'

/**
 * 该js只适用于edit页面,且页面中定义了formData,editData,editType.不可用做全局通用,和mixin有冲突.
 * 若都有craeted钩子,则先执行本文件中的created
 * 使用方法:在子组件中mixins:[mixin]
 */
export default {
  data() {
    return {
      // 弹框的title属性
      title: "编辑",
      // 表明该组件通用的api前缀,父组件必须重写
      api: null,
      // 需要新增或修改的对象,父组件需要自定义其中的属性.此处定义是为了保证本组件在初始化时不报错
      formData: {}
    }
  },
  watch: {},
  created() {
    this.handlerTitle();
    this.validate();
    this.createFormData();
  },
  methods: {
    /**
     * 根据情况修改弹框的标题
     */
    handlerTitle() {
      if (this.editType === 1) {
        this.title = "新增";
      } else if (this.editType === 2) {
        this.title = "编辑";
      }
    },
    /**
     * 检验父组件所必须的api属性
     */
    validate() {
      if (!this.api) {
        throw new Error("上级组件的api属性不可为空");
      }
    },
    /**
     * 当为修改时,将需要修改的数据复制到formData中
     * editType和editData不可写在data中,edit.vue页面中的这2个参数是从index.vue传过来,
     * 若是在data中定义了相同字段,则将不会使用父组件传过来的数据.
     * 或者是本组件定义的属性覆盖了父组件传过来的同名属性
     */
    createFormData() {
      if (this.editType === 2) {
        for (let key in this.editData) {
          this.formData[key] = this.editData[key];
        }
      }
    },
    /**
     * 检验所填字段不为空,且名称不可重复.父组件必须定义api
     * 该方法必须配合elementui的form表单的rules使用,否则无效
     * @param rule 该参数由elementui添加,包含了需要检查的prop属性中的值,
     *        该值必须和formData中的字段名一样,否则将会报错
     * @param value 该参数由elementui添加,当前表单中响应字段的值
     * @param callback 该参数由elementui添加,回调函数
     */
    hasValue(rule, value, callback) {
      if (!value || value.trim().length === 0) {
        callback(new Error("名称不能为空"));
      }
      if (2 === this.editType) {
        if (value === this.editData[rule.field]) {
          callback();
          return;
        }
      }
      this.$hasValue(this.api, {[rule.field]: value}).then(resp => {
        if (resp.data === 1) {
          callback(new Error("该名称重复,请修改"));
        } else {
          callback();
        }
      });
    },
    /**
     *  确定按钮,新增或修改.
     *  $refs后的检验属性必须同edit.vue中表单的ref指向相同.本方法可在edit.vue中重写
     *  该方法中由于异步调用了ajax,close方法应该放在调用成功后的回调函数中
     *  若无其他严格要求,可不改写.
     *  该方法没有对重复提交做处理,需要自定义
     *  @param validCreate 自定义新增时校验方法
     *  @param validEdit 自定义修改时校验方法
     */
    handlerData(validCreate, validEdit) {
      this.$refs['formData'].validate(valid => {
        if (!valid) {
          this.$message("校验失败,请检查参数");
          return false;
        } else {
          if (this.editType === 1) {
            if (validCreate && this.$isFunc(validCreate)) {
              let validFlag = validCreate(this.formData);
              if (validFlag) {
                this.$create(this.api, this.formData);
                this.close();
              } else {
                this.$message("校验失败");
              }
            } else {
              this.$create(this.api, this.formData);
              this.close();
            }
          } else if (this.editType === 2) {
            if (validEdit && this.$isFunc(validEdit)) {
              let validFlag = validEdit(this.formData);
              if (validFlag) {
                this.$edit(this.api, this.formData);
                this.close();
              } else {
                this.$message("校验失败");
              }
            } else {
              this.$edit(this.api, this.formData);
              this.close();
            }
          }
        }
      });
    },
    // 点击弹出框的X按钮事件,应该有更好的办法,没找到
    beforeClose() {
      this.closeDialog(false);
    },
    // 关闭弹框
    closeDialog(flag) {
      this.close(flag);
    },
    close(fresh, freshTree) {
      this.$emit('closeDialog', fresh, freshTree);
    }
  }
};
