<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="12">
          <a-col :md="4" :sm="7">
            <a-form-item label="计划类型">
              <a-select
                v-model="queryParam.planType"
                placeholder="请选择计划类型">
                <a-select-option value="配变电">配变电</a-select-option>
                <a-select-option value="线路">线路</a-select-option>
                <a-select-option value="电缆">电缆</a-select-option>
                <a-select-option value="其他">其他</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>

          <!--<a-col :md="4" :sm="7">
            <a-form-item label="交接单号">
              <a-input placeholder="请输入交接单号" v-model="queryParam.itemSlip"></a-input>
            </a-form-item>
          </a-col>-->

          <a-col :md="4" :sm="7">
            <a-form-item label="资产编号">
              <a-input placeholder="请输入资产编号" v-model="queryParam.assetNo"></a-input>
            </a-form-item>
          </a-col>

          <template v-if="true">
<!--          <template v-if="toggleSearchStatus">-->

            <a-col :md="4" :sm="7">
              <a-form-item label="项目编号">
                <a-input placeholder="请输入项目编号" v-model="queryParam.projectNo"></a-input>
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

          </template>
        </a-row>
        <a-row :gutter="12">
          <a-col :md="7" :sm="10">
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                <a-button @click="handleAdd" type="primary"
                          style="margin-left: 8px;" icon="plus">新建</a-button>
                <a-button type="primary" icon="import" @click="showImportModal" style="margin-left: 8px">导入</a-button>
                <a-modal
                  v-model="visible"
                  :width=400
                  on-ok="handleCancel" style="margin-top: 150px">
                  <!-- 自定义内容-begin -->
                  <a-form
                    :form="form"
                    :label-col="{ span: 3 }"
                    :wrapper-col="{ span: 12 }">

                    <a-form-item label="导入类型" style="margin-left: 10px;width: 500px;margin-left: 30px;margin-top: 20px">
                      <a-select v-model="planType" placeholder="选择导入类型" style="width: 200px;margin-left: 5px">
                        <a-select-option value="配变电">配变电</a-select-option>
                        <a-select-option value="线路">线路</a-select-option>
                        <a-select-option value="电缆">电缆</a-select-option>
                        <a-select-option value="其他">其他</a-select-option>
                      </a-select>
                    </a-form-item>

                    <a-form-item label="选择文件" style="margin-left: 10px;width: 500px;margin-left: 30px">
                      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader"
                                :action="importExcelUrl" @change="handleImportExcel" style="margin-left: 5px">
                        <a-button>选择文件</a-button>
                      </a-upload>
                    </a-form-item>

                  </a-form>
                  <!-- 自定义内容-END -->
                  <!-- 自定义页脚-begin -->
                  <template slot="footer">
                    <a-button @click="handleCancel">关闭</a-button>
                  </template>
                  <!-- 自定义页脚-END -->
                </a-modal>
                <a-button style="margin-left: 8px" type="primary" icon="download"
                          @click="showExportPlan1Modal">导出</a-button>
                <a-button @click="TheSameDay" icon="search" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">今日派单</a-button>
                <a-button @click="mergePlan" icon="login" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">合并派单</a-button>
                <a-button @click="completePlan" icon="check-circle" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">合并完单</a-button>
                <a-button @click="completePlan" icon="check-circle" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">派单记录</a-button>
                <a-button @click="completePlan" icon="check-circle" type="primary"
                          style="margin-left: 8px;background-color: darkturquoise;border: darkturquoise">完单记录</a-button>
                <a-modal
                  v-model="plan1ExportModal_visible"
                  :width=600 style="margin-top: 150px">
                  <!-- 自定义内容-begin -->
                  <a-form
                    :form="plan1Exportform"
                    :label-col="{ span: 5 }"
                    :wrapper-col="{ span: 12 }">
                    <a-form-item label="导出反馈说明" style="margin-top: 20px">
                      <a-input v-model="queryParam.explain" placeholder="请输入反馈说明" style="width: 370px"></a-input>
                    </a-form-item>
                  </a-form>
                  <!-- 自定义内容-END -->
                  <!-- 自定义页脚-begin -->
                  <template slot="footer">
                    <a-button type="primary" @click="handleExportXls('计划表1')">导出反馈excel</a-button>
                    <a-button @click="handleCancelExportPlan1Modal">关闭</a-button>
                  </template>
                  <!-- 自定义页脚-END -->
                </a-modal>
                <!--<a @click="handleToggleSearch" style="margin-left: 8px">
                    {{ toggleSearchStatus ? '收起' : '展开' }}
                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
                </a>-->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <!--    <div class="table-operator">-->
    <!--      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
    <!--      <a-button type="primary" icon="download" @click="handleExportXls('计划表1')">导出</a-button>-->
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
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{
        selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        rowKey="id"
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
          <a @click="handleEdit(record)">详情</a>
          <a-divider type="vertical"/>
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical"/>
          <a @click="assigns(record)">派单</a>
          <a-divider type="vertical"/>
          <a @click="accomplish(record)">完单</a>
          <a-divider type="vertical"/>
          <a @click="plan1Status(record)">计划状态</a>
        </span>

      </a-table>
    </div>

    <plan1-modal ref="modalForm" @ok="modalFormOk"></plan1-modal>
    <planAccomplish-modal ref="planAccomplishModal"></planAccomplish-modal>   <!-- 完单 modal -->
    <planSendOrders-modal ref="planSendOrdersModal" @ok="modasFormOk"></planSendOrders-modal> <!-- 派单 modal -->
    <plan-send-orders-the-same-day-modal ref="planSendOrdersTheSameDayModal"></plan-send-orders-the-same-day-modal>
    <plan-complete-state-modal ref="planCompleteStateModal" @ok="modasFormOk"></plan-complete-state-modal>
    <merge-plan ref="MergePlan" @ok="mergePlan"></merge-plan>
    <complete-plan ref="CompletePlan" @ok="CompletePlan"></complete-plan>
    <complete-plan1-model ref="CompletePlan1Model" @ok="CompletePlan"></complete-plan1-model>
    <merge-plan-model-plan1 ref="MergePlanModelPlan1" @ok="mergePlan"></merge-plan-model-plan1>
  </a-card>
</template>

<script>

  import JEllipsis from '../../components/jeecg/JEllipsis'
  import '@/assets/less/TableExpand.less'
  import {mixinDevice} from '@/utils/mixin'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'
  import Plan1Modal from './modules/Plan1Modal'
  import JInput from '@/components/jeecg/JInput'
  import {httpAction, getAction} from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import PlanSendOrdersModal from './modules/PlanSendOrdersModal'
  import PlanAccomplishModal from './modules/PlanAccomplishModal'
  import PlanSendOrdersTheSameDayModal from './modules/PlanSendOrdersTheSameDayModal'
  import {downFile, postAction} from '../../api/manage'
  import PlanCompleteStateModal from './modules/PlanCompleteStateModal'
  import MergePlan from "./modules/MergePlanModel";
  import CompletePlan from "./modules/CompletePlanModel";
  import MergePlanModelPlan1 from "./modules/MergePlanModelPlan1";
  import CompletePlan1Model from "./modules/CompletePlan1Model";


  export default {
    name: 'Plan1List',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      CompletePlan1Model,
      MergePlanModelPlan1,
      CompletePlan,
      MergePlan,
      Plan1Modal,
      JInput,
      JDate,
      PlanSendOrdersModal,
      PlanAccomplishModal,
      JEllipsis,
      PlanSendOrdersTheSameDayModal,
      PlanCompleteStateModal
    },
    data() {
      return {
        description: '计划表1管理页面',
        form: this.$form.createForm(this),
        width: 800,
        loading: false,
        visible: false,
        plan1ExportModal_visible: false,
        plan1ExportModal_width: 800,
        plan1Exportform: this.$form.createForm(this),
        confirmLoading: false,
        endOpen: false,
        // 导入excel类型
        planType: '配变电',
        //查询条件
        queryParam: {},
        // 表头
        columns: [
          {
            title: '计划类型',
            align: 'center',
            dataIndex: 'planType',
            scopedSlots: {customRender: 'factoryText'}
          },
          // {
          //   title: '交接单号',
          //   align: 'center',
          //   dataIndex: 'itemSlip',
          //   scopedSlots: {customRender: 'factoryText'}
          // },
          {
            title: '资产编号',
            align: 'center',
            dataIndex: 'assetNo',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '项目名称',
            align: 'center',
            dataIndex: 'projectName',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '项目编码',
            align: 'center',
            dataIndex: 'projectNo',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '废旧物料描述',
            align: 'center',
            dataIndex: 'wasteMaterialText',
            scopedSlots: {customRender: 'factoryText'}
          },
          {
            title: '计划数量',
            align: 'center',
            dataIndex: 'numReceipts',
          },
          {
            title: '入库状态',
            align: 'center',
            dataIndex: '',
            customRender: (text,record) => {
              if (record.alreadyDeliverStorage === null) return '未入库'
              else return '已入库'
            }
          },
          {
            title: '入库数量',
            align: 'center',
            dataIndex: 'alreadyDeliverStorage',
            customRender: (value, row, index) => {
              if (value === null) return 0
              else return value
            }
          },
          {
            title: '出库状态',
            align: 'center',
            dataIndex: '',
            customRender: (text,record) => {
              if (record.alreadyReceivingStorage === null) return '未出库'
              else return '已出库'
            }
          },
          {
            title: '出库数量',
            align: 'center',
            dataIndex: 'alreadyReceivingStorage',
            customRender: (value, row, index) => {
              if (value === null) return 0
              else return value
            }
          },
          {
            title: '派单状态',
            align: 'center',
            dataIndex: 'sendOrdersState',
            customRender: (value, row, index) => {
              if (value === 0) return '未派单'
              else return '已派单'
            }
          },
          {
            title: '计划状态',
            align: 'center',
            dataIndex: 'completeState_dictText'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align: 'center',
            fixed:"right",
            width: 110,
            scopedSlots: {customRender: 'action'}
          }
        ],
        url: {
          list: '/cable/plan1/list',
          delete: '/cable/plan1/delete',
          deleteBatch: '/cable/plan1/deleteBatch',
          exportXlsUrl: '/cable/plan1/exportXls',
          importExcelUrl: 'cable/plan1/importExcel'
        },
        dictOptions: {},
        plan1types: {},
        completeStates: {}
      }
    },
    computed: {},
    methods: {
      /**
       * 计划1完成状态
       */
      plan1Status(record) {
        console.log('计划1完成状态[0未完成 1已完成]', record)
        this.$refs.planCompleteStateModal.show(record, 1)
      },
      /**
       * 今日派单
       */
      TheSameDay() {
        this.$refs.planSendOrdersTheSameDayModal.theSameDays()
        this.$refs.planSendOrdersTheSameDayModal.title = ''
      },
      /* 导出 */
      handleExportXls(fileName) {
        if (!fileName || typeof fileName != 'string') {
          fileName = '导出文件'
        }
        let param = {...this.queryParam}
        if (this.selectedRowKeys && this.selectedRowKeys.length > 0) {
          param['selections'] = this.selectedRowKeys.join(',')
        }
        console.log('导出参数', param)
        downFile(this.url.exportXlsUrl, param).then((data) => {
          if (!data) {
            this.$message.warning('文件下载失败')
            this.plan1ExportModal_visible = false
            return
          }
          if (typeof window.navigator.msSaveBlob !== 'undefined') {
            window.navigator.msSaveBlob(new Blob([data], {type: 'application/vnd.ms-excel'}), fileName + '.xls')
          } else {
            let url = window.URL.createObjectURL(new Blob([data], {type: 'application/vnd.ms-excel'}))
            let link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName + '.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link) //下载完成移除元素
            window.URL.revokeObjectURL(url) //释放掉blob对象
            this.plan1ExportModal_visible = false
          }
        })
      },
      /* 弹出导出反馈说明框 */
      showExportPlan1Modal() {
        this.plan1ExportModal_visible = true
      },
      /* 隐藏导出反馈说明框 */
      handleCancelExportPlan1Modal() {
        this.plan1ExportModal_visible = false
      },
      modasFormOk() {
        this.loadData(1)
      },
      loadData(arg) {
        if (!this.url.list) {
          this.$message.error('请设置url.list属性!')
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1
        }
        var params = this.getQueryParams()//查询条件
        this.loading = true
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
          }
          if (res.code === 510) {
            this.$message.warning(res.message)
          }
          this.loading = false
        })
      },
      /* 导入前操作 */
      importExcelUrl: function () {
        if (this.planType !== '') {
          return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}/` + this.planType
        } else {
          this.$message.warning('请选择导入类型')
          this.visible = false
          return false
        }
      },
      /* 导入后操作 */
      handleImportExcel(info) {
        if (info.file.status !== 'uploading') {
          console.log(info.file, info.fileList)
        }
        if (info.file.status === 'done') {
          if (info.file.response.success) {
            // this.$message.success(`${info.file.name} 文件上传成功`);
            if (info.file.response.code === 201) {
              let {message, result: {msg, fileUrl, fileName}} = info.file.response
              let href = window._CONFIG['domianURL'] + fileUrl
              this.$warning({
                  title: message,
                  content: (
                    < div >
                    < span > {msg} < /span><br/ >
                    < span > 具体详情请 < a href = {href} target = '_blank' download = {fileName} > 点击下载 < /a> </span >
                < /div>
              )
            })
            } else {
              this.$message.success(info.file.response.message || `${info.file.name} 文件上传成功`)
              this.visible = false
            }
            this.loadData()
          } else {
            this.$message.error(`${info.file.name} ${info.file.response.message}.`)
          }
        } else if (info.file.status === 'error') {
          //this.$message.error(`文件上传失败: ${info.file.msg} `)
          this.visible = false
        }
      },
      /* 弹出导入excel选择框 */
      showImportModal() {
        this.visible = true
      },
      /* 隐藏导入excel选择框 */
      handleCancel() {
        this.visible = false
      },
      /* 加载完成状态字典数据 */
      completeStateList() {
        getAction('/sys/dictItem/selectCompleteState').then((res) => {
          if (res.success) {
            this.completeStates = res.result
          }
        })
      },
      /* 加载plan1类型字典数据 */
      plan1TypeList() {
        getAction('/sys/dictItem/selectPlan1Type').then((res) => {
          if (res.success) {
            this.plan1types = res.result
          }
        })
      },
      /**
       * 合并完单
       */
      completePlan() {
        var ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择合并派单项目!')
        //TODO 打开合并完单页面
        this.$refs.CompletePlan1Model.completePlanModelShow(ids, 1)
        this.$refs.CompletePlan1Model.title = '合并完单'
      },
      /**
       * 合并派单
       */
      mergePlan() {
        let ids = this.selectedRowKeys
        if (ids.length == 0)
          return this.$message.warning('请选择合并派单项目!')

        console.log("点击了合并派单", ids)
        //TODO 打开合并派单页面
        this.$refs.MergePlanModelPlan1.mergePlanModelShow(ids, 1)
        this.$refs.MergePlanModelPlan1.title = '合并派单'
      },
      /* 派单操作 */
      assigns(val) {
        console.log('计划1派单', val)
        this.$refs.planSendOrdersModal.dakpd(val, 1)
        this.$refs.planSendOrdersModal.title = ''
      },
      /* 完单操作 */
      accomplish(val) {
        console.log('完单')
        this.$refs.planAccomplishModal.dakwd(val, 1, null, null)
        this.$refs.planAccomplishModal.title = ''
      }
    },
    created() {
      this.plan1TypeList()
      this.completeStateList()
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
