<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="close"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 30px">
    <a-spin :spinning="confirmLoading" style="margin-top: 10px">
      <a-form
        :form="form"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }">


        <div style="">
          <div style="margin-top: 10px;">
            <div style="display: inline-block;width: 100%;">
              <a-table
                ref="table"
                rowKey="id"
                size="middle"
                bordered
                :columns="columns"
                :dataSource="dataSource"
                :pagination="ipagination"
                @change="handleTableChange"
                class="j-table-force-nowrap">
                <span slot="factoryText" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="realname" slot-scope="text">
                  <j-ellipsis :value="text" :length="10"/>
                </span>
                <span slot="action" slot-scope="text, record">
                <a-popconfirm title="确定删除吗?" @confirm="() => sdhandleDelete(record)">
                  <a>删除</a>
                </a-popconfirm>
                </span>

              <span slot="receiptPhotos" slot-scope="text,record">
                <template v-if="record.receiptPhotos!=undefined&&record.receiptPhotos!=null&&record.receiptPhotos!=''">
                  <a @click="() => getUrlNewView(record.receiptPhotos)">点我查看</a>
                </template>
                <template v-else>
                  <!--         暂无-->
                </template>
              </span>

                <span slot="scenePhotos1" slot-scope="text,record">
                   <template v-if="record.scenePhotos1!=undefined&&record.scenePhotos1!=null&&record.scenePhotos1!=''">
                     <a @click="() => getUrlNewView(record.scenePhotos1)">点我查看</a>
                   </template>
                <template v-else>
<!--                  暂无-->
                </template>
                </span>
                <span slot="scenePhotos2" slot-scope="text,record">
                <template v-if="record.scenePhotos2!=undefined&&record.scenePhotos2!=null&&record.scenePhotos2!=''">
                  <a @click="() => getUrlNewView(record.scenePhotos2)">点我查看</a>
                 </template>
                <template v-else>
<!--         暂无-->
                </template>
                </span>
              </a-table>
            </div>
          </div>

        </div>

      </a-form>
    </a-spin>

    <plan-accomplish-modal ref="planAccomplishModal"></plan-accomplish-modal>
    <plan-the-same-day-de-modal ref="planTheSameDayDeModal" @ok=""></plan-the-same-day-de-modal>
    <!-- 自定义页脚 -->
    <template slot="footer">
      <a-button key="back" @click="handleCancel">
        关闭
      </a-button>
    </template>
  </a-modal>
</template>


<script>
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import pick from 'lodash.pick'
  import JDate from '@/components/jeecg/JDate'
  import { httpAction, getAction, deleteAction } from '@/api/manage'
  import PlanAccomplishModal from './PlanAccomplishModal'
  import '@/assets/less/TableExpand.less'
  import PlanTheSameDayDeModal from './PlanTheSameDayDeModal'

  export default {
    name: 'PlanSendOrdersWdModal',
    components: {
      JDate,
      JEllipsis,
      PlanAccomplishModal,
      PlanTheSameDayDeModal
    },
    data () {
      return {
        ipagination: {
          pageNo: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '6', '7', '8', '9', '10'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        warehouseLists: [],
        sendOrdersTypes: [],
        storageLocations: [],
        dataSource: [],
        columns: [
          {
            title: '派单类型',
            align: 'center',
            dataIndex: 'planType',
            width: 80
          },
          {
            title: '目标仓库',
            align: 'center',
            dataIndex: 'wname',
            width: 90
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storagename'
          },
          {
            title: '任务日期',
            align: 'center',
            dataIndex: 'taskTime'
          },
          {
            title: '完单数量',
            align: 'center',
            dataIndex: 'accomplishnum'
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'accomplishnumunit'
          },
          {
            title: '回单图片',
            align: 'center',
            dataIndex: 'receiptPhotos',
            scopedSlots: { customRender: 'receiptPhotos' }
          },
          {
            title: '是否异常',
            align: 'center',
            dataIndex: 'scenesituation',
            customRender: (value, row, index) => {
              var y=''
              if(value=='0'){
                y='正常'
              }else {
                y='异常'
              }
              return y
            }
          },
          {
              title: '异常原因',
              align: 'center',
              dataIndex: 'anomalousCause'
          },
          {
            title: '异常照片1',
            align: 'center',
            dataIndex: 'scenePhotos1',
            scopedSlots: { customRender: 'scenePhotos1' },
          },
          {
            title: '异常照片2',
            align: 'center',
            dataIndex: 'scenePhotos2',
            scopedSlots: { customRender: 'scenePhotos2' },
            width: 200
          },
          {
              title: '操作',
              dataIndex: 'action',
              width: 100,
              align: 'center',
              scopedSlots: {customRender: 'action'}
          }
        ],
        form: this.$form.createForm(this),
        title: '操作',
        width: 1300,
        visible: false,
        model: {},
        confirmLoading: false,
        url: {
          add: '/cable/sendOrders/add',
          deleteWangda: '/cable/sendOrders/delete',
          edit: '/cable/sendOrders/edit',
          wddelete: '/cable/sendOrders/wddelete'
        },
        //查询参数
        ids:'',
        paln: '',
        data: {},
        vehicles: {},
        users: {},
        serials: '',
        storageLocations2: [],
        vadjsoifjweoi: {},
        // 派单类型
        sendOrderType: false
      }
    },
    created () {
      this.cloer()
    },
    methods: {
      /**
       * 派单类型 change 方法[待定功能]============
       * @Param value 派单类型[0出库/1入库]
       */
      changeOperatorSchema (value) {
        console.log('派单类型[0出库/1入库]', value)
        if (value == 0) {
          // 出库操作
          this.sendOrderType = true
        }
        if (value == 1) {
          // 入库操作
          this.sendOrderType = false
        }
      },
      getUrlNewView(textUrl) {
        let config = window._CONFIG['domianURL'] + '/sys/common/view'
        window.open(config + '/' + textUrl, '_blank')
      },
      queryCler () {
        this.model = {}
        this.form = this.$form.createForm(this)

      },
      handleTableChange (pagination, filters, sorter) {
        //分页、排序、筛选变化时触发
        console.log('分页')
        console.log(pagination)
        console.log(filters)
        console.log(sorter)
        //TODO 筛选
        if (Object.keys(sorter).length > 0) {
          this.isorter.column = sorter.field
          this.isorter.order = 'ascend' == sorter.order ? 'asc' : 'desc'
        }
        this.ipagination = pagination;
        this.dataas()
      },
      handleEdits (record) {
        this.$refs.planTheSameDayDeModal.theSames(record)
        this.$refs.planTheSameDayDeModal.title = '修改'
      },
      sdhandleDelete: function (record) {
        var that = this
        let param  = {
          id:record.id,
          type:record.planType
        }
        deleteAction(that.url.wddelete, param ).then((res) => {
          if (res.success) {
            that.$message.success(res.message)
            that.dataas(that.vadjsoifjweoi)
          } else {
            that.$message.warning(res.message)
          }
          that.$emit('ok')
        })
      },
      dataas (ids, paln) {
        let param = {
          ids:this.ids,
          planType:this.paln,
          pageNo:this.ipagination.current,
          pageSize:this.ipagination.pageSize,
        }
        console.log('参数this.ids', this.ids)
        console.log('参数this.paln', this.paln)
        console.log('参数', param)
        getAction('/cable/sendOrders/selectSendOrdersWD', param).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            this.ipagination.total = res.result.total
            console.log('查询数据》：', this.dataSource)
          }
        })
      },
      dakpd (ids, paln) {
        console.log("打开完单记录页面：：",ids,paln)
        this.ids = ids
        this.paln = paln
        this.dataas(ids, paln)
        this.cloer()
        // this.data = val
        // console.log(val)
        // console.log('dakpd《《《《《《《《♥》》》》》》》》》:2')
        // this.paln = paln
        // // 不同计划派单所对应的物料编号
        // if (paln == 1) {
        //   // 计划1物料编号
        //   this.serials = val.wasteMaterialCode
        // } else if (paln == 2) {
        //   // 计划2物料编号
        //   this.serials = val.receiptNo
        //   // this.serials = val.equipmentNo
        // } else if (paln == 3) {
        //   // 计划3物料编号
        //   this.serials = val.materialCode
        // } else {
        //   // 计划4物料编号
        //   this.serials = 'cable2'
        // }
        // this.warehouseList()
        // this.sendOrdersType()
        this.visible = true
      },
      close () {
        this.model = {}
        this.form.resetFields()
        this.$emit('close')
        this.visible = false
      },
      cloer () {
        this.form = this.$form.createForm(this)
        console.log('cloer')
        this.popupCallback({})
      },
      handleOk () {
        const that = this
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true
            let httpurl = ''
            let method = ''
            if (!this.model.id) {
              httpurl += this.url.add
              method = 'post'
            } else {
              httpurl += this.url.edit
              method = 'put'
            }
            this.model.planId = this.data.id
            this.model.planType = this.paln
            this.model.serial = this.serials
            this.model.projectNo = this.data.projectNo
            let formData = Object.assign(this.model, values)
            console.log('表单提交数据', formData)
            httpAction(httpurl, formData, method).then((res) => {
              if (res.success) {
                that.$message.success(res.message)
                that.$emit('ok')
              } else {
                that.$message.warning(res.message)
              }
            }).finally(() => {
              that.confirmLoading = false
              // that.close()
              that.dataas(that.vadjsoifjweoi)
            })
          }

        })
      },
      handleCancel () {
        this.close()
      },
      popupCallback (row) {
        this.form.setFieldsValue(pick(row, 'operatorSchema', 'warehouseId', 'storageLocation', 'taskTime', 'license', ''))
      }
    }
  }
</script>
<style>
  .ant-table-body {
    overflow: auto;
  }
</style>