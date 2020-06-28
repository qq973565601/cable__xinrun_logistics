<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="计划类型">
              <a-select v-model="queryParam.planType" placeholder="请选择计划类型">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
                <a-select-option value="电缆2">电缆2</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="备品">备品</a-select-option>
                <a-select-option value="临措">临措</a-select-option>
                <a-select-option value="正常">正常</a-select-option>
                <a-select-option value="抢修">抢修</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料编号">
              <a-input placeholder="请输入物料编号" v-model="queryParam.serial"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="物料名称">
              <a-input placeholder="请输入物料名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="toggleSearchStatus">

            <a-col :md="4" :sm="7">
              <a-form-item label="项目编号">
                <a-input placeholder="请输入项目编号" v-model="queryParam.projectNo"></a-input>
              </a-form-item>
            </a-col>

            <a-col :md="4" :sm="7">
              <a-form-item label="供用商名">
                <a-input placeholder="请输入供用商名" v-model="queryParam.supplier"></a-input>
              </a-form-item>
            </a-col>

          </template>

          <a-col :md="7" :sm="10">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>
      <a-table
        ref="table"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

                <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt=""
               style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="uploadFile(text)">
            下载
          </a-button>
        </template>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">查看库位</a>
        </span>
      </a-table>
    </div>

    <out-put-warehouse-modal ref="OutPutWarehouseList"></out-put-warehouse-modal>

  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OutPutWarehouseModal from './modules/OutPutWarehouseModal'
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import JDate from '@/components/jeecg/JDate'

  export default {
    name: 'OutPutWarehouseList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      OutPutWarehouseModal,
      JEllipsis,
      JDate
    },
    data() {
      return {
        description: '出车入库页面',
        // 查询条件
        queryParam: {},
        columns: [
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '项目编号',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '物料编号',
            align: 'center',
            dataIndex: 'serial',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '物料名称',
            align: 'center',
            dataIndex: 'name',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'unit_dictText'
          },
          {
            title: '供用商名称',
            align: 'center',
            dataIndex: 'supplier',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '计划入库数',
            align: 'center',
            dataIndex: 'jihuaruk'
          },
          {
            title: '实际入库数量',
            align: 'center',
            dataIndex: 'deliverNum'
          },
          {
            title: '计划出库',
            align: 'center',
            dataIndex: 'jihuaruk'
          },
          {
            title: '实际出库',
            align: 'center',
            dataIndex: 'receivingNum'
          },
          {
            title: '当前库存（中间库）',
            align: 'center',
            dataIndex: 'KCnums'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            // fixed:"right",
            width: 147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        confirmLoading: false,
        url: {
          list: '/cable/testdata/getOutPutWarehouseList',
          delete: '/cable/warehouse/delete',
          deleteBatch: '/cable/warehouse/deleteBatch',
          exportXlsUrl: '/cable/warehouse/exportXls',
          importExcelUrl: 'cable/warehouse/importExcel'
        },
        dictOptions: {}
      }
    },
    methods: {
      handleEdit(record) {
        this.$refs.OutPutWarehouseList.outshwo(record)
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>