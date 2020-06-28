<template>
  <div class="indexclass">
    <div class="indextitle">
      欢迎进入昕润物流管理系统首页
    </div>
  </div>
<!--  <div>-->
<!--    &lt;!&ndash; 主页内容-begin &ndash;&gt;-->
<!--    <index-chart v-if="indexStyle==1"></index-chart>-->
<!--    <index-bdc v-if="indexStyle==2"></index-bdc>-->
<!--    <index-task v-if="indexStyle==3"></index-task>-->
<!--    <div style="width: 100%;text-align: right;margin-top: 20px">-->
<!--      请选择首页样式：-->
<!--      <a-radio-group v-model="indexStyle">-->
<!--        <a-radio :value="1">统计图表</a-radio>-->
<!--        <a-radio :value="2">统计图表2</a-radio>-->
<!--        <a-radio :value="3">任务表格</a-radio>-->
<!--      </a-radio-group>-->
<!--    </div>-->
<!--    &lt;!&ndash; 主页内容-END &ndash;&gt;-->
<!--  </div>-->
</template>

<script>
  import IndexChart from './IndexChart'
  import IndexTask from "./IndexTask"
  import IndexBdc from './IndexBdc'

  export default {
    name: "Analysis",
    components: {
      IndexChart,
      IndexTask,
      IndexBdc
    },
    data() {
      return {
        indexStyle:1
      }
    },
    created() {

    },
    methods: {

    }
  }
</script>
<style>
  .indexclass{
    background: #f0f2f5 url(~@/assets/xrwlbjpt.jpg) no-repeat 50%;
    background-size: 100%;
  }
  .indextitle{
    width: 100%;
    text-align: center;
    font-size: 30px;
    color: #777777;
    padding-bottom: 24.5%;
    padding-top: 20%;
  }
</style>