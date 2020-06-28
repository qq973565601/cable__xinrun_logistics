<template>
  <a-card :bordered="false">

    <!-- 查询区域-begin -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="计划类型">
              <a-select v-model="queryParam.planType" placeholder="请选择计划类型">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
                <a-select-option value="电缆">电缆</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="备品">备品</a-select-option>
                <a-select-option value="正常">正常</a-select-option>
                <a-select-option value="抢修">抢修</a-select-option>
                <a-select-option value="临措">临措</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料编号">
              <a-input v-model="queryParam.serial" placeholder="请输入物料编号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料名称">
              <a-input v-model="queryParam.name" placeholder="请输入物料名称"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="项目编号">
              <a-input v-model="queryParam.projectNo"  placeholder="请输入项目编号"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="计划年份">
              <a-select v-model="queryParam.dateTime" placeholder="请选择计划年份">
                <a-select-option :value="year">{{year}}</a-select-option>
                <a-select-option :value="year-1">{{year-1}}</a-select-option>
                <a-select-option :value="year-2">{{year-2}}</a-select-option>
                <a-select-option :value="year-3">{{year-3}}</a-select-option>
                <a-select-option :value="year-4">{{year-4}}</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>

          <!--<a-dropdown v-if="selectedRowKeys.length > 0">
            <a-menu slot="overlay">
              <a-menu-item key="1" @click="batchDel">
                <a-icon type="delete"/>
                删除
              </a-menu-item>
            </a-menu>
            <a-button style="margin-left: 8px;font-weight: bold;"> 批量操作
              <a-icon type="down"/>
            </a-button>
          </a-dropdown>-->

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">
                  <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="7"/>
        </span>

      </a-table>
    </div>
    <!-- table区域-END -->

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import AFormItem from 'ant-design-vue/es/form/FormItem'
  import ACol from 'ant-design-vue/es/grid/Col'
  import JEllipsis from '../../components/jeecg/JEllipsis'

  export default {
    name: 'AnnualAccount',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      ACol,
      AFormItem,
      JEllipsis
    },
    data() {
      return {
        description: '物料年度出入台账',
        // 计划年份
        year: '',
        // 查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title:'项目编号',
            align:"center",
            dataIndex: 'projectNo',
            scopedSlots: { customRender: 'factoryText' }
          },{
            title:'项目名称',
            align:"center",
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          },{
            title:'物料编号',
            align:"center",
            dataIndex:'serial',
            scopedSlots: { customRender: 'factoryText' }
          },{
            title:'物料名称',
            align:"center",
            dataIndex:'name',
            scopedSlots: { customRender: 'factoryText' }
          },{
            title: '1月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m1_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m1_0'
              }
            ]
          },{
            title: '2月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m2_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m2_0'
              }
            ]
          },{
            title: '3月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m3_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m3_0'
              }
            ]
          },{
            title: '4月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m4_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m4_0'
              }
            ]
          },{
            title: '5月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m5_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m5_0'
              }
            ]
          },{
            title: '6月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m6_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m6_0'
              }
            ]
          },{
            title: '7月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m7_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m7_0'
              }
            ]
          },{
            title: '8月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m8_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m8_0'
              }
            ]
          },{
            title: '9月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m9_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m9_0'
              }
            ]
          },{
            title: '10月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m10_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m10_0'
              }
            ]
          },{
            title: '11月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m11_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m11_0'
              }
            ]
          },{
            title: '12月',
            align:"center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'m12_1'
              },{
                title: '出库',
                align:"center",
                dataIndex:'m12_0'
              }
            ]
          },{
            title: '合计',
            align: "center",
            children: [
              {
                title: '入库',
                align:"center",
                dataIndex:'mh_0'
              },{
                title: '出库',
                align:"center",
                dataIndex:'mh_1'
              }
            ]
          }
        ],
        url: {
          list:'/cable/material/getAnnualAccountList'
        }
      }
    },
    methods: {
      // 计划年份
      getCurrentYear() {
        let date = new Date();
        this.year = date.getFullYear();
      }
    },
    mounted() {
      // 调用计划年份
      this.getCurrentYear();
    }
  }
</script>

<style scoped>

</style>