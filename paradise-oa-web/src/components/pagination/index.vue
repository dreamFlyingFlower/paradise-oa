<template>
  <div class="pagination-container">
    <el-pagination
            :background="true"
            :current-page="$store.getters.pageIndex"
            layout="total, sizes, prev, pager, next, jumper"
            :page-sizes="[10,20,30,50]"
            page-size.sync="10"
            :total="$store.getters.total"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange">
    </el-pagination>
  </div>
</template>

<script>

  export default {
    name: 'pagination',
    methods: {
      handleSizeChange(val) {
        this.$store.commit('PAGE_SIZE', val);
      },
      handleCurrentChange(val) {
        this.$store.commit('PAGE_INDEX', val);
      }
    }
  }
</script>

<style scoped>
  .pagination-container {
    background: #fff;
    text-align: right;
    margin-bottom: 15px;
  }
</style>