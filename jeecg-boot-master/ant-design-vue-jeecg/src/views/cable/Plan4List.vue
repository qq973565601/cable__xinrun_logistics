<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">

          <a-col :md="4" :sm="7">
            <a-form-item label="工程账号">
              <a-input placeholder="请输入工程账号" v-model="queryParam.projectNo"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="工程名称">
              <a-input placeholder="请输入工程名称" v-model="queryParam.engName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="电缆名称">
              <a-input placeholder="请输入电缆名称" v-model="queryParam.cableName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="7">
            <a-form-item label="完成状态">
              <a-select v-model="queryParam.completeState" placeholder="请选择完成状态">
                <template v-for="(types,index) in completeStates">
                  <a-select-option v-bind:value="types.itemValue">{{types.itemText}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="9" :sm="10" >
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" style="margin-left: 8px;background-color: #00DB00;border: #00DB00" icon="plus">新建</a-button>
              <a-upload name="file" style="margin-left: 8px" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
                <a-button type="primary" icon="import">导入</a-button>
              </a-upload>
              <a-button style="margin-left: 8px" type="primary" icon="download" @click="showExportPlan4Modal">导出</a-button>
              <a-modal
                v-model="plan4ExportModal_visible"
                :width=600 style="margin-top: 150px">
                <!-- 自定义内容-begin -->
                <a-form
                  :form="plan4Exportform"
                  :label-col="{ span: 5 }"
                  :wrapper-col="{ span: 12 }">
                  <a-form-item label="导出反馈说明"  style="margin-top: 20px">
                    <a-input v-model="queryParam.explain" placeholder="请输入反馈说明" style="width: 370px"></a-input>
                  </a-form-item>
                </a-form>
                <!-- 自定义内容-END -->
                <!-- 自定义页脚-begin -->
                <template slot="footer">
                  <a-button type="primary" @click="handleExportXls('计划表4')">导出反馈excel</a-button>
                  <a-button @click="handleCancelExportPlan4Modal">关闭</a-button>
                </template>
                <!-- 自定义页脚-END -->
              </a-modal>
              <a-button style="margin-left: 8px" type="primary" icon="import" @click="showExportModal">导出反馈汇总</a-button>
              <a-button @click="TheSameDay" icon="search" type="primary" style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">今日派单</a-button>
              <a-modal
                v-model="visible"
                :width=700
                on-ok="handleCancel" style="margin-top: 150px">
                <!-- 自定义内容-begin -->
                <a-form style="margin-top: 20px">

                  <a-form-item label="退役日期" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 400px;margin-right: -140px">
                    <j-date placeholder="输入退役日期" v-model="queryParam.decommissioningDate" ></j-date>
                  </a-form-item>

                  <a-form-item label="交接单号" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 400px;margin-right: -50px">
                    <a-input placeholder="输入交接单号" v-model="queryParam.itemSlip"></a-input>
                  </a-form-item>

                  <a-form-item label="情况说明" :labelCol="labelCol" :wrapperCol="wrapperCol" style="width: 795px;margin-left: -83px">
                    <a-input placeholder="输入情况说明" v-model="queryParam.description"></a-input>
                  </a-form-item>

                  <a-form-item label="备注" :labelCol="labelCol" :wrapperCol="wrapperCol"style="width: 795px;margin-left: -83px">
                    <a-input placeholder="输入备注" v-model="queryParam.remark"></a-input>
                  </a-form-item>

                </a-form>
                <!-- 自定义内容-END -->

                <!-- 自定义页脚-begin -->
                <template slot="footer">
                  <a-button type="primary" @click="handleExportXls2('计划表4汇总')">导入反馈汇总</a-button>
                  <a-button @click="handleCancel">关闭</a-button>
                </template>
                <!-- 自定义页脚-END -->
              </a-modal>
            </span>
          </a-col>

        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
<!--    <div class="table-operator">-->
<!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
<!--      <a-button type="primary" icon="download" @click="handleExportXls('计划表4')">导出</a-button>-->
<!--      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">-->
<!--        <a-button type="primary" icon="import">导入</a-button>-->
<!--      </a-upload>-->
<!--      <a-dropdown v-if="selectedRowKeys.length > 0">-->
<!--        <a-menu slot="overlay">-->
<!--          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
<!--        </a-menu>-->
<!--        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>-->
<!--      </a-dropdown>-->
<!--    </div>-->

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
          <j-ellipsis :value="text" :length="4"/>
        </span>
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
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
          <a @click="handleEdit(record)">详情</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a @click="assigns(record)">派单</a>
          <a-divider type="vertical" />
          <a @click="accomplish(record)">完单</a>
        </span>

      </a-table>
    </div>

    <plan4-modal ref="modalForm" @ok="modalFormOk"></plan4-modal>
    <planAccomplish-modal ref="planAccomplishModal"></planAccomplish-modal>
    <planSendOrders-modal ref="planSendOrdersModal"></planSendOrders-modal>
    <plan-send-orders-the-same-day-modal ref="planSendOrdersTheSameDayModal"></plan-send-orders-the-same-day-modal>
  </a-card>
</template>

<script>
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import Plan4Modal from './modules/Plan4Modal'
  import JInput from '@/components/jeecg/JInput'
  import PlanAccomplishModal from './modules/PlanAccomplishModal'
  import PlanSendOrdersModal from './modules/PlanSendOrdersModal'
  import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import JEllipsis from '../../components/jeecg/JEllipsis'
  import PlanSendOrdersTheSameDayModal from './modules/PlanSendOrdersTheSameDayModal'

  export default {
    name: "Plan4List",
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      Plan4Modal,
      JInput,
      PlanSendOrdersModal,
      PlanAccomplishModal,
      JDate,
      JEllipsis,
      PlanSendOrdersTheSameDayModal
    },
    data () {
      return {
        description: '计划表4管理页面',
        width: 500,
        loading: false,
        visible: false,
        plan4ExportModal_visible: false,
        plan4ExportModal_width: 800,
        plan4Exportform: this.$form.createForm(this),
        confirmLoading: false,
        endOpen: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 }
        },
        //保存查询条件
        queryParam: { },
        // 表头
        columns: [
          {
            title:'工程账号',
            align:"center",
            dataIndex: 'projectNo',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'工程名称',
            align:"center",
            dataIndex: 'engName',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'电缆名称',
            align:"center",
            dataIndex: 'cableName',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'电压等级',
            align:"center",
            dataIndex: 'voltageGrade',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'电缆截面',
            align:"center",
            dataIndex: 'cableCross',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'预计抽取长度',
            align:"center",
            dataIndex: 'samplingLength',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'抽取日期',
            align:"center",
            dataIndex: 'samplingDate',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'抽取地点',
            align:"center",
            dataIndex: 'samplingAddres',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'施工班组',
            align:"center",
            dataIndex: 'construc',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'班组联系人及电话',
            align:"center",
            dataIndex: 'construcContact',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'施工队伍',
            align:"center",
            dataIndex: 'team',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'队伍联系人及电话',
            align:"center",
            dataIndex: 'teamContact',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title:'派单状态',
            align:"center",
            dataIndex: 'sendOrdersState',
            customRender: (value, row, index) => {
              var s = ''
              if(value===0){
                s='未派单'
              }else if(value===1){
                s='已派单'
              }
              return s
            }
          },
          {
            title:'完成状态',
            align:"center",
            dataIndex: 'completeState_dictText',
            scopedSlots: { customRender: 'factoryText' },
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            // fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/cable/plan4/list",
          delete: "/cable/plan4/delete",
          deleteBatch: "/cable/plan4/deleteBatch",
          exportXlsUrl: "/cable/plan4/exportXls",
          exportXlsUrl2: '/cable/plan4/exportXls2',
          importExcelUrl: "cable/plan4/importExcel",
        },
        dictOptions:{},
        completeStates:{},
      }
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      TheSameDay(){
        this.$refs.planSendOrdersTheSameDayModal.theSameDays()
        this.$refs.planSendOrdersTheSameDayModal.title = ''
      },
      showExportPlan4Modal() {
        this.plan4ExportModal_visible = true
      },
      handleCancelExportPlan4Modal() {
        this.plan4ExportModal_visible = false
      },
      // 显示导出反馈汇总弹出框
      showExportModal() {
        this.visible = true
      },
      // 隐藏导出反馈汇总弹出框
      handleCancel() {
        this.visible = false
      },
      // 导出反馈excel
      handleExportXls(fileName) {
        if (!fileName || typeof fileName != 'string') {
          fileName = '导出文件'
        }
        let param = { ...this.queryParam }
        if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
          param['selections'] = this.selectedRowKeys.join(',')
        }
        console.log('导出参数', param)
        downFile(this.url.exportXlsUrl, param).then((data) => {
          if (!data) {
            this.$message.warning('文件下载失败')
            this.plan4ExportModal_visible = false
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
          } else {
            let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName + '.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link) //下载完成移除元素
            window.URL.revokeObjectURL(url) //释放掉blob对象
            this.plan4ExportModal_visible = false
          }
        })
      },
      // 导出反馈汇总
      handleExportXls2(fileName) {
        if (!fileName || typeof fileName != 'string') {
          fileName = '导出文件'
        }
        // 构造参数
        let param = {
          decommissioningDate: this.queryParam.decommissioningDate,
          itemSlip: this.queryParam.itemSlip,
          description: this.queryParam.description,
          remark: this.queryParam.remark
        }
        downFile(this.url.exportXlsUrl2, param).then((data) => {
          if (!data) {
            this.$message.warning('文件下载失败')
            this.visible = false;
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
          } else {
            let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName + '.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link) //下载完成移除元素
            window.URL.revokeObjectURL(url) //释放掉blob对象
            this.visible = false;
          }
        })
      },
      assigns(val){
        console.log("派单")
        this.$refs.planSendOrdersModal.dakpd(val,4);
        this.$refs.planSendOrdersModal.title = "";
      },
      accomplish(val){
        console.log("完单")
        this.$refs.planAccomplishModal.dakwd(val,4);
        this.$refs.planAccomplishModal.title = "";
      },
      completeStateList() {
        getAction("/sys/dictItem/selectCompleteState").then((res) => {
          if (res.success) {
            this.completeStates = res.result;
          }
        })
      },
    },
    created () {
      this.completeStateList();
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>