<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="storageLocationList">
        <a-row :gutter="24">

          <a-col :md="4" :sm="5">
            <a-form-item label="仓库名称">
              <a-select v-model="queryParam.warehouseId" placeholder="请选择仓库名称" @change="storageLocationList">
                <template v-for="warehouse in warehouseLists">
                  <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>
                </template>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="5">
            <a-form-item label="库位名称">
              <a-input placeholder="请输入库位名称" v-model="queryParam.storageLocationName"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="4" :sm="5">
            <a-form-item label="库位容积">
              <a-input placeholder="请输入库位容积" v-model="queryParam.storageLocationVolume"></a-input>
            </a-form-item>
          </a-col>

          <a-col :md="5" :sm="7">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="storageLocationList" icon="search">查询</a-button>
              <a-button type="primary" @click="searchs" icon="reload" style="margin-left: 8px">重置</a-button>
              <a-button @click="handleAdd" type="primary" style="margin-left: 8px" icon="plus">新增库位</a-button>
            </span>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <div style="display: inline-block;margin-bottom: 50px;margin-left: 20px;">
            <span class="spa" style="background-color: white;border: 1px solid #777777">空闲</span>
            <span class="spa" style="background-color: dodgerblue;margin-left: 10px;color: white">正常</span>
            <span class="spa" style="background-color: orange;margin-left: 10px;color: white">饱和</span>
            <span class="spa" style="background-color: red;margin-left: 10px;color: white">超标</span>
          </div>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 库位展示区域 -->
    <div>
        <span v-for="(storageLocation,index) in storageLocations">
          <!-- 空闲库位容量 0 为空闲状态-->
          <span v-if="storageLocation.percentage == 0">
            <div :id="storageLocation.id" class="stordiv">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                 {{storageLocation.percentage + '%'}}
               </span>
             </div>
              <div class="yid">
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
              </div>
            <div class="aclick">
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>

          <!-- 正常库位容量 60 至 80 之间为正常状态-->
          <span v-if="storageLocation.percentage > 0 && storageLocation.percentage <= 60">
            <div :id="storageLocation.id" class="stordiv" style="background-color: dodgerblue">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                {{storageLocation.percentage + '%'}}
               </span>
             </div>
                            <div class="yid">
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
              </div>
            <div class="aclick">
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>

          <!-- 饱和库位容量 80 至 100 之间为饱和状态-->
          <span v-if="storageLocation.percentage > 60 && storageLocation.percentage <= 80">
            <div :id="storageLocation.id" class="stordiv" style="background-color: orange">
             <div class="stor">
               {{storageLocation.storageLocationName}}
               <span class="spaa">
                 {{storageLocation.percentage + '%'}}
               </span>
             </div>
                            <div class="yid">
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
              </div>
            <div class="aclick">
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>

          <!-- 满仓库位容量 大于或等于100为满仓状态-->
          <span v-if=" storageLocation.percentage > 80">
            <div :id="storageLocation.id" class="stordiv" style="background-color: red">
             <div class="stor">
                {{storageLocation.storageLocationName}}
               <span class="spaa">
                 {{storageLocation.percentage + '%'}}
               </span>
             </div>
                            <div class="yid">
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
              </div>
            <div class="aclick">
              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>
              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">
                  <a class="a2">删</a>
              </a-popconfirm>
            </div>
          </div>
          </span>

        </span>
    </div>
    <!-- 库位展示区域-END -->

    <storageLocation-modal ref="storageLocationModal" @ok="modalFormOk"></storageLocation-modal>
    <storageLocationAdd-modal ref="storageLocationAddModal" @ok="modalFormOk"></storageLocationAdd-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StorageLocationModal from './modules/StorageLocationModal'
  import StorageLocationAddModal from './modules/StorageLocationAddModal'
  import { deleteAction, getAction } from '@/api/manage'
  import JInput from '@/components/jeecg/JInput'

  export default {
    name: 'StorageLocationList',
    mixins: [JeecgListMixin, mixinDevice],
    components: {
      StorageLocationModal,
      StorageLocationAddModal,
      JInput
    },
    data() {
      return {
        description: '库位表管理页面',
        //查询条件
        queryParam: {
          storageLocationName: '',
          storageLocationVolume: ''
        },
        //保存自家仓库
        warehouseLists: {},
        //保存查询出来的库位
        storageLocations: [],
        dictOptions: {},
        url: {
          list: '/cable/storageLocation/list',
          delete: '/cable/storageLocation/delete',
          deleteBatch: '/cable/storageLocation/deleteBatch',
          exportXlsUrl: '/cable/storageLocation/exportXls',
          importExcelUrl: 'cable/storageLocation/importExcel',
          warehouseOneselfList: '/cable/warehouse/warehouseOneselfList'
        },
        wId: ''
      }
    },
    computed: {
      importExcelUrl: function() {
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
      }
    },
    methods: {
      modalFormOk() {
        this.searchs()
      },
      handleAdd() {
        let wa = this.queryParam.warehouseId
        console.log(wa)
        if (wa != undefined) {
          this.$refs.storageLocationAddModal.addStorageLocation(wa)
          this.$refs.storageLocationAddModal.title = ''
        } else {
          window.confirm('请先选择一个仓库!')
        }
      },
      searchs() {
        this.queryParam.storageLocationName = ''
        this.queryParam.storageLocationVolume = ''
        this.storageLocationList()
      },
      hDelete(val) {
        let that = this
        deleteAction(this.url.delete, { id: val }).then((res) => {
          if (res.success) {
            that.storageLocationList()
          } else {
            window.confirm('该库位存放了东西，不能删除!')
          }
        })
      },
      viewDetails(val) {
        let wa = this.queryParam.warehouseId
        console.log(wa)
        this.$refs.storageLocationModal.showStorageLocation(wa, val)
        this.$refs.storageLocationModal.title = ''
      },
      initDictConfig() {
      },
      warehouseList() {
        this.warehouseLists = ''
        getAction(this.url.warehouseOneselfList).then((res) => {
          if (res.success) {
            this.warehouseLists = res.result
            this.queryParam.warehouseId = this.warehouseLists[0].id
            this.storageLocationList()
          }
        })
      },
      storageLocationList() {
        this.wId = this.queryParam.warehouseId
        this.storageLocations = ''
        getAction(this.url.list, {
          warehouseId: this.queryParam.warehouseId,
          storageLocationName: this.queryParam.storageLocationName,
          storageLocationVolume: this.queryParam.storageLocationVolume
        }).then((res) => {
          if (res.success) {
            this.storageLocations = res.result
          }
        })
      }
    },
    created() {
      this.warehouseList()
      this.storageLocationList()
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';

  .stordiv {
    width: 300px;
    height: 100px;
    margin: 5px;
    display: inline-block;
    border: 1px solid #777777;
    border-radius: 5px;
  }

  .stor {
    display: inline-block;
    width: 100%;
    padding-top: 10px;
    padding-left: 10px;
  }

  .spa {
    display: inline-block;
    width: 80px;
    height: 33px;
    line-height: 33px;
    text-align: center;
    border-radius: 5px
  }

  .aclick {
    float: right;
    margin-top: 20px;
  }

  .a1 {
    display: inline-block;
    color: white;
    width: 120px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    border-radius: 5px;
    background-color: darkcyan;
  }

  .a2 {
    display: inline-block;
    color: white;
    width: 30px;
    height: 30px;
    line-height: 30px;
    background-color: orange;
    text-align: center;
    margin-left: 5px;
    border-radius: 5px;
    margin-right: 10px;
  }

  .spaa {
    margin-right: 30px;
    float: right;
    display: inline-block;
    font-size: 16px;
  }

  .yid {
    display: inline-block;
    float: left;
  }

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

<!--<template>-->
<!--  <a-card :bordered="false">-->
<!--    &lt;!&ndash; 查询区域 &ndash;&gt;-->
<!--    <div class="table-page-search-wrapper">-->
<!--      <a-form layout="inline" @keyup.enter.native="searchQuery">-->
<!--        <a-row :gutter="24">-->

<!--          <a-col :md="4" :sm="7">-->
<!--            <a-form-item label="仓库名称">-->
<!--              <a-select v-model="queryParam.warehouseId" placeholder="请选择仓库名称" @change="storageLocationList">-->
<!--                <template v-for="warehouse in warehouseLists">-->
<!--                  <a-select-option v-bind:value="warehouse.id">{{warehouse.name}}</a-select-option>-->
<!--                </template>-->
<!--              </a-select>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="4" :sm="7">-->
<!--            <a-form-item label="库位名称">-->
<!--              <j-input placeholder="请输入库位名称" v-model="queryParam.storageLocationName"></j-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="4" :sm="7">-->
<!--            <a-form-item label="库位容积">-->
<!--              <a-input placeholder="请输入库位容积" v-model="queryParam.storageLocationVolume"></a-input>-->
<!--            </a-form-item>-->
<!--          </a-col>-->

<!--          <a-col :md="5" :sm="10">-->
<!--            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">-->
<!--              <a-button type="primary" @click="storageLocationList" icon="search">查询</a-button>-->
<!--              <a-button type="primary" @click="searchs" icon="reload" style="margin-left: 8px">重置</a-button>-->
<!--              <a-button @click="handleAdd" type="primary" style="margin-left: 8px" icon="plus">新增库位</a-button>-->
<!--            </span>-->
<!--          </a-col>-->
<!--          <div style="display: inline-block;width: 350px">-->
<!--            <span class="spa" style="background-color: white;border: 1px solid #777777">空闲</span>-->
<!--            <span class="spa" style="background-color: dodgerblue;margin-left: 10px;color: white">正常</span>-->
<!--            <span class="spa" style="background-color: orange;margin-left: 10px;color: white">饱和</span>-->
<!--            <span class="spa" style="background-color: red;margin-left: 10px;color: white">超标</span>-->
<!--          </div>-->
<!--        </a-row>-->
<!--      </a-form>-->
<!--    </div>-->
<!--    &lt;!&ndash; 查询区域-END &ndash;&gt;-->

<!--    &lt;!&ndash; 库位展示区域 &ndash;&gt;-->
<!--    <div>-->
<!--        <span v-for="(storageLocation,index) in storageLocations">-->

<!--           &lt;!&ndash; 空闲库位容量 0 为空闲状态&ndash;&gt;-->
<!--          <span v-if="storageLocation.percentage == 0">-->
<!--            <div :id="storageLocation.id" class="stordiv">-->
<!--             <div class="stor">-->
<!--                {{storageLocation.storageLocationName}}-->
<!--               <span class="spaa">-->
<!--                 {{storageLocation.percentage + '%'}}-->
<!--               </span>-->
<!--             </div>-->
<!--              <div class="yid">-->
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
<!--              </div>-->
<!--            <div class="aclick">-->
<!--              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>-->
<!--              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">-->
<!--                  <a class="a2">删</a>-->
<!--              </a-popconfirm>-->
<!--            </div>-->
<!--          </div>-->
<!--          </span>-->

<!--          &lt;!&ndash; 正常库位容量 0 至 60 之间为正常状态&ndash;&gt;-->
<!--          <span v-if="storageLocation.percentage > 0 && storageLocation.percentage <= 60">-->
<!--            <div :id="storageLocation.id" class="stordiv" style="background-color: dodgerblue">-->
<!--             <div class="stor">-->
<!--                {{storageLocation.storageLocationName}}-->
<!--               <span class="spaa">-->
<!--                {{storageLocation.percentage + '%'}}-->
<!--               </span>-->
<!--             </div>-->
<!--                            <div class="yid">-->
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
<!--              </div>-->
<!--            <div class="aclick">-->
<!--              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>-->
<!--              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">-->
<!--                  <a class="a2">删</a>-->
<!--              </a-popconfirm>-->
<!--            </div>-->
<!--          </div>-->
<!--          </span>-->

<!--          &lt;!&ndash; 饱和库位容量 60 至 80 之间为饱和状态&ndash;&gt;-->
<!--          <span v-if="storageLocation.percentage > 60 && storageLocation.percentage <= 80">-->
<!--            <div :id="storageLocation.id" class="stordiv" style="background-color: orange">-->
<!--             <div class="stor">-->
<!--               {{storageLocation.storageLocationName}}-->
<!--               <span class="spaa">-->
<!--                 {{storageLocation.percentage + '%'}}-->
<!--               </span>-->
<!--             </div>-->
<!--                            <div class="yid">-->
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
<!--              </div>-->
<!--            <div class="aclick">-->
<!--              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>-->
<!--              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">-->
<!--                  <a class="a2">删</a>-->
<!--              </a-popconfirm>-->
<!--            </div>-->
<!--          </div>-->
<!--          </span>-->

<!--          &lt;!&ndash; 满仓库位容量 大于80为满仓状态&ndash;&gt;-->
<!--          <span v-if="storageLocation.percentage > 80">-->
<!--            <div :id="storageLocation.id" class="stordiv" style="background-color: red">-->
<!--             <div class="stor">-->
<!--                {{storageLocation.storageLocationName}}-->
<!--               <span class="spaa">-->
<!--                 {{storageLocation.percentage + '%'}}-->
<!--               </span>-->
<!--             </div>-->
<!--                            <div class="yid">-->
<!--                <a class="yida" @click="yiDon(storageLocation.id)">移动物料</a>-->
<!--              </div>-->
<!--            <div class="aclick">-->
<!--              <a class="a1" @click="viewDetails(storageLocation.id)">查看存储物料</a>-->
<!--              <a-popconfirm title="确定删除吗?" @confirm="() => hDelete(storageLocation.id)">-->
<!--                  <a class="a2">删</a>-->
<!--              </a-popconfirm>-->
<!--            </div>-->
<!--          </div>-->
<!--          </span>-->

<!--        </span>-->
<!--    </div>-->
<!--    &lt;!&ndash; 库位展示区域-END &ndash;&gt;-->

<!--    <storageLocation-modal ref="storageLocationModal"></storageLocation-modal>-->
<!--    <storageLocationAdd-modal ref="storageLocationAddModal" @ok="modalFormOk"></storageLocationAdd-modal>-->
<!--    <material-storage-location-modal ref="materialStorageLocationModal" @ok="modalFormOk"></material-storage-location-modal>-->
<!--  </a-card>-->
<!--</template>-->

<!--<script>-->

<!--  import '@/assets/less/TableExpand.less'-->
<!--  import { mixinDevice } from '@/utils/mixin'-->
<!--  import { JeecgListMixin } from '@/mixins/JeecgListMixin'-->
<!--  import StorageLocationModal from './modules/StorageLocationModal'-->
<!--  import StorageLocationAddModal from './modules/StorageLocationAddModal'-->
<!--  import { deleteAction, getAction } from '@/api/manage'-->
<!--  import JInput from '@/components/jeecg/JInput'-->
<!--  import MaterialStorageLocationModal from './modules/MaterialStorageLocationModal'-->

<!--  export default {-->
<!--    name: 'StorageLocationList',-->
<!--    mixins: [JeecgListMixin, mixinDevice],-->
<!--    components: {-->
<!--      MaterialStorageLocationModal,-->
<!--      StorageLocationModal,-->
<!--      StorageLocationAddModal,-->
<!--      JInput-->
<!--    },-->
<!--    data() {-->
<!--      return {-->
<!--        description: '库位表管理页面',-->
<!--        //查询条件-->
<!--        queryParam: {-->
<!--          storageLocationName: '',-->
<!--          storageLocationVolume: ''-->
<!--        },-->
<!--        //保存自家仓库-->
<!--        warehouseLists: {},-->
<!--        //保存查询出来的库位-->
<!--        storageLocations: [],-->
<!--        dictOptions: {},-->
<!--        url: {-->
<!--          list: '/cable/storageLocation/list',-->
<!--          delete: '/cable/storageLocation/delete',-->
<!--          deleteBatch: '/cable/storageLocation/deleteBatch',-->
<!--          exportXlsUrl: '/cable/storageLocation/exportXls',-->
<!--          importExcelUrl: 'cable/storageLocation/importExcel',-->
<!--          warehouseOneselfList: '/cable/warehouse/warehouseOneselfList'-->
<!--        },-->
<!--        wId:'',-->
<!--      }-->
<!--    },-->
<!--    computed: {-->
<!--      importExcelUrl: function() {-->
<!--        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`-->
<!--      }-->
<!--    },-->
<!--    methods: {-->
<!--      yiDon(val) {-->
<!--        console.log(val)-->
<!--        console.log(this.wId)-->
<!--        this.$refs.materialStorageLocationModal.yiDon(val,this.wId)-->
<!--        this.$refs.materialStorageLocationModal.title = ''-->
<!--      },-->
<!--      modalFormOk() {-->
<!--        console.log('加载')-->
<!--        this.searchs()-->
<!--      },-->
<!--      handleAdd() {-->
<!--        console.log('新增库位')-->
<!--        let wa = this.queryParam.warehouseId-->
<!--        console.log(wa)-->
<!--        if (wa != undefined) {-->
<!--          this.$refs.storageLocationAddModal.addStorageLocation(wa)-->
<!--          this.$refs.storageLocationAddModal.title = ''-->
<!--        } else {-->
<!--          window.confirm('请先选择一个仓库!')-->
<!--        }-->
<!--      },-->
<!--      searchs() {-->
<!--        this.queryParam.storageLocationName = ''-->
<!--        this.queryParam.storageLocationVolume = ''-->
<!--        this.storageLocationList()-->
<!--      },-->
<!--      loadData(arg) {-->
<!--        if (!this.url.list) {-->
<!--          this.$message.error('请设置url.list属性!')-->
<!--          return-->
<!--        }-->
<!--        //加载数据 若传入参数1则加载第一页的内容-->
<!--        if (arg === 1) {-->
<!--          this.ipagination.current = 1-->
<!--        }-->
<!--        var params = this.getQueryParams()//查询条件-->
<!--        this.loading = true-->
<!--        getAction(this.url.list, params).then((res) => {-->
<!--          if (res.success) {-->
<!--            this.dataSource = res.result.records-->
<!--            this.ipagination.total = res.result.total-->
<!--          }-->
<!--          if (res.code === 510) {-->
<!--            this.$message.warning(res.message)-->
<!--          }-->
<!--          this.loading = false-->
<!--        })-->
<!--      },-->
<!--      hDelete(val) {-->
<!--        let that = this-->
<!--        deleteAction(this.url.delete, { id: val }).then((res) => {-->
<!--          if (res.success) {-->
<!--            that.storageLocationList()-->
<!--          } else {-->
<!--            window.confirm('该库位存放了东西，不能删除!')-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      viewDetails(val) {-->
<!--        console.log('查看存储物料')-->
<!--        console.log(val)-->
<!--        let wa = this.queryParam.warehouseId-->
<!--        console.log(wa)-->
<!--        this.$refs.storageLocationModal.showStorageLocation(wa, val)-->
<!--        this.$refs.storageLocationModal.title = ''-->
<!--      },-->
<!--      initDictConfig() {-->
<!--      },-->
<!--      warehouseList() {-->
<!--        this.warehouseLists = ''-->
<!--        getAction(this.url.warehouseOneselfList).then((res) => {-->
<!--          if (res.success) {-->
<!--            this.warehouseLists = res.result-->
<!--            this.queryParam.warehouseId = this.warehouseLists[0].id-->
<!--            this.storageLocationList()-->
<!--          }-->
<!--        })-->
<!--      },-->
<!--      storageLocationList() {-->
<!--        this.wId = this.queryParam.warehouseId;-->
<!--        this.storageLocations = ''-->
<!--        getAction(this.url.list, {-->
<!--          warehouseId: this.queryParam.warehouseId,-->
<!--          storageLocationName: this.queryParam.storageLocationName,-->
<!--          storageLocationVolume: this.queryParam.storageLocationVolume-->
<!--        }).then((res) => {-->
<!--          if (res.success) {-->
<!--            this.storageLocations = res.result-->
<!--            console.log(this.storageLocations)-->
<!--          }-->
<!--        })-->
<!--      }-->
<!--    },-->
<!--    created() {-->
<!--      this.warehouseList()-->
<!--      this.storageLocationList()-->
<!--    }-->
<!--  }-->
<!--</script>-->
<!--<style scoped>-->
<!--  @import '~@assets/less/common.less';-->

<!--  .stordiv {-->
<!--    width: 300px;-->
<!--    height: 100px;-->
<!--    margin: 5px;-->
<!--    display: inline-block;-->
<!--    border: 1px solid #777777;-->
<!--    border-radius: 5px;-->
<!--  }-->

<!--  .stor {-->
<!--    display: inline-block;-->
<!--    width: 100%;-->
<!--    padding-top: 10px;-->
<!--    padding-left: 10px;-->
<!--  }-->

<!--  .spa {-->
<!--    display: inline-block;-->
<!--    width: 80px;-->
<!--    height: 33px;-->
<!--    line-height: 33px;-->
<!--    text-align: center;-->
<!--    border-radius: 5px-->
<!--  }-->

<!--  .aclick {-->
<!--    float: right;-->
<!--    margin-top: 20px;-->
<!--  }-->

<!--  .a1 {-->
<!--    display: inline-block;-->
<!--    color: white;-->
<!--    width: 120px;-->
<!--    height: 30px;-->
<!--    line-height: 30px;-->
<!--    text-align: center;-->
<!--    border-radius: 5px;-->
<!--    background-color: darkcyan;-->
<!--  }-->

<!--  .a2 {-->
<!--    display: inline-block;-->
<!--    color: white;-->
<!--    width: 30px;-->
<!--    height: 30px;-->
<!--    line-height: 30px;-->
<!--    background-color: orange;-->
<!--    text-align: center;-->
<!--    margin-left: 5px;-->
<!--    border-radius: 5px;-->
<!--    margin-right: 10px;-->
<!--  }-->

<!--  .spaa {-->
<!--    margin-right: 30px;-->
<!--    float: right;-->
<!--    display: inline-block;-->
<!--    font-size: 16px;-->
<!--  }-->

<!--  .yid {-->
<!--    display: inline-block;-->
<!--    float: left;-->
<!--  }-->

<!--  .yida {-->
<!--    display: inline-block;-->
<!--    margin-top: 20px;-->
<!--    margin-left: 15px;-->
<!--    width: 80px;-->
<!--    height: 30px;-->
<!--    line-height: 30px;-->
<!--    text-align: center;-->
<!--    border-radius: 5px;-->
<!--    background-color: #afafaf;-->
<!--    color: white;-->
<!--  }-->
<!--</style>-->