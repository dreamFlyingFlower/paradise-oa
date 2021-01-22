import {mapGetters} from 'vuex'

/**
 * 创建通用混入对象,当vue组件引入该对象时,组件将会并入混入对象的所有选项.最好不要使用全局混入,维护时容易出错
 * 当组件的data,methods,component,directive对象中有同样的属性时,组件的属性将会覆盖混入对象的属性
 * 当组件和混入对象都定义了钩子方法(created),则先执行混入对象的钩子,再执行组件的钩子
 * 使用方法:在子组件中mixins:[mixin]
 */
export default {
  data() {
    return {
      // 是否因其他原因导致pageIndex改变,默认false
      pageIndexChange: false,
      // 表格展示数据
      tableDatas: [],
      // 是否展示新增编辑图层
      showEditDialog: false,
      // 展示新增编辑图层时传到图层的数据
      editData: {},
      // 展示新增编辑图层时传到图层的类别,通用1为新增,2为修改
      editType: 1,
      // 批量删除时,后台所需的id所属对象的集合
      multipleSelection: [],
      // 通用方法中的api前缀,当前组件某些方法需要使用,子组件若要调用则必须重写该属性
      api: null,
      // 通用方法中的主键字段,当前组件某些方法需要使用,子组件若要调用则必须重写该属性
      primaryKey: null,
      // 是否存在树形结构
      existTree: false
    }
  },
  computed: {
    // 分页参数
    ...mapGetters(
        ["pageIndex", "pageSize", "total", "fresh", "freshTree"]
    )
  },
  watch: {
    'pageIndex': function (val) {
      this.$store.commit('PAGE_INDEX', val);
      // pageSize改变时会重置pageIndex,可能造成refresh方法双重调用
      if (this.pageIndexChange) {
        this.pageIndexChange = false;
        return false;
      }
      this.$nextTick(() => {
        this.refresh();
      });
    },
    /**
     * 当pageSize改变时,会将pageIndex重置为1,若是pageIndex原先本来就为1,则不会调用refresh方法
     * 若是pageSize直接调用refresh方法,同时重置了pageIndex,若pageIndex原先不为1,
     * 则会调用2次refresh方法,此处将设置一个标志
     * @param val 改变后的新值
     */
    'pageSize': function (val) {
      this.$store.commit('PAGE_SIZE', val);
      this.pageIndexChange = true;
      this.$store.commit('PAGE_INDEX', 1);
      this.$nextTick(() => {
        this.refresh();
      });
    },
    /**
     * TODO
     * 页面删除时会改变total的值,删除了一整页的时候需要重新请求
     * total的值必须在每次请求refresh方法的时候设置.此时可能会出现重复请求
     */
    'total': function (val) {
      let quotient = val / this.pageSize;
      let flag = val % this.pageSize === 0;
      if (this.pageIndex > 1) {
        if (flag && (quotient < this.pageIndex)) {
          this.$store.commit('PAGE_INDEX', this.pageIndex - 1);
        }
      }
    },
    'fresh': function () {
      // 若存在树形结构,当树需要刷新的时候,刷新树的同时会刷新当前页面的数据列表
      if (!this.existTree) {
        if (this.fresh) {
          // 刷新当前页面
          this.refresh();
          this.$store.commit("FRESH", false);
        }
      } else {
        if (this.fresh) {
          this.refresh();
          this.$store.commit("FRESH", false);
        }
        if (this.existTree && this.freshTree) {
          this.refreshTree();
          this.$store.commit("FRESH_TREE", false);
        }
      }
    },
    'freshTree': function () {
      if (this.existTree && this.freshTree) {
        // 刷新树形结构
        this.refreshTree();
        this.$store.commit("FRESH_TREE", false);
      }
    }
  },
  created() {
    // 页面跳转,重置分页参数
    this.$store.commit("PAGE_INDEX", 1);
    this.refresh();
  },
  methods: {
    /**
     * 该方法不做任何操作.只是为了子组件引用该组件时不会报错
     * 子组件必须在页面中重写该方法,该方法在子组件中的分页操作.若无分页可不重写
     */
    refresh() {
    },
    /**
     * 同refresh方法,刷新的是页面中的树形结构数据
     * 若需要在增删改等操作完成后刷新树,则子组件必须在页面中重写该方法
     */
    refreshTree() {
    },
    /**
     * 检验子组件所必须的api属性
     */
    validate() {
      if (!this.api) {
        throw new Error("上级组件的api属性不可为空");
      }
      if(!this.primaryKey){
        throw new Error("子组件所对应的api的主键字段不可为空")
      }
    },
    /**
     * 打开新增编辑弹框
     */
    openEditDialog(row, type) {
      this.showEditDialog = true;
      this.editData = row && Object.keys(row).length > 0 ? row : null;
      this.editType = type;
    },
    /**
     * 关闭弹框
     * fresh:boolean 是否刷新当前页面,true刷新,false不刷新
     * freshTree:boolean 当有树形结构时,是否刷新树形结构,true刷新,false不刷新
     */
    closeDialog(fresh, freshTree) {
      this.showEditDialog = false;
      if (fresh) {
        this.refresh();
      }
      if (freshTree) {
        this.refreshTree();
      }
    },
    //table多选条数
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 批量删除方法
    deletes() {
      if (!this.api || !this.primaryKey) {
        throw new Error("若使用该方法,引入当前组件的子组件必须重写data中的api和primaryKey属性");
      }
      let ids = [];
      for (let item of this.multipleSelection) {
        ids.push(item[this.primaryKey]);
      }
      this.$removes(this.api, ids);
    }
  }
};
