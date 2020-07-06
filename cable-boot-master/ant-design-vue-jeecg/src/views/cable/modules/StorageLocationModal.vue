<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @cancel="handleCancel"
    @ok="handleCancel"
    cancelText="关闭" style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">

      <a-table
        ref="table"
        bordered
        rowKey="id"
        size="middle"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        @change="handleTableChange" style="margin-top: 20px">
            <span slot="factoryText" slot-scope="text">
          <j-ellipsis :value="text" :length="10"/>
        </span>
        <span slot="action" slot-scope="text, record">

<!--          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">-->
          <!--            <a>删除</a>-->
          <!--          </a-popconfirm>-->
          <!--          <a-divider type="vertical"/>-->
     <a @click="yiDon(record)">移动物料</a>
        </span>
      </a-table>
      <material-storage-location-modal ref="materialStorageLocationModal"
                                       @ok="modalFormOk"></material-storage-location-modal>
    </a-spin>
  </j-modal>
</template>

<script>
  import MaterialStorageLocationModal from './MaterialStorageLocationModal'
  import { getAction } from '@/api/manage'
  import JEllipsis from '@/components/jeecg/JEllipsis'

  export default {
    name: 'StorageLocationModal',
    components: {
      MaterialStorageLocationModal,
      JEllipsis
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title: '操作',
        width: 900,
        visible: false,
        model: {},
        dataSource: [],
        ipagination: {
          pageNo: 1,
          pageSize: 5,
          pageSizeOptions: ['5', '10'],
          showTotal: (total, range) => {
            return range[0] + '-' + range[1] + ' 共' + total + '条'
          },
          showQuickJumper: true,
          showSizeChanger: true,
          total: 0
        },
        columns: [
          {
            title: '项目编码',
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
            dataIndex: 'materialName',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '库存数',
            align: 'center',
            dataIndex: 'quantityInStock',
            scopedSlots: { customRender: 'factoryText' }
          },
          {
            title: '单位',
            align: 'center',
            dataIndex: 'unit_dictText',
            scopedSlots: { customRender: 'factoryText' }
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
        validatorRules: {},
        storageLocationId: ''
      }
    },
    created () {
    },
    methods: {
      modalFormOk(){
        this.data();
      },
      yiDon (val) {
        this.$refs.materialStorageLocationModal.yiDon(val,this.storageLocationId )
        this.$refs.materialStorageLocationModal.title = ''
      },
      data () {
        console.log(this.storageLocationId)
        getAction('/cable/inventory/insurancePageList', { storageLocationId: this.storageLocationId }).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
            console.log(this.dataSource)
          }
        })
        this.visible = true
      },
      showStorageLocation (wa, val) {
        this.storageLocationId = val
        this.visible = true
        this.data()
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
        /*this.ipagination = pagination;*/
        this.data(this.date)
      },
      close () {
        // this.$emit('close')
        this.$emit('ok');
        this.visible = false
      },
      handleCancel () {
        console.log("确定")
        this.close()
      }

    }
  }

</script>
<style>

  .yida {
    display: inline-block;
    margin-top: 20px;
    margin-left: 15px;
    width: 80px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    border-radius: 5px;
    background-color: #afafaf;
    color: white;
  }
</style>