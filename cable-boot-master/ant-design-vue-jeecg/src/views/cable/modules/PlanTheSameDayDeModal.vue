<template>
  <a-modal
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭" style="margin-top: 200px">
    <a-spin :spinning="confirmLoading">
      <a-form :form="form" style="padding-left: 40px;margin-top: 15px">

        <a-form-item label="联系人" v-if="phones" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 300px">
          <a-input v-decorator="['linkman']" placeholder="请输入联系人"></a-input>
        </a-form-item>

        <a-form-item label="电话" v-if="phones" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 300px">
          <a-input v-decorator="['phone']" placeholder="请输入电话"></a-input>
        </a-form-item>

        <a-form-item label="联系人" v-if="phon" :labelCol="labelCol" :wrapperCol="wrapperCol" style="display: inline-block;width: 500px;margin-left: 30px">
          <a-input v-decorator="['linkman']" placeholder="请输入联系人"></a-input>
        </a-form-item>

      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>

  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import { validateDuplicateValue } from '@/utils/util'


  export default {
    name: "PlanTheSameDayDeModal",
    components: {
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:700,
        visible: false,
        phones:true,
        phon:false,
        model: {},
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {

        },
        url: {
          edit: "/cable/plan1/planTheSameDayEdit",
        },
      }
    },
    created () {
    },
    methods: {
      theSames(record){
        this.visible = true;
        this.edit(record);
        if(record.planType=='4'){
          this.phones =false;
          this.phon = true;
        }
      },
      edit (record) {
        this.form.resetFields();
        this.model = Object.assign({}, record);
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,'linkman','phone'))
        })
      },
      close () {
        this.visible = false;
        this.phones = true;
        this.phon = false;
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = 'put';
            httpurl+=this.url.edit;
            let formData = Object.assign(this.model, values);
            httpAction(httpurl,formData,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }
        })
      },
      handleCancel () {
        this.close()
      },
    }
  }
</script>