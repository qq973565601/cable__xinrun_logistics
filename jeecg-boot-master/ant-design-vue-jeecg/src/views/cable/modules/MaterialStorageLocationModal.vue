<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <j-modal&ndash;&gt;-->
<!--&lt;!&ndash;    :title="title"&ndash;&gt;-->
<!--&lt;!&ndash;    :width="width"&ndash;&gt;-->
<!--&lt;!&ndash;    :visible="visible"&ndash;&gt;-->
<!--&lt;!&ndash;    :confirmLoading="confirmLoading"&ndash;&gt;-->
<!--&lt;!&ndash;    @ok="handleOk"&ndash;&gt;-->
<!--&lt;!&ndash;    @cancel="handleCancel"&ndash;&gt;-->
<!--&lt;!&ndash;    cancelText="关闭">&ndash;&gt;-->
<!--&lt;!&ndash;    <a-spin :spinning="confirmLoading">&ndash;&gt;-->

<!--&lt;!&ndash;      <a-spin :spinning="confirmLoading">&ndash;&gt;-->
<!--&lt;!&ndash;        <a-form :form="form" style="padding-left: 40px;margin-top: 15px">&ndash;&gt;-->

<!--&lt;!&ndash;          <a-form-item label="转移库位" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-select v-decorator="['storageLocationCId', validatorRules.storageLocationCId]" @change="materialsList" placeholder="请选择转移库位">&ndash;&gt;-->
<!--&lt;!&ndash;              <template v-for="(storageLocation,index) in storageLocations">&ndash;&gt;-->
<!--&lt;!&ndash;                <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}</a-select-option>&ndash;&gt;-->
<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </a-select>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;          <a-form-item label="转移物料" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-select v-decorator="['materialId', validatorRules.materialId]" placeholder="请选择转移物料">&ndash;&gt;-->
<!--&lt;!&ndash;              <template v-for="(material,index) in materials">&ndash;&gt;-->
<!--&lt;!&ndash;                <a-select-option v-bind:value="material.mid">{{material.materialName}}</a-select-option>&ndash;&gt;-->
<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </a-select>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;          <a-form-item label="转移数量" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-input-number v-decorator="['amount']" placeholder="请输入转移数量"></a-input-number>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;          <a-form-item label="转移容积" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-input-number v-decorator="['accomplishVolume']" placeholder="请输入转移容积"></a-input-number>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;          <a-form-item label="存放库位" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--&lt;!&ndash;            <a-select v-decorator="['storageLocationRId', validatorRules.storageLocationRId]" placeholder="请选择存放库位">&ndash;&gt;-->
<!--&lt;!&ndash;              <template v-for="(storageLocation,index) in storageLocations">&ndash;&gt;-->
<!--&lt;!&ndash;                <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}</a-select-option>&ndash;&gt;-->
<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </a-select>&ndash;&gt;-->
<!--&lt;!&ndash;          </a-form-item>&ndash;&gt;-->

<!--&lt;!&ndash;        </a-form>&ndash;&gt;-->
<!--&lt;!&ndash;      </a-spin>&ndash;&gt;-->

<!--&lt;!&ndash;    </a-spin>&ndash;&gt;-->
<!--&lt;!&ndash;  </j-modal>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script>&ndash;&gt;-->
<!--&lt;!&ndash;  import {httpAction, getAction} from '@/api/manage'&ndash;&gt;-->
<!--&lt;!&ndash;  import pick from 'lodash.pick'&ndash;&gt;-->
<!--&lt;!&ndash;  import JEllipsis from '@/components/jeecg/JEllipsis'&ndash;&gt;-->


<!--&lt;!&ndash;  export default {&ndash;&gt;-->
<!--&lt;!&ndash;    name: "MaterialStorageLocationModal",&ndash;&gt;-->
<!--&lt;!&ndash;    components: {&ndash;&gt;-->
<!--&lt;!&ndash;      JEllipsis&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    data () {&ndash;&gt;-->
<!--&lt;!&ndash;      return {&ndash;&gt;-->
<!--&lt;!&ndash;        form: this.$form.createForm(this),&ndash;&gt;-->
<!--&lt;!&ndash;        title:"操作",&ndash;&gt;-->
<!--&lt;!&ndash;        width:800,&ndash;&gt;-->
<!--&lt;!&ndash;        visible: false,&ndash;&gt;-->
<!--&lt;!&ndash;        model: {},&ndash;&gt;-->
<!--&lt;!&ndash;        confirmLoading: false,&ndash;&gt;-->
<!--&lt;!&ndash;        labelCol: {&ndash;&gt;-->
<!--&lt;!&ndash;          xs: { span: 24 },&ndash;&gt;-->
<!--&lt;!&ndash;          sm: { span: 5 },&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        wrapperCol: {&ndash;&gt;-->
<!--&lt;!&ndash;          xs: { span: 24 },&ndash;&gt;-->
<!--&lt;!&ndash;          sm: { span: 16 },&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        validatorRules: {&ndash;&gt;-->

<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        url: {&ndash;&gt;-->
<!--&lt;!&ndash;          edit: "/cable/storageLocation/MaterialStorageLocationEdit",&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        storageLocationId:'',&ndash;&gt;-->
<!--&lt;!&ndash;        warehouseId:'',&ndash;&gt;-->
<!--&lt;!&ndash;        storageLocations:[],&ndash;&gt;-->
<!--&lt;!&ndash;        materials:[],&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    created () {&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    methods: {&ndash;&gt;-->
<!--&lt;!&ndash;      yiDon(sid,wid){&ndash;&gt;-->
<!--&lt;!&ndash;        console.log('移库')&ndash;&gt;-->
<!--&lt;!&ndash;        this.warehouseId = wid;&ndash;&gt;-->
<!--&lt;!&ndash;        this.storageLocationId = sid;&ndash;&gt;-->
<!--&lt;!&ndash;        this.visible = true;&ndash;&gt;-->
<!--&lt;!&ndash;        console.log(this.warehouseId)&ndash;&gt;-->
<!--&lt;!&ndash;        console.log(this.storageLocationId)&ndash;&gt;-->
<!--&lt;!&ndash;        this.storageLocationList();&ndash;&gt;-->
<!--&lt;!&ndash;        this.materialsList();&ndash;&gt;-->
<!--&lt;!&ndash;      },&ndash;&gt;-->
<!--&lt;!&ndash;      storageLocationList() {&ndash;&gt;-->
<!--&lt;!&ndash;        this.storageLocations.length = 0;&ndash;&gt;-->
<!--&lt;!&ndash;        getAction("/cable/storageLocation/list", {warehouseId: this.warehouseId}).then((res) => {&ndash;&gt;-->
<!--&lt;!&ndash;          if (res.success) {&ndash;&gt;-->
<!--&lt;!&ndash;            this.storageLocations = res.result&ndash;&gt;-->
<!--&lt;!&ndash;            console.log("加载库位")&ndash;&gt;-->
<!--&lt;!&ndash;            console.log(this.storageLocations)&ndash;&gt;-->
<!--&lt;!&ndash;          }&ndash;&gt;-->
<!--&lt;!&ndash;        })&ndash;&gt;-->
<!--&lt;!&ndash;      },&ndash;&gt;-->
<!--&lt;!&ndash;      materialsList(val) {&ndash;&gt;-->
<!--&lt;!&ndash;        this.materials.length = 0;&ndash;&gt;-->
<!--&lt;!&ndash;        getAction("/cable/inventory/insuranceLists", {storageLocationId:val}).then((res) => {&ndash;&gt;-->
<!--&lt;!&ndash;          if (res.success) {&ndash;&gt;-->
<!--&lt;!&ndash;            this.materials = res.result&ndash;&gt;-->
<!--&lt;!&ndash;            console.log("加载物料")&ndash;&gt;-->
<!--&lt;!&ndash;            console.log(this.materials)&ndash;&gt;-->
<!--&lt;!&ndash;          }&ndash;&gt;-->
<!--&lt;!&ndash;        })&ndash;&gt;-->
<!--&lt;!&ndash;      },&ndash;&gt;-->
<!--&lt;!&ndash;      handleOk () {&ndash;&gt;-->
<!--&lt;!&ndash;        const that = this;&ndash;&gt;-->
<!--&lt;!&ndash;        // 触发表单验证&ndash;&gt;-->
<!--&lt;!&ndash;        this.form.validateFields((err, values) => {&ndash;&gt;-->
<!--&lt;!&ndash;          if (!err) {&ndash;&gt;-->
<!--&lt;!&ndash;            that.confirmLoading = true;&ndash;&gt;-->
<!--&lt;!&ndash;            let httpurl = '';&ndash;&gt;-->
<!--&lt;!&ndash;            let method = 'put';&ndash;&gt;-->
<!--&lt;!&ndash;            httpurl+=this.url.edit;&ndash;&gt;-->
<!--&lt;!&ndash;            let formData = Object.assign(this.model, values);&ndash;&gt;-->
<!--&lt;!&ndash;            console.log("表单提交数据",formData)&ndash;&gt;-->
<!--&lt;!&ndash;            httpAction(httpurl,formData,method).then((res)=>{&ndash;&gt;-->
<!--&lt;!&ndash;              if(res.success){&ndash;&gt;-->
<!--&lt;!&ndash;                that.$message.success(res.message);&ndash;&gt;-->
<!--&lt;!&ndash;                that.$emit('ok');&ndash;&gt;-->
<!--&lt;!&ndash;              }else{&ndash;&gt;-->
<!--&lt;!&ndash;                that.$message.warning(res.message);&ndash;&gt;-->
<!--&lt;!&ndash;              }&ndash;&gt;-->
<!--&lt;!&ndash;            }).finally(() => {&ndash;&gt;-->
<!--&lt;!&ndash;              that.confirmLoading = false;&ndash;&gt;-->
<!--&lt;!&ndash;              that.close();&ndash;&gt;-->
<!--&lt;!&ndash;            })&ndash;&gt;-->
<!--&lt;!&ndash;          }&ndash;&gt;-->

<!--&lt;!&ndash;        })&ndash;&gt;-->
<!--&lt;!&ndash;      },&ndash;&gt;-->
<!--&lt;!&ndash;      close () {&ndash;&gt;-->
<!--&lt;!&ndash;        this.form=this.$form.createForm(this);&ndash;&gt;-->
<!--&lt;!&ndash;        this.$emit('close');&ndash;&gt;-->
<!--&lt;!&ndash;        this.visible = false;&ndash;&gt;-->
<!--&lt;!&ndash;      },&ndash;&gt;-->
<!--&lt;!&ndash;      handleCancel () {&ndash;&gt;-->
<!--&lt;!&ndash;        this.close()&ndash;&gt;-->
<!--&lt;!&ndash;      },&ndash;&gt;-->


<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->
<!--<template>-->
<!--  <j-modal-->
<!--    :title="title"-->
<!--    :width="width"-->
<!--    :visible="visible"-->
<!--    :confirmLoading="confirmLoading"-->
<!--    @ok="handleOk"-->
<!--    @cancel="handleCancel"-->
<!--    cancelText="关闭">-->
<!--    <a-spin :spinning="confirmLoading">-->

<!--      <a-spin :spinning="confirmLoading">-->
<!--        <a-form :form="form" style="padding-left: 40px;margin-top: 15px">-->

<!--          &lt;!&ndash;          <a-form-item label="转移库位" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--          &lt;!&ndash;            <a-select v-decorator="['storageLocationCId', validatorRules.storageLocationCId]" @change="materialsList" placeholder="请选择转移库位">&ndash;&gt;-->
<!--          &lt;!&ndash;              <template v-for="(storageLocation,index) in storageLocations">&ndash;&gt;-->
<!--          &lt;!&ndash;                <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}</a-select-option>&ndash;&gt;-->
<!--          &lt;!&ndash;              </template>&ndash;&gt;-->
<!--          &lt;!&ndash;            </a-select>&ndash;&gt;-->
<!--          &lt;!&ndash;          </a-form-item>&ndash;&gt;-->

<!--          &lt;!&ndash;          <a-form-item label="转移物料" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--          &lt;!&ndash;            <a-select v-decorator="['materialId', validatorRules.materialId]" placeholder="请选择转移物料">&ndash;&gt;-->
<!--          &lt;!&ndash;              <template v-for="(material,index) in materials">&ndash;&gt;-->
<!--          &lt;!&ndash;                <a-select-option v-bind:value="material.mid">{{material.materialName}}</a-select-option>&ndash;&gt;-->
<!--          &lt;!&ndash;              </template>&ndash;&gt;-->
<!--          &lt;!&ndash;            </a-select>&ndash;&gt;-->
<!--          &lt;!&ndash;          </a-form-item>&ndash;&gt;-->



<!--          &lt;!&ndash;          <a-form-item label="转移容积" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--          &lt;!&ndash;            <a-input-number v-decorator="['accomplishVolume']" placeholder="请输入转移容积"></a-input-number>&ndash;&gt;-->
<!--          &lt;!&ndash;          </a-form-item>&ndash;&gt;-->
<!--          &lt;!&ndash;          <a-form-item label="单位" :labelCol="labelCol" :wrapperCol="wrapperCol" class="inupt_d"style="margin-left: -35px">&ndash;&gt;-->
<!--          &lt;!&ndash;            <a-select v-decorator="['rawMaterialUnit', validatorRules.rawMaterialUnit]" class="inupt_b_a" style="width: 220px" placeholder="请选择原物料单位">&ndash;&gt;-->
<!--          &lt;!&ndash;              <template v-for="(materialUnit,index) in materialUnits">&ndash;&gt;-->
<!--          &lt;!&ndash;                <a-select-option v-bind:value="materialUnit.itemValue">{{materialUnit.itemText}}</a-select-option>&ndash;&gt;-->
<!--          &lt;!&ndash;              </template>&ndash;&gt;-->
<!--          &lt;!&ndash;            </a-select>&ndash;&gt;-->
<!--          &lt;!&ndash;          </a-form-item>&ndash;&gt;-->
<!--          <a-form-item label="转移数量" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--            <a-input-number v-decorator="['amount', model.amount]" placeholder="请输入转移数量"></a-input-number>-->
<!--          </a-form-item>-->
<!--          <a-form-item label="存放库位" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--            <a-select v-decorator="['storageLocationRId', model.storageLocationRId]" placeholder="请选择存放库位">-->
<!--              <template v-for="(storageLocation,index) in storageLocations">-->
<!--                <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}</a-select-option>-->
<!--              </template>-->
<!--            </a-select>-->
<!--          </a-form-item>-->

<!--        </a-form>-->
<!--      </a-spin>-->

<!--    </a-spin>-->
<!--  </j-modal>-->
<!--</template>-->

<!--<script>-->
<!--  import {httpAction, getAction} from '@/api/manage'-->
<!--  import pick from 'lodash.pick'-->
<!--  import JEllipsis from '@/components/jeecg/JEllipsis'-->


<!--  export default {-->
<!--    name: "MaterialStorageLocationModal",-->
<!--    components: {-->
<!--      JEllipsis-->
<!--    },-->
<!--    data () {-->
<!--      return {-->
<!--        form: this.$form.createForm(this),-->
<!--        title:"操作",-->
<!--        width:800,-->
<!--        visible: false,-->
<!--        model: {},-->
<!--        confirmLoading: false,-->
<!--        labelCol: {-->
<!--          xs: { span: 24 },-->
<!--          sm: { span: 5 },-->
<!--        },-->
<!--        wrapperCol: {-->
<!--          xs: { span: 24 },-->
<!--          sm: { span: 16 },-->
<!--        },-->
<!--        validatorRules: {-->

<!--        },-->
<!--        url: {-->
<!--          edit: "/cable/storageLocation/MaterialStorageLocationEdit",-->
<!--        },-->
<!--        storageLocationId:'',-->
<!--        warehouseId:'',-->
<!--        storageLocations:[],-->
<!--        materials:[],-->
<!--        materialUnits:{},-->
<!--      }-->
<!--    },-->
<!--    created () {-->
<!--      this.getMaterialUnitList();-->
<!--    },-->
<!--    methods: {-->
<!--      getMaterialUnitList() {-->
<!--        getAction('/sys/dictItem/getDictItemUnit').then((res) => {-->
<!--          if (res.success) {-->
<!--            this.materialUnits = res.result-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      yiDon(val,kid){-->
<!--        this.visible = true;-->
<!--        this.model=val;-->
<!--        this.model.storageLocationCId=kid;-->
<!--        this.storageLocationList()-->
<!--      },-->
<!--      storageLocationList() {-->
<!--        this.storageLocations.length = 0;-->
<!--        getAction("/cable/storageLocation/list2", {storageLocationCId:  this.model.storageLocationCId}).then((res) => {-->
<!--          if (res.success) {-->
<!--            this.storageLocations = res.result-->
<!--            console.log("加载库位")-->
<!--            console.log(this.storageLocations)-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      materialsList(val) {-->
<!--        this.materials.length = 0;-->
<!--        getAction("/cable/inventory/insuranceLists", {storageLocationId:val}).then((res) => {-->
<!--          if (res.success) {-->
<!--            this.materials = res.result-->
<!--            console.log("加载物料")-->
<!--            console.log(this.materials)-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      handleOk () {-->
<!--        const that = this;-->
<!--        // 触发表单验证-->
<!--        this.form.validateFields((err, values) => {-->
<!--          if (!err) {-->
<!--            that.confirmLoading = true;-->
<!--            let httpurl = '';-->
<!--            let method = 'post';-->
<!--            httpurl+=this.url.edit;-->
<!--            let formData = Object.assign(this.model, values);-->
<!--            console.log("表单提交数据",formData)-->
<!--            console.log("表单提交数据",this.model)-->
<!--            httpAction(httpurl,formData,method).then((res)=>{-->
<!--              if(res.success){-->
<!--                that.$message.success(res.message);-->
<!--                that.$emit('ok');-->
<!--              }else{-->
<!--                that.$message.warning(res.message);-->
<!--              }-->
<!--            }).finally(() => {-->
<!--              that.confirmLoading = false;-->
<!--              that.close();-->
<!--            })-->
<!--          }-->

<!--        })-->
<!--      },-->
<!--      close () {-->
<!--        this.form=this.$form.createForm(this);-->
<!--        this.$emit('close');-->
<!--        this.visible = false;-->
<!--      },-->
<!--      handleCancel () {-->
<!--        this.close()-->
<!--      },-->


<!--    }-->
<!--  }-->
<!--</script>-->

<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭"  style="margin-top: 100px">
    <a-spin :spinning="confirmLoading">

      <a-spin :spinning="confirmLoading">
        <a-form :form="form" style="padding-left: 40px;margin-top: 15px">

          <a-form-item label="转移数量" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-input-number v-decorator="['amount', validatorRules.amount]" style="width: 475px" placeholder="请输入转移数量"></a-input-number>
          </a-form-item>

          <a-form-item label="存放仓库" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['warehouseId', validatorRules.warehouseId]" @change="warehouses" placeholder="请选择存放库位" >
              <template v-for="(warehouse,index) in warehouseLists">
                <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>

          <a-form-item label="存放库位" :labelCol="labelCol" :wrapperCol="wrapperCol">
            <a-select v-decorator="['storageLocationRId', validatorRules.storageLocationRId]" placeholder="请选择存放库位">
              <template v-for="(storageLocation,index) in storageLocations">
                <a-select-option v-bind:value="storageLocation.id">{{storageLocation.storageLocationName}}</a-select-option>
              </template>
            </a-select>
          </a-form-item>

        </a-form>
      </a-spin>

    </a-spin>
  </j-modal>
</template>

<script>
  import {httpAction, getAction} from '@/api/manage'
  import pick from 'lodash.pick'
  import JEllipsis from '@/components/jeecg/JEllipsis'


  export default {
    name: "MaterialStorageLocationModal",
    components: {
      JEllipsis
    },
    data () {
      return {
        form: this.$form.createForm(this),
        title:"操作",
        width:800,
        visible: false,
        model: {},
        confirmLoading: false,
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        validatorRules: {
          amount: { rules: [{ required: true, message: '请输入转移数量!' }] },
          warehouseId: { rules: [{ required: true, message: '请选择存放仓库' }] },
          storageLocationRId: { rules: [{ required: true, message: '请选择接收库位!' }] },
        },
        url: {
          edit: "/cable/storageLocation/MaterialStorageLocationEdit",
        },
        storageLocationId:'',
        warehouseId:'',
        storageLocations:[],
        materials:[],
        materialUnits:{},
        warehouseLists:{},
      }
    },
    created () {
      this.getMaterialUnitList();
      this.getWarehouseList();
    },
    methods: {
      warehouses(val){
        this.storageLocations.length=0;
        this.storageLocationList(val);
      },
      getWarehouseList() {
        getAction('/cable/warehouse/warehouseOneselfList').then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
          }
        })
      },
      getMaterialUnitList() {
        getAction('/sys/dictItem/getDictItemUnit').then((res) => {
          if (res.success) {
            this.materialUnits = res.result
          }
        })
      },
      yiDon(val,kid){
        this.visible = true;
        this.model=val;
        this.model.storageLocationCId=kid;
        this.storageLocationList()
      },
      storageLocationList(val) {
        this.storageLocations.length = 0;
        getAction("/cable/storageLocation/list2", {id: this.model.storageLocationCId,warehouseId:val}).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
            console.log("加载库位")
            console.log(this.storageLocations)
          }
        })
      },
      materialsList(val) {
        this.materials.length = 0;
        getAction("/cable/inventory/insuranceLists", {storageLocationId:val}).then((res) => {
          if (res.success) {
            this.materials = res.result
            console.log("加载物料")
            console.log(this.materials)
          }
        })
      },
      handleOk () {
        const that = this;
        // 触发表单验证
        this.form.validateFields((err, values) => {
          if (!err) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = 'post';
            httpurl+=this.url.edit;
            let formData = Object.assign(this.model, values);
            console.log("表单提交数据",formData)
            console.log("表单提交数据",this.model)
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
      close () {
        this.form=this.$form.createForm(this);
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },


    }
  }
</script>