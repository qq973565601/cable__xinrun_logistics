<!--<template>-->
<!--  <a-modal-->
<!--    :title="title"-->
<!--    :width="1040"-->
<!--    :visible="visible"-->
<!--    :confirmLoading="confirmLoading"-->
<!--    cancelText="关闭"-->
<!--    @cancel="handleCancel"-->
<!--    :footer="null">-->

<!--    <a-spin :spinning="confirmLoading">-->
<!--      <a-form :form="form">-->
<!--        <div style="width: 1000px;">-->
<!--          <div style="margin-top: 10px;">-->
<!--            <div style="border: 0.5px solid #cacaca;margin-top: -10px;">-->
<!--              <a-table-->
<!--                ref="table"-->
<!--                bordered-->
<!--                rowKey="storageLocationId"-->
<!--                size="middle"-->
<!--                :columns="columns"-->
<!--                :dataSource="dataSource">-->
<!--&lt;!&ndash;                <span slot="action" slot-scope="text, record">&ndash;&gt;-->



<!--&lt;!&ndash;          </span>&ndash;&gt;-->

<!--              </a-table>-->
<!--            </div>-->
<!--          </div>-->
<!--          &lt;!&ndash;          <div style="overflow-y: scroll;overflow-x: hidden;height: 200px;">&ndash;&gt;-->
<!--          &lt;!&ndash;            <div class="message" v-for="(message,index) in messageList">&ndash;&gt;-->
<!--          &lt;!&ndash;              <img :src="message.picUrl" class="message-img"/>&ndash;&gt;-->
<!--          &lt;!&ndash;              <span class="message-span2">{{message.wxNc}}</span>&ndash;&gt;-->
<!--          &lt;!&ndash;              <p class="message-p">{{message.content}}</p>&ndash;&gt;-->
<!--          &lt;!&ndash;            </div>&ndash;&gt;-->
<!--          &lt;!&ndash;          </div>&ndash;&gt;-->
<!--          &lt;!&ndash; 子表单区域 &ndash;&gt;-->
<!--          &lt;!&ndash;            <a-tab-pane tab="客户信息" key="1" :forceRender="true">&ndash;&gt;-->


<!--        </div>-->


<!--      </a-form>-->
<!--    </a-spin>-->
<!--  </a-modal>-->
<!--</template>-->
<!--<script>-->
<!--  import { getAction } from '@/api/manage'-->
<!--  import pick from 'lodash.pick'-->
<!--  import { validateDuplicateValue } from '@/utils/util'-->
<!--  import JDate from '@/components/jeecg/JDate'-->
<!--  import {JeecgListMixin} from '@/mixins/JeecgListMixin'-->


<!--  export default {-->
<!--    name: 'InventoryModal',-->
<!--    mixins: [JeecgListMixin],-->
<!--    components: {-->
<!--      JDate-->
<!--    },-->
<!--    data() {-->
<!--      return {-->
<!--        form: this.$form.createForm(this),-->
<!--        title: '',-->
<!--        width: 800,-->
<!--        visible: false,-->
<!--        model: {},-->
<!--        columns: [-->
<!--          {-->
<!--            title: '数量',-->
<!--            align: 'center',-->
<!--            dataIndex: 'currentInventory'-->
<!--          },-->
<!--          {-->
<!--            title: '库位',-->
<!--            align: 'center',-->
<!--            dataIndex: 'storageLocationName'-->
<!--          }-->
<!--        ],-->
<!--        confirmLoading: false,-->
<!--        url: {-->
<!--          list: '/cable/testdata/keweiQuery'-->

<!--        },-->
<!--        dataSource: [],-->
<!--      }-->
<!--    },-->
<!--    created() {-->
<!--    },-->
<!--    methods: {-->
<!--      loadData(arg) {-->

<!--      },-->
<!--      outshwoz(record) {-->
<!--        console.log('打开页面')-->
<!--        console.log(record)-->
<!--        console.log(record.id)-->
<!--        console.log(record.id)-->
<!--        console.log(record.id)-->
<!--        this.visible = true-->
<!--        let that = this-->
<!--       var ls = record.id.split('_')-->
<!--        getAction(that.url.list, { id: ls[0], type: ls[1] ,warehouseId:record.warehouseId}, 'get').then((res) => {-->
<!--          if (res.success) {-->
<!--            console.log('res', res)-->
<!--            that.dataSource = res.result-->
<!--          } else {-->
<!--            that.$message.warning(res.message)-->
<!--          }-->
<!--        })-->
<!--      },-->

<!--      close() {-->
<!--        this.$emit('close')-->
<!--        this.dataSource = []-->
<!--        this.visible = false-->
<!--      },-->
<!--      handleOk() {-->
<!--        const that = this-->
<!--        // 触发表单验证-->
<!--        this.form.validateFields((err, values) => {-->
<!--          if (!err) {-->
<!--            that.confirmLoading = true-->
<!--            let httpurl = ''-->
<!--            let method = ''-->
<!--            if (!this.model.id) {-->
<!--              httpurl += this.url.add-->
<!--              method = 'post'-->
<!--            } else {-->
<!--              httpurl += this.url.edit-->
<!--              method = 'put'-->
<!--            }-->
<!--            let formData = Object.assign(this.model, values)-->
<!--            console.log('表单提交数据', formData)-->
<!--            httpAction(httpurl, formData, method).then((res) => {-->
<!--              if (res.success) {-->
<!--                that.$message.success(res.message)-->
<!--                that.$emit('ok')-->
<!--              } else {-->
<!--                that.$message.warning(res.message)-->
<!--              }-->
<!--            }).finally(() => {-->
<!--              that.confirmLoading = false-->
<!--              that.close()-->
<!--            })-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      handleCancel() {-->
<!--        this.close()-->
<!--      }-->
<!--    }-->
<!--  }-->
<!--</script>-->


<template>
  <a-modal
    :title="title"
    :width="1040"
    :visible="visible"
    :confirmLoading="confirmLoading"
    cancelText="关闭"
    @cancel="handleCancel"
    :footer="null">

    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <div style="width: 1000px;">
          <div style="margin-top: 10px;">
            <div style="border: 0.5px solid #cacaca;margin-top: -10px;">
              <a-table
                ref="table"
                bordered
                rowKey="storageLocationId"
                size="middle"
                :columns="columns"
                :dataSource="dataSource">
                <!--                <span slot="action" slot-scope="text, record">-->



                <!--          </span>-->

              </a-table>
            </div>
          </div>
          <!--          <div style="overflow-y: scroll;overflow-x: hidden;height: 200px;">-->
          <!--            <div class="message" v-for="(message,index) in messageList">-->
          <!--              <img :src="message.picUrl" class="message-img"/>-->
          <!--              <span class="message-span2">{{message.wxNc}}</span>-->
          <!--              <p class="message-p">{{message.content}}</p>-->
          <!--            </div>-->
          <!--          </div>-->
          <!-- 子表单区域 -->
          <!--            <a-tab-pane tab="客户信息" key="1" :forceRender="true">-->


        </div>


      </a-form>
    </a-spin>
  </a-modal>
</template>
<script>
  import { getAction } from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'
  import JDate from '@/components/jeecg/JDate'
  import {JeecgListMixin} from '@/mixins/JeecgListMixin'


  export default {
    name: 'InventoryModal',
    mixins: [JeecgListMixin],
    components: {
      JDate
    },
    data() {
      return {
        form: this.$form.createForm(this),
        title: '',
        width: 800,
        visible: false,
        model: {},
        columns: [
          {
            title: '数量',
            align: 'center',
            dataIndex: 'currentInventory'
          },
          {
            title: '库位',
            align: 'center',
            dataIndex: 'storageLocationName'
          }
        ],
        confirmLoading: false,
        url: {
          list: '/cable/testdata/keweiQuery'

        },
        dataSource: [],
      }
    },
    created() {
    },
    methods: {
      loadData(arg) {

      },
      outshwoz(record) {
        this.visible = true
        let that = this
        // var ls = record.id.split('_')
        // getAction(that.url.list, { id: ls[0], type: ls[1] ,warehouseId:record.warehouseId}, 'get').then((res) => {
        getAction(that.url.list, { id: record.id}, 'get').then((res) => {
          if (res.success) {
            console.log('res', res)
            that.dataSource = res.result
          } else {
            that.$message.warning(res.message)
          }
        })
      },

      close() {
        this.$emit('close')
        this.dataSource = []
        this.visible = false
      },
      handleOk() {
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
              that.close()
            })
          }
        })
      },
      handleCancel() {
        this.close()
      }
    }
  }
</script>