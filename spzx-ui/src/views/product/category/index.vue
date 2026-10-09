<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Upload"
            @click="handleImport"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="warning"
            plain
            icon="Download"
            @click="handleExport"
        >导出</el-button>
      </el-col>
      <right-toolbar></right-toolbar>
    </el-row>

  <!--
  lazy : 表示懒加载的意思。
  :load="fetchData"  表示执行哪个函数进行加载数据
  :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    属性配置
    children  用于获取当前分类的孩子分类集合数据
    hasChildren 表示当前分类是否含有孩子，有孩子会在分类名称前显示 > 箭头，可以点击箭头查询孩子集合。
    冒号前属性名称是element-plus组件里定义的名称，固定不能写错(查官方文档)。冒号后的属性名称一般是后端返回的JSON数据的属性名称(Bean对象属性名称)。
  -->
    <el-table
        :data="categoryList"
        style="width: 100%"
        row-key="id"
        border
        lazy
        :load="fetchData"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="imageUrl" label="图标" #default="scope">
        <img :src="scope.row.imageUrl" width="50" />
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" />
      <el-table-column prop="status" label="状态" #default="scope">
        {{ scope.row.status == 1 ? '正常' : '停用' }}
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" />
    </el-table>



    <el-dialog :title="upload.title" v-model="upload.open" width="400px" append-to-body>
      <el-upload
          ref="uploadRef"
          :limit="1"
          accept=".xlsx, .xls"
          :headers="upload.headers"
          :action="upload.url + '?updateSupport=' + upload.updateSupport"
          :disabled="upload.isUploading"
          :on-progress="handleFileUploadProgress"
          :on-success="handleFileSuccess"
          :auto-upload="false"
          drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的用户数据
            </div>
            <span>仅允许导入xls、xlsx格式文件。</span>
            <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitFileForm">确 定</el-button>
          <el-button @click="upload.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="Category">
import { getTreeSelect } from '@/api/product/category.js'
import { getToken } from "@/utils/auth.js";

//页面初始数据
const categoryList = ref([]);

const data = reactive({
  queryParams: {

  },
  upload: {
    title: '导入分类数据',
    open: false,
    headers: {
      Authorization: 'Bearer ' + getToken() //从Cookie中获取JWT令牌，挂在请求头上。
    },
    url: import.meta.env.VITE_APP_BASE_API + "/product/category/import",
    updateSupport: false,   //业务逻辑中：用于判断    数据库数据是否支持更新操作。
    isUploading: false   //文件是否正在上传。
  }
})

const { queryParams,upload } = toRefs(data)

const {proxy} = getCurrentInstance()

//================================================
function  getList(id){
  getTreeSelect(id).then(resultJson=>{
    categoryList.value = resultJson.data
  })

}

//页面初始，执行函数取数据，回显数据
getList(0)


//数据列表
// function fetchData(row, treeNode, resolve){}
// Promise.reject()   用于保存异步失败结果
// Promise.resolve()  用于保存异步成功结果
const fetchData = async (row, treeNode, resolve) => {
  // console.log(row)
  // console.log(treeNode)
  // 向后端发送请求获取数据
  const {code,msg,data} = await getTreeSelect(row.id)  // 有await必须有async存在； 反之，有async存在，可以没有await

  // 返回数据
  resolve(data)
}

//导出
function handleExport(){
  //在main.js中进行全局声明，来自utils/request.js模块
  proxy.download("product/category/export",{
    //作为查询条件使用。
    ...queryParams.value, //展开运算符,将对象所有属性全都拷贝过来。相当于克隆操作。
  }, `category_${new Date().getTime()}.xlsx`);
}

//导入按钮事件处理，打开对话框
function handleImport(){
  upload.value.open = true
}

//上传进度条
function handleFileUploadProgress(event, file, fileList){
  upload.value.isUploading = true //正在上传中
}

//文件上传成功后执行的回调处理
function handleFileSuccess(response, file, fileList){
  upload.value.isUploading = false
  proxy.$refs["uploadRef"].handleRemove(file);
  upload.value.open = false
  proxy.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
  getList(0)
}

//下载模板
function importTemplate(){
  //后端暂时没有提供下载模块的接口，就用真正下载功能提供模板 TODO。
  proxy.download("product/category/export",{
    //作为查询条件使用。
    ...queryParams.value, //展开运算符,将对象所有属性全都拷贝过来。相当于克隆操作。
  }, `templete_${new Date().getTime()}.xlsx`);
}

//提交文件上传
function submitFileForm(){
  proxy.$refs["uploadRef"].submit()
}

</script>