<template>
  <div class="Jcommon-layout">
    <div class="Jcommon-layout-center">
      <el-row class="Jcommon-search-box" :gutter="16">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-col :span="6">
            <el-form-item label="合同名" prop="contractName">
              <el-input
                v-model="queryParams.contractName"
                placeholder="请输入合同名"
                maxlength="40"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="创建时间" prop="createTimeRange">
              <el-date-picker
                v-model="queryParams.createTimeRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                :default-time="['00:00:00', '23:59:59']"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="合同类型" prop="contractTypeArray">
              <el-cascader
                v-model="queryParams.contractTypeArray"
                :options="contractTypeOptions"
                :props="{
                  children: 'children',
                  value: 'id',
                  label: 'name',
                  checkStrictly: true,
                }"
                placeholder="请选择合同类型"
              ></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item>
              <el-button
                type="primary"
                icon="el-icon-search"
                size="mini"
                @click="handleQuery"
                >搜索</el-button
              >
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
                >重置</el-button
              >
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div class="Jcommon-layout-main Jflex-main">
        <div class="Jcommon-head">
          <el-row :gutter="10" class="mb8" style="width: 100%">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAdd"
                v-hasPermi="['contract:contractDraft:add']"
                >创建合同</el-button
              >
            </el-col>
            <right-toolbar
              :showSearch.sync="showSearch"
              @queryTable="getList"
            ></right-toolbar>
          </el-row>
        </div>

        <JTable
          v-loading="loading"
          :hasC="false"
          :hasNO="true"
          :data="contractDraftList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column label="主键" align="center" prop="id" v-if="false" />
          <el-table-column
            label="合同名称"
            align="center"
            prop="contractName"
            minWidth="180"
          />
          <el-table-column
            label="合同编号"
            align="center"
            prop="contractCode"
            minWidth="180"
          >
            <template slot-scope="scope">
                <span style="color:#ccc" v-if="!scope.row.contractCode">提交合同后生成</span>
                <span v-if="scope.row.contractCode">{{scope.row.contractCode}}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="合同类型"
            align="center"
            prop="contractTypeName"
            width="120"
          />
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            width="120"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
            </template>
          </el-table-column>
          <el-table-column label="草拟人" align="center" prop="drafterName" />
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            fixed="right"
            width="300"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="showApply(scope.row)"
                v-hasPermi="['contract:contractDraft:edit']"
                v-show="scope.row.coordinationStatus == '0'"
                >提交合同</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-download"
                @click="handleDownload(scope.row)"
                v-hasPermi="['contract:contractDraft:edit']"
                v-show="scope.row.coordinationStatus == '0'"
                >下载文件</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-folder-opened"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['contract:contractDraft:edit']"
                v-show="scope.row.coordinationStatus == '0'"
                >文件管理</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['contract:contractDraft:edit']"
                v-show="scope.row.coordinationStatus == '0'"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </JTable>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <!-- 添加或修改合同草拟对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="500px"
      append-to-body
      :close-on-click-modal="false"
      class="Jdialog Jdialog_center"
    >
          <div class="form-header-custom">
            <span class="form-header-title">基本信息</span>
          </div>
          &#160;
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="合同名称" prop="contractName">
              <el-input
                v-model="form.contractName"
                placeholder="请输入合同名称"
                maxlength="40"
                show-word-limit
                :readonly="!isAdd"
              />
            </el-form-item>
            <el-form-item label="合同类型" prop="contractTypeArray">
              <el-cascader
                v-model="form.contractTypeArray"
                :options="contractTypeOptions"
                :disabled="!isAdd"
                :props="{
                  children: 'children',
                  value: 'id',
                  label: 'name',
                  checkStrictly: true,
                }"
                @change="handleChange"
                style="width: 100%"
                ref="cascaderItem"
                placeholder="请选择合同类型"
              ></el-cascader>
              <el-input v-model="form.contractTypeName" v-if="false" />
            </el-form-item>
            
            <el-form-item label="关联合同" prop="relatedContractId">
              <el-input
                v-model="form.relatedContractName"
                placeholder="请选择关联合同"
                :readonly="true"
                show-word-limit
              >
               <el-button slot="append" type="primary" icon="el-icon-search" @click="selectContractDialogShow"></el-button>
              </el-input>
            </el-form-item>
           
            <el-form-item label="上传文件" prop="fileGroup">
              <FileUploadContract
                uploadModule="contract"
                :limit="200"
                :fileSize="70"
                :fileType="fileType"  
                :showFiles="false"
                style="width: 100%"
                @uploadSuccess="fileUploadSuccess($event)"
              />
            </el-form-item>

            <el-form-item label="" prop="">
              <el-table
                :data="selectedFiles"
                :show-header="false"
                style="width: 100%"
              >
                <el-table-column prop="name" label="文件名"> </el-table-column>
                <el-table-column label="文件类型" width="40">
                  <template slot-scope="scope">
                    <el-tag size="mini" v-if="scope.row.type == 0">正文</el-tag>
                    <el-tag
                      size="mini"
                      v-if="scope.row.type == 1"
                      type="success"
                      >附件</el-tag
                    >
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="110">
                  <template slot-scope="scope">
                    <el-button
                      :disabled="scope.row.type == 0 || selectedTemplate"
                      size="mini"
                      type="text"
                      @click="changeMainFile(scope.row)"
                      >设为正文</el-button
                    >
                    <el-button
                      size="mini"
                      type="text"
                      @click="deleteSelectedFile(scope)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
            <el-form-item label="模板文件" v-if="selectedTemplate">
              <ul class="el-upload-list">
                <li>
                  <el-col :span="20">
                    <span class="el-icon-document">
                      {{ selectedTemplate.mainFileName }}</span
                    >
                  </el-col>
                  <el-col :span="4">
                    <el-tag size="mini">正文</el-tag>
                  </el-col>
                </li>
                <li
                  v-for="(temp, index) in selectedTemplate.attachmentsArray"
                  :key="index"
                >
                  <el-col :span="20">
                    <span class="el-icon-document">
                      {{ temp.attachmentFileName }}
                    </span>
                  </el-col>
                  <el-col :span="4">
                    <el-tag size="mini" type="success">附件</el-tag>
                  </el-col>
                </li>
              </ul>
            </el-form-item>
          </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <apply-form
      ref="applyForm"
      v-if="showApplyForm"
      @close="showApplyForm = false"
      @refresh="refresh"
    />
    <doc-dialog ref="triggerDocDialog" />
    <new-version ref="newVersion" @reload="getList()"/>
    <selectContractDialog
        ref="selectContractDialog"
        @addContract="addRelatedContract"
        @deleteContract="unSelectedContract"
    />
  </div>
</template>

<script>
import { getContractTypeEnable } from "@/api/contract/contractType";
import orgSelect from "@/components/workflow/FormControls/OrgSelect";
import applyForm from "./apply.vue";
import docDialog from "./download.vue";
import newVersion from "./newVersion.vue"
import selectContractDialog from "./selectContractDialog.vue";
import {
  listContractDraft,
  getContractDraft,
  delContractDraft,
  addContractDraft,
  updateContractDraft,
} from "@/api/contract/contractDraft";
export default {
  dicts: ["CONTRACT_REVIEW_STATUS"],
  components: { orgSelect, applyForm, docDialog, newVersion, selectContractDialog },
  data() {
    return {
      contractCollaboratorsArray: [],
      // 合同类型
      contractTypeOptions: [],
      //文件类型
      //xlsx  doc  docx xls pdf png jpg jpeg
      fileType: ["doc", "docx", "wps", "xls", "xlsx", "pdf", "png", "jpg", "jpeg"],
      activeName: "first",
      contractType: [],
      templateListLoading: false,
      templateList: [],
      // 按钮loading
      buttonLoading: false,
      selectedFiles: [],
      selectedTemplate: {},
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 合同草拟表格数据
      contractDraftList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      isAdd: true,
      showApplyForm: false,
      //是否显示协同弹出层
      collaboratorsOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractName: undefined,
        contractType: undefined,
        contractTypeArray: undefined,
        contractCollaborators: undefined,
        createTimeRange:[],
      },
      // 表单参数
      form: {},
      coordinationForm: {},
      // 表单校验
      rules: {
        id: [{ required: true, message: "主键不能为空", trigger: "blur" }],
        contractName: [
          { required: true, message: "合同名称不能为空", trigger: "blur" },
        ],
        contractTypeArray: [
          { required: true, message: "合同类型不能为空", trigger: "change" },
        ],
        fileGroup: [
          { required: true, message: "合同文件不能为空", trigger: "change" },
        ],
      },
    };
  },
  created() {
    this.getList();
    this.getContractType();
  },
  methods: {
    //显示合同审批申请框
    showApply(row) {
      this.showApplyForm = true;
      this.$nextTick(() => {
        this.$refs.applyForm.init(row.id);
      });
    },
    // 更多操作
    handleCommand(command, row) {
      switch (command) {
        case "handleCheck":
          this.showApply(row);
          break;
        case "handleDownload":
          this.handleDownload(row);
          break;
        case "handleDelete":
          this.handleDelete(row);
          break;
        case "handleUpdateFile":
          this.handleUpdateFile(row);
          break;
        default:
          break;
      }
    },
    changeMainFile(data) {
      this.$confirm("确认要修改合同主文件吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //遍历文件
          for (let i = 0; i < this.selectedFiles.length; i++) {
            if (this.selectedFiles[i].uid === data.uid) {
              this.selectedFiles[i].type = "0";
            } else {
              this.selectedFiles[i].type = "1";
            }
          }
        })
        .catch(() => {});
    },
    deleteSelectedFile(scope) {
      this.$confirm("确认要删除 " + scope.row.name + "?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.selectedFiles.splice(scope.$index, 1);
      });
    },
    //文件上传回调
    fileUploadSuccess(file) {
      let data = {
        uid: file.uid,
        name: file.name,
        from: "upload",
        type: 1, //0正文，1附件
      };

      if (this.selectedFiles.length == 0 && this.selectedTemplate == undefined) {
        data.type = 0;
      }

      this.selectedFiles.push(data);
    },
    handleDownload(row) {
        this.$refs.triggerDocDialog.show(row.id, row.contractName);
    },
    handleChange(value) {
      const pathLabels =
        this.$refs["cascaderItem"].getCheckedNodes()[0].pathLabels;
      this.form.contractTypeName = pathLabels[pathLabels.length - 1];
      //清空选择的模板数据
      this.selectedTemplate = undefined;
      this.templateList = [];
    },
    changeTemplate(data) {
      if (data.selected) {
        this.templateList = this.templateList.map((x) => {
          x.borderColor = "#e6ebf5";
          x.selected = false;
          return x;
        });
        data.selected = false;
        data.borderColor = "#e6ebf5";
        this.selectedTemplate = undefined;
      } else {
        this.templateList = this.templateList.map((x) => {
          x.borderColor = "#e6ebf5";
          x.selected = false;
          return x;
        });
        data.selected = true;
        data.borderColor = "#1890ff";
        this.selectedTemplate = data;
      }

      //将主文件都改为附件
      for (let i = 0; i < this.selectedFiles.length; i++) {
        this.selectedFiles[i].type = "1";
      }
    },
    /** 查询合同类型 */
    getContractType() {
      getContractTypeEnable().then((response) => {
        this.contractTypeOptions = response.data;
      });
    },
    /** 查询合同草拟列表 */
    getList() {
      this.loading = true;
      listContractDraft(this.queryParams).then((response) => {
        this.contractDraftList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    coordinationFormCancel() {
      this.collaboratorsOpen = false;
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        contractName: undefined,
        contractType: undefined,
        relatedContractId: undefined,
        relatedContractName: undefined,
        relatedContractCode: undefined,
        relatedType: undefined,
        relatedTime: undefined,
        contractTemplateId: undefined,
        contractTemplateType: undefined,
        drafterName: undefined,
        drafterId: undefined,
        fileGroupsVersion: undefined,
        fileGroupsVersionName: undefined,
        currentReviewer: undefined,
        currentReviewerId: undefined,
        projectId: undefined,
        projectName: undefined,
        projectCode: undefined,
        fileGroupsId: undefined,
        coordinationStatus: "0",
        contractCollaborators: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenantId: undefined,
        contractTypeName: undefined,
        selectedFiles: [],
      };
      this.selectedFiles = [];
      this.selectedTemplate = undefined;
      this.templateList = [];
      this.resetForm("form");
    },
    // 表单重置
    resetCoordinationForm() {
      this.coordinationForm = {
        contractCollaboratorsArray: undefined,
      };
      this.resetForm("coordinationForm");
    },
    refresh() {
      this.showApplyForm = false;
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.contractType = JSON.stringify(
        this.queryParams.contractTypeArray
      );
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.contractType = undefined;
      this.queryParams.contractTypeArray = undefined;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.isAdd = true;
      this.title = "合同草拟";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getContractDraft(id).then((response) => {
        response.data.contractTypeArray = JSON.parse(
          response.data.contractType
        );
        this.selectedFiles = JSON.parse(response.data.fileGroup);
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.isAdd = false;
        this.title = "修改合同草拟";
      });
    },
    /**上传新版本 */
    handleUpdateFile(row) {
        this.$refs.newVersion.showDialog(row.id);
    },
    /** 提交按钮 */
    submitForm() {
      this.form.fileGroup = JSON.stringify(this.selectedFiles);
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          this.form.contractType = JSON.stringify(this.form.contractTypeArray);
          if(this.selectedTemplate != undefined) {
            this.form.contractTemplateId = this.selectedTemplate.id;
          } else {
            this.form.contractTemplateId = undefined;
          }
          this.form.fileGroup = JSON.stringify(this.selectedFiles);
          if (this.form.id != null) {
            updateContractDraft(this.form)
              .then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            addContractDraft(this.form)
              .then((response) => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let contractName = row.contractName;
      this.$modal
        .confirm('是否确认删除合同合同名称为"' + contractName + '"的数据项？')
        .then(() => {
          this.loading = true;
          return delContractDraft(ids);
        })
        .then(() => {
          this.loading = false;
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "contract/contractDraft/export",
        {
          ...this.queryParams,
        },
        `contractDraft_${new Date().getTime()}.xlsx`
      );
    },
    selectContractDialogShow() {
      this.$refs.selectContractDialog.show();
    },
    addRelatedContract(row) {
        this.form.relatedContractId = row.id;
        this.form.relatedContractName = row.contractName;
    },
    unSelectedContract() {
        this.form.relatedContractId = undefined;
        this.form.relatedContractName = undefined;
    }
  },
};
</script>
<style lang="scss" scoped>
.form-header-custom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #dcdfe6;
  .form-header-title {
    display: inline-block;
    padding: 10px 0;
    font-size: 16px;
    font-weight: 600;
  }
}

.template-number {
  padding: 10px;
  color: #909399;
}

.template-title {
  padding: 10px;
  font-size: 16px;
  line-height: 1.5;
}

.template-info {
  padding: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 12px;

  &.bg {
    background: #ebeef5;
  }

  .template-time {
    color: #909399;
  }
}
</style>
