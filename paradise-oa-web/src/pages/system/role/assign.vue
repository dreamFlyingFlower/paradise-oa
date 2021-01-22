<template>
  <div class="panel">
    <div class="panel-body" v-loading="loading" element-loading-text="拼命加载中">
      <el-row type="flex" class="row-bg">
        <el-col class="inner-padding">
          <el-tree ref="tree" node-key="menuId" :data="treeDatas" :props="props"
                   :default-expanded-keys="defaultExpandedKeys" :default-checked-keys="defaultCheckedKeys"
                   show-checkbox @check="check">
            <span class="custom-tree-node" slot-scope="{node,data}">
              <span>{{data.menuName}}</span>
              <span v-if="data.button && data.button.length > 0" style="margin-left: 20px">
                <el-checkbox v-for="item in data.button" :key="item.buttonId"
                             :value="item.isCheck === 1" @change="check(item)">{{item.buttonName}}
                </el-checkbox>
              </span>
            </span>
          </el-tree>
        </el-col>
      </el-row>
      <el-row type="flex" class="row-bg">
        <el-col class="inner-padding">
          <el-button type="primary" @click="submitFrom">确定</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
  import mixinEdit from '../../../mixin/mixinEdit'
  import apiRole from '../../../api/role'

  // 角色分配菜单
  export default {
    name: 'assign',
    props: ['showEditDialog', 'editData', 'editType'],
    data() {
      return {
        api: 'role',
        primaryKey: 'roleId',
        roleId: this.$route.params.roleId,
        loading: true,
        // 属性结构数据
        treeDatas: [],
        // 默认展开的数据
        defaultExpandedKeys: [],
        // 默认选中的数据
        defaultCheckedKeys: [],
        // 当前选中的checkedKeys
        currentCheckedNodes: [],
        // 上次选中的checkedKeys
        preCheckedNodes: [],
        params: {
          menus: [],
          halfMenus: [],
          buttons: []
        },
        props: {
          label: 'menuName',
          children: 'children'
        }
      }
    },
    mixins: [mixinEdit],
    watch: {
      currentCheckedNodes: function (newVal, oldVal) {
        this.preCheckedNodes = oldVal;
        console.log(this.currentCheckedNodes);
        console.log(this.preCheckedNodes);
        this.handlerCheckedMenu();
      }
    },
    created() {
      this.roleId && this.refresh()
    },
    methods: {
      refresh() {
        apiRole.getRoleMenus(this.roleId).then(resp => {
          this.treeDatas = resp.data;
          this.loading = false;
          this.initChecked(this.treeDatas);
          this.$nextTick(() => {
            this.currentCheckedNodes = this.$refs.tree.getCheckedNodes();
          })
        })
      },
      initChecked(treeDatas) {
        for (let item of treeDatas) {
          if (item.isCheck === 1) {
            this.defaultExpandedKeys.push(item.menuId);
            this.defaultCheckedKeys.push(item.menuId);
            if (item.button && item.button.length > 0) {
              for (let btn of item.button) {
                this.params.buttons.push(btn.buttonId);
              }
            }
          }
          if (item.children) {
            this.initChecked(item.children);
          }
        }
      },
      /**
       * el-tree的check-change方法无效
       * 当tree为折叠状态时,不会对折叠状态的树节点进行监听,只有当所有节点都是展开状态时,
       * 该方法的第1和第2参数才有效,任何情况下第3参数都无效
       * @param data object 发生状态改变的节点数据
       * @param self boolean 节点本身是否被选中
       * @param children boolean节点的子节点是否有被选中的项
       */
      checkChange(data, self, children) {
        if (data.buttonId) {
          // 点击按钮选项
          data.checked = data.checked === 1 ? 0 : 1;
          if (data.checked) {
            this.params.buttons.push(data.buttonId)
          } else {
            let index = this.params.buttons.findIndex(
                item => item === data.buttonId
            );
            if (index !== -1) {
              this.params.buttons.splice(index, 1)
            }
          }
        } else {
          // 当点击菜单的时候
          let buttons = data.button;
          if (!buttons || data.button.length === 0) {
            return
          }
          for (let button of buttons) {
            button.isCheck = self ? 1 : 0;
            if (self) {
              this.params.buttons.push(button.buttonId)
            } else {
              let index = this.params.buttons.findIndex(
                  item => button.buttonId === item
              );
              if (index !== -1) {
                this.params.buttons.splice(index, 1)
              }
            }
          }
        }
      },
      check(data, checkDatas) {
        if (data.buttonId) {
          // 点击按钮选项
          data.isCheck = data.isCheck === 1 ? 0 : 1;
          if (data.isCheck) {
            this.params.buttons.push(data.buttonId)
          } else {
            let index = this.params.buttons.findIndex(
                item => item === data.buttonId
            );
            if (index !== -1) {
              this.params.buttons.splice(index, 1)
            }
          }
        } else {
          // 当点击菜单的时候
          this.currentCheckedNodes = checkDatas.checkedNodes
        }
      },
      // 当菜单选项有变化时调用此方法
      handlerCheckedMenu() {
        // 删除上次选中,本次没有选中的菜单中下的按钮
        if (this.$isArray(this.preCheckedNodes) && this.preCheckedNodes.length > 0) {
          for (let preCheckedNode of this.preCheckedNodes) {
            if (preCheckedNode.btnNum === 0) {
              continue
            }
            let index = this.currentCheckedNodes.findIndex(
                currentCheckedNode =>
                    currentCheckedNode.menuId === preCheckedNode.menuId
            );
            if (index === -1) {
              this.handlerButton(preCheckedNode, 0)
            }
          }
        }
        // 增加本次选中,上次没有选中的按钮
        if (this.$isArray(this.currentCheckedNodes) && this.currentCheckedNodes.length > 0) {
          for (let currentCheckedNode of this.currentCheckedNodes) {
            if (currentCheckedNode.btnNum === 0) {
              continue
            }
            this.handlerButton(currentCheckedNode, 1)
          }
        }
      },
      /**
       * 删除或选中菜单下的按钮
       * @param checkedNode 未选中的菜单节点
       * @param isCheck 是否选中按钮,0否1是
       */
      handlerButton(checkedNode, isCheck) {
        let buttons = checkedNode.button
        for (let button of buttons) {
          button.isCheck = isCheck;
          let index = this.params.buttons.findIndex(
              item => button.buttonId === item
          );
          if (index !== -1 && !isCheck) {
            this.params.buttons.splice(index, 1)
          } else if (index === -1 && isCheck) {
            this.params.buttons.push(button.buttonId)
          }
        }
      },
      submitFrom() {
        this.params.menus = this.$refs.tree.getCheckedKeys(true);
        this.params.halfMenus = this.$refs.tree.getHalfCheckedKeys();
        console.log(this.params)
        return;
        this.$nextTick(function () {
          apiRole.savePermissions(this.roleId, this.params).then(() => {
            this.$message.success('修改成功')
            // this.$router.back();
            // location.reload();
          })
        })
      }
    }
  }
</script>

<style scoped>
</style>
