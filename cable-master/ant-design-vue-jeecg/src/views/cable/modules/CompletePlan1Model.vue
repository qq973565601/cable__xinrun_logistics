<template>
  <a-modal
    :title="title"
    :width="1200"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form"
              :label-col="{ span: 8 }"
              :wrapper-col="{ span: 14 }">
        <!-- 主表单区域 -->

        <a-row :gutter="24">
          <a-col :md="6" :sm="12">
            <a-form-item label="派单类型">
              <a-select v-decorator="['operatorSchema',validatorRules.operatorSchema]" placeholder="请选择派单类型" @change="changeOperatorSchema">
                  <a-select-option value="0">出库</a-select-option>
                  <a-select-option value="1">入库</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="自家仓库">
              <j-dict-select-tag v-decorator="['warehouseId',validatorRules.warehouseId]" :triggerChange="true" @change="types"
                                 placeholder="请选择自家仓库" dictCode="warehouse,name,id,type='1'"/>
            </a-form-item>
          </a-col>

          <a-col :md="6" :sm="12" v-if="this.sendOrderType">
            <a-form-item label="自家库位">
              <a-select v-decorator="['storageLocationId',validatorRules.storageLocationId]" id="sl"
                        placeholder="请选择自家库位">
                <template v-for="storageLocation in storageLocations">
                  <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}
                  </a-select-option>
                </template>
              </a-select>
              <!--<a-select v-decorator="['storageLocationId',validatorRules.storageLocationId]" id="sl"
                        placeholder="请选择自家库位">
                <template v-for="storageLocation in storageLocations">
                  <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}
                  </a-select-option>
                </template>
              </a-select>-->
            </a-form-item>
          </a-col>
          <!-- 出库才选择终点仓库,入库不需要选择终点仓库[根据 this.sendOrderType 做判断是否显示] -->
          <a-col :md="6" :sm="12" v-if="this.sendOrderType">
            <a-form-item label="终点仓库">

              <j-dict-select-tag v-decorator="['endWarehouseId',validatorRules.endWarehouseId]" :triggerChange="true"
                                 placeholder="请选择终点仓库" dictCode="warehouse,name,id"/>

            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="12">
            <a-form-item label="任务日期">
              <j-date v-decorator="['taskTime',validatorRules.taskTime]" :showTime="true" date-format="YYYY-MM-DD"
                      placeholder="请选择任务时间">
              </j-date>
            </a-form-item>
          </a-col>


          <a-col :span="12">
            <a-form-item label="员工">
              <a-select
                mode="multiple"
                placeholder="请选择员工"
                optionFilterProp="children"
                v-decorator="['realname',validatorRules.realname]"
                :getPopupContainer="(target) => target.parentNode">
                <a-select-option v-for="(role,roleindex) in users" :key="roleindex.toString()" :value="role.id">
                  {{ role.realname }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <!--<a-row :gutter="24">
          &lt;!&ndash;<a-row :gutter="24">
            <a-col :span="12">
              <a-form-item label="员工">
                <a-select
                  mode="multiple"
                  placeholder="请选择员工"
                  optionFilterProp="children"
                  v-decorator="['realname',validatorRules.realname]"
                  :getPopupContainer="(target) => target.parentNode">
                  <a-select-option v-for="(role,roleindex) in users" :key="roleindex.toString()" :value="role.id">
                    {{ role.realname }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="12">
              <a-form-item label="车辆">
                <a-select
                  mode="multiple"
                  placeholder="请选择车辆"
                  optionFilterProp="children"
                  v-decorator="['license',validatorRules.license]"
                  :getPopupContainer="(target) => target.parentNode">
                  <a-select-option v-for="(role,roleindex) in vehicles" :key="roleindex.toString()" :value="role.license">
                    {{ role.license }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>&ndash;&gt;

          <a-col :span="12">
            <a-button @click="handleOk" type="primary" style="width: 100px;float: right;margin-right: 230px">
              <template v-if="!model.id">派单</template>
              <template v-if="model.id">保存</template>
            </a-button>
            <a-button @click="queryCler" type="primary" style="width: 100px;float:right;margin-right: 30px;">
              清空
            </a-button>
          </a-col>
        </a-row>-->
      </a-form>

      <!-- 子表单区域 -->
      <a-tabs v-model="activeKey" @change="handleChangeTabs">
        <a-tab-pane tab="派单信息" key="1" :forceRender="true">

          <j-editable-table
            ref="editableTable1"
            :loading="table1.loading"
            :columns="table1.columns"
            :dataSource="table1.dataSource"
            :maxHeight="300"
            align="center"
            :rowNumber="false"
            :alwaysEdit="false"
            :rowSelection="true"
            :actionButton="false"/>

        </a-tab-pane>

        <a-tab-pane tab="车辆人员信息" key="2" :forceRender="true">

          <j-editable-table
            ref="editableTable2"
            :loading="table2.loading"
            :columns="table2.columns"
            :dataSource="table2.dataSource"
            :maxHeight="300"
            align="center"
            :rowNumber="false"
            :rowSelection="true"
            :actionButton="true"/>

          <span slot="factoryText" slot-scope="text">
            <j-ellipsis :value="text" :length="10"/>
          </span>

        </a-tab-pane>
      </a-tabs>

    </a-spin>
  </a-modal>
</template>

<script>

  import JEditableTable from '@/components/jeecg/JEditableTable'
  import { FormTypes, VALIDATE_NO_PASSED, getRefPromise, validateFormAndTables } from '@/utils/JEditableTableUtil'
  import { httpAction, getAction } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import pick from 'lodash.pick'
  import moment from 'moment'

  export default {
    name: 'CompletePlan1Model',
    components: {
      JDate, JEditableTable
    },
    data() {
      return {
        title: '操作',
        visible: false,
        form: this.$form.createForm(this),
        confirmLoading: false,
        // 派单类型
        sendOrderType: false,
        storageLocations:[],
        model: {},
        validatorRules: {
          operatorSchema: {rules: [{required: true, message: '请选择派单类型'}]},
          warehouseId: {rules: [{required: true, message: '请选择目标仓库'}]},
          endWarehouseId: {rules: [{required: true, message: '请选择终点仓库'}]},
          storageLocationId: {rules: [{required: true, message: '请选择库位'}]},
          taskTime: {rules: [{required: true, message: '请选择任务时间'}]}
        },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 6 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 24 - 6 }
        },
        activeKey: '1',
        // 客户信息
        table1: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '工程账号',
              key: 'projectNo',
              width: '15%',
              type: FormTypes.normal,
              defaultValue: 'A1002',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '工程名称',
              key: 'projectName',
              width: '18%',
              type: FormTypes.normal,
              defaultValue: 'A一零零二',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }],
              scopedSlots: { customRender: 'factoryText' },
            },
            {
              title: '物料名称',
              key: 'wasteMaterialText',
              width: '18%',
              type: FormTypes.normal,
              defaultValue: 'A02',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '物料数量',
              key: 'numReceipts',
              width: '8%',
              type: FormTypes.inputNumber,
              defaultValue: '',
              placeholder: '${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '地址',
              key: 'engineeringAddress',
              width: '15%',
              type: FormTypes.normal,
              defaultValue: '长江南路电力仓库',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '联系人',
              key: 'projectContact',
              width: '8%',
              type: FormTypes.normal,
              defaultValue: '李四',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '联系电话',
              key: 'projectPhone',
              width: '15%',
              type: FormTypes.normal,
              defaultValue: '10086',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            /*{
              title: '性别',
              key: 'sex',
              width: '18%',
              type: FormTypes.select,
              options: [ // 下拉选项
                { title: '男', value: '1' },
                { title: '女', value: '2' }
              ],
              defaultValue: '',
              placeholder: '请选择${title}'
            },
            {
              title: '身份证号',
              key: 'idcard',
              width: '15%',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{
                pattern: '^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$',
                message: '${title}格式不正确'
              }]
            },
            {
              title: '手机号',
              key: 'telphone',
              width: '15%',
              type: FormTypes.input,
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{
                pattern: '^1(3|4|5|7|8)\\d{9}$',
                message: '${title}格式不正确'
              }]
            }*/
          ]
        },
        // 车辆人员信息
        table2: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '车辆',
              key: 'license',
              width: '20%',
              type: FormTypes.sel_search,
              options:this.vehicles,
              dictCode:"vehicle,license,license,state='0'",
              defaultValue: '',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '数量',
              key: 'number',
              width: '10%',
              type: FormTypes.inputNumber,
              defaultValue: '1',
              placeholder: '请输入${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
            {
              title: '员工',
              key: 'realname',
              width: '20%',
              type: FormTypes.sel_search,
              options:this.vehicles,
              dictCode:"sys_user,realname,id,status='1'",
              defaultValue: '',
              placeholder: '请选择${title}',
              validateRules: [{ required: true, message: '${title}不能为空' }]
            },
          ]
        },
        url: {
          add: '/test/jeecgOrderMain/add',
          edit: '/test/jeecgOrderMain/edit',
          orderCustomerList: '/cable/plan1/idslist',
          orderTicketList: '/test/jeecgOrderMain/queryOrderTicketListByMainId'
        }
      }
    },
    created() {
      this.vehiclesList()
    },
    methods: {
      /**
       * 自家仓库
       */
      types(val) {
        this.storageLocations = []
        this.form.setFieldsValue(pick({storageLocationId: undefined}, 'storageLocationId'))
        let va = val
        getAction('/cable/storageLocation/list', {warehouseId: va}).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log(this.storageLocations)
          }
        })
      },
      /**
       * 派单类型 change 方法[待定功能]============
       * @Param value 派单类型[0出库/1入库]
       */
      changeOperatorSchema(value) {
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
      // 获取所有的editableTable实例
      getAllTable() {
        return Promise.all([
          getRefPromise(this, 'editableTable1'),
          getRefPromise(this, 'editableTable2')
        ])
      },

      add() {
        // 默认新增一条数据
        this.getAllTable().then(editableTables => {
          editableTables[0].add()
          editableTables[1].add()
        })

        this.edit({})
      },
      completePlanModelShow(record) {
        this.activeKey = '1'
        this.form.resetFields()
        this.model = Object.assign({}, record)

        /*this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model, 'orderCode', 'ctype', 'orderMoney', 'content'))
          //时间格式化
          this.form.setFieldsValue({ orderDate: this.model.orderDate ? moment(this.model.orderDate) : null })
        })*/

        console.log("打开合并完单页面》》》： ",record)
        // 加载子表数据
        if (record) {
          let params = { ids: record.toString() }
          this.requestTableData(this.url.orderCustomerList, params, this.table1)
          // this.requestTableData(this.url.orderTicketList, params, this.table2)
        }

      },
      close() {
        this.storageLocations = []
        this.vehicles={}
        this.visible = false
        this.getAllTable().then(editableTables => {
          editableTables[0].initialize()
          editableTables[1].initialize()
        })
        this.$emit('close')
      },
      /** 查询某个tab的数据 */
      requestTableData(url, params, tab) {
        tab.loading = true
        getAction(url, params).then(res => {
          if (res.success) {
            this.visible = true
            tab.dataSource = res.result.records
          }else {
            this.visible = false
            this.$message.warning(res.message)
            return
          }
          console.log("查询成功》》数据是》》：",res.result.records,"表格是》：",tab)
        }).finally(() => {
          tab.loading = false
        })
      },
      handleOk() {
        this.validateFields()
      },
      handleCancel() {
        this.close()
      },
      /** ATab 选项卡切换事件 */
      handleChangeTabs(key) {
        getRefPromise(this, `editableTable${key}`).then(editableTable => {
          editableTable.resetScrollTop()
        })
      },

      /** 触发表单验证 */
      validateFields() {
        this.getAllTable().then(tables => {
          /** 一次性验证主表和所有的次表 */
          return validateFormAndTables(this.form, tables)
        }).then(allValues => {
          let formData = this.classifyIntoFormData(allValues)
          // 发起请求
          return this.requestAddOrEdit(formData)
        }).catch(e => {
          if (e.error === VALIDATE_NO_PASSED) {
            // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
            this.activeKey = e.index == null ? this.activeKey : (e.index + 1).toString()
          } else {
            console.error(e)
          }
        })
      },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let orderMain = Object.assign(this.model, allValues.formValue)
        //时间格式化
        orderMain.orderDate = orderMain.orderDate ? orderMain.orderDate.format('YYYY-MM-DD HH:mm:ss') : null
        return {
          ...orderMain, // 展开
          jeecgOrderCustomerList: allValues.tablesValue[0].values,
          jeecgOrderTicketList: allValues.tablesValue[1].values
        }
      },
      /** 发起新增或修改的请求 */
      requestAddOrEdit(formData) {
        let url = this.url.add, method = 'post'
        if (this.model.id) {
          url = this.url.edit
          method = 'put'
        }
        this.confirmLoading = true
        httpAction(url, formData, method).then((res) => {
          if (res.success) {
            this.$message.success(res.message)
            this.$emit('ok')
            this.close()
          } else {
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.confirmLoading = false
        })
      }

    }
  }
</script>

<style scoped>
</style>