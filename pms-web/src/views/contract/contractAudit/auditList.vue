<template>
  <div class="Jcommon-layout-center">
    <el-row class="Jcommon-search-box" :gutter="16">
      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-col :span="6" style="height: 48px">
          <el-form-item label="合同编号" prop="contractCode">
            <el-input v-model="queryParams.contractCode" placeholder="请输入合同编号" maxlength="40" />
          </el-form-item>
        </el-col>
        <el-col :span="6" style="height: 48px">
          <el-form-item label="合同名" prop="contractName">
            <el-input v-model="queryParams.contractName" placeholder="请输入合同名" maxlength="40" />
          </el-form-item>
        </el-col>
        <el-col :span="6" style="height: 48px">
          <el-form-item label="提交合同时间" prop="createTimeRange" label-width="120">
            <el-date-picker v-model="queryParams.createTimeRange" type="daterange" range-separator="至"
              start-placeholder="开始日期" end-placeholder="结束日期" value-format="timestamp"
              :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="6" style="height: 48px">
          <el-form-item label="合同类型" prop="contractType">
            <el-cascader v-model="queryParams.contractTypeArray" :options="contractTypeOptions" :props="{
              children: 'children',
              value: 'id',
              label: 'name',
              checkStrictly: true,
            }" placeholder="请选择合同类型"></el-cascader>
          </el-form-item>
        </el-col>
        <el-col :span="6" style="height: 48px">
          <el-form-item label="相对方" prop="partnerNames">
            <el-input v-model="queryParams.partnerNames" placeholder="请输入相对方名" maxlength="40" />
          </el-form-item>
        </el-col>
        <el-col :span="6" style="height: 48px">
          <el-form-item label="草拟人名" prop="drafterName">
            <el-input v-model="queryParams.drafterName" placeholder="请输入草拟人名" maxlength="40" />
          </el-form-item>
        </el-col>
        <el-col :span="6" style="height: 48px">
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
    <div class="Jcommon-layout-main Jflex-main">
      <JTable v-loading="loading" :has-c="false" :has-n-o="true" :data="contractAuditList"
        @selection-change="handleSelectionChange" :row-class-name="tableRowClassName">
        <el-table-column v-if="false" label="主键" align="center" prop="id" />
        <el-table-column label="合同名称" align="center" prop="contractName" min-width="180px" class-name="warn-text">
          <template slot-scope="scope">
            {{ scope.row.contractName }}
          </template>
        </el-table-column>
        <el-table-column label="创建人" align="center" prop="drafterName" min-width="120px" />
        <el-table-column label="合同状态" align="center" prop="contractStatus">
          <template slot-scope="scope">
            <el-tag v-if="
              scope.row.contractStatus == '3' &&
              scope.row.contractApproveStatus == '1'
            " type="primary">
              审批中
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '3' &&
              scope.row.contractApproveStatus == '3'
            " type="danger">
              审批不通过
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '3' &&
              scope.row.contractApproveStatus == '2'
            " type="success">
              审批通过
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '4' &&
              scope.row.contractStampedStatus == '1'
            " type="primary">
              盖章中
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '4' &&
              scope.row.contractStampedStatus == '2'
            " type="success">
              已盖章
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '4' &&
              scope.row.contractStampedStatus == '3'
            " type="danger">
              拒绝盖章
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '5' &&
              scope.row.contractReviewStatus == '4'
            " type="primary">
              待复核
            </el-tag>
            <el-tag v-if="
              scope.row.contractStatus == '5' &&
              scope.row.contractReviewStatus == '5'
            " type="danger">
              复核失败
            </el-tag>
            <el-tag
                type="success"
                v-if="
                  scope.row.contractStatus == '6'
                "
                >履约中</el-tag
              >
              <el-tag
                type="primary"
                v-if="scope.row.contractStatus == '7'"
                >中止</el-tag
              >
              <el-tag
                type="warn"
                v-if="scope.row.contractStatus == '8'"
                >已结束</el-tag
              >
          </template>
        </el-table-column>
        <el-table-column label="我方主体" align="center" prop="subjectNames" min-width="200px" />

        <el-table-column label="合作方" align="center" prop="partnerNames" min-width="200px" />

        <el-table-column label="合同类型" align="center" prop="contractTypeName" min-width="120px" />

        <el-table-column label="合同编号" align="center" prop="contractCode" min-width="180px">
          <template slot-scope="scope">
            <span v-if="!scope.row.contractCode" style="color: #ccc">合同审批通过后生成</span>
            <span v-if="scope.row.contractCode">{{
              scope.row.contractCode
            }}</span>
          </template>
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="320">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-search" @click="showContract(scope.row)">查看合同</el-button>

            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">作废</el-button>
            <el-button v-hasPermi="['contract:contractDraft:edit']" size="mini" type="text" icon="el-icon-download"
              @click="handleDownload(scope.row)">下载文档</el-button>
          </template>
        </el-table-column>
      </JTable>

      <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
        @pagination="getList" />
    </div>

    <contractInfo-dialog ref="triggerContractDialog" />

    <doc-dialog ref="triggerDocDialog" />
    <trace ref="trace" />
    <contract-cancel ref="contractCancel" @refresh="refresh" />
    <!-- 双章上传-->
    <uploadSealDialog ref="uploadSealDialog" @close="closeForm" />
  </div>
</template>

<script>
import { getContractType } from "@/api/contract/contractType";
import docDialog from "../contractDraft/download.vue";
import orgSelect from "@/components/workflow/FormControls/";
import request from "@/utils/request";
import {
  listContractAudit,
  auditSuccess,
  auditFail,
  backToDraft,
  getSeal,
  applyStamp,
  deptTreeSelect,
  uploadSealContract,
  getRemarks,
  addRemark,
  deleteRemark,
  notify,
} from "@/api/contract/contractAudit";
import { delContractDraft } from "@/api/contract/contractDraft";
import contractInfoDialog from "@/views/contract/ContractInfoDialog/index.vue";
import contractCancel from "@/views/contract/contractAudit/contractCancel.vue";
import trace from "./trace.vue";
import uploadSealDialog from "@/views/contract/contractAudit/uploadSealFile";
export default {
  dicts: ["CONTRACT_REVIEW_STATUS", "SEAL_TYPE"],
  components: {
    orgSelect,
    contractInfoDialog,
    docDialog,
    trace,
    contractCancel,
    uploadSealDialog,
  },
  data() {
    return {
      defaultProps: {
        value: "id",
        label: "name",
        disabled: "disabled",
      },
      // 部门树
      deptPersonOptions: undefined,
      contractCollaboratorsArray: [],
      // 合同类型
      contractTypeOptions: [],
      // 文件类型
      fileType: ["pdf"],
      contractType: [],
      // 按钮loading
      buttonLoading: false,
      selectedFiles: [],
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
      contractAuditList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层
      sealOpen: false,
      remarksOpen: false,
      sealContractOpen: false,
      openId: undefined,
      auditId: undefined,
      sealList: [],
      remarksList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractCode: undefined,
        contractName: undefined,
        contractType: undefined,
        contractCollaborators: undefined,
        partnerNames: undefined,
        drafterName: undefined,
      },
      remakForm: {},
      // 表单校验
      rules: {
        contractChargeId: [
          { required: true, message: "合同负责人不能为空", trigger: "blur" },
        ],
        contractEffectiveTime: [
          { required: true, message: "生效时间不能为空", trigger: "blur" },
        ],
        contractEndTime: [
          { required: true, message: "截止时间不能为空", trigger: "blur" },
        ],
        remarks: [{ required: true, message: "备注不能为空", trigger: "blur" }],
      },
      busId: "",
    };
  },
  computed: {
    drafterTaskName() {
      return this.$store.state.settings.drafterTask;
    },
  },
  created() {
    //带有busId的，刷新列表之后，直接打开busId的数据
    this.busId = this.$route.query.busId;
    this.getList();
    this.getContractType();
  },
  methods: {
    showDraftList(row) {
      //通知刷新待办
      this.$EventBus.$emit("updateTipCount");
      this.$router.push({
        path: "/contract/contractDraft",
        query: { openId: row.id },
      });
    },
    checkUserIsCreator(row) {
      if (row.drafterId == this.$store.state.user.userInfo.userId) {
        return true;
      } else {
        return false;
      }
    },
    toEditPage() {
      this.$router.push({ path: "/contract/revise" });
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.contractStatus == "4" && row.contractStampedStatus == "2") {
        return "warning-row";
      } else if (row.contractStatus == "5" && row.contractReviewStatus == "5") {
        return "warning-row";
      } else {
        return "";
      }
    },
    showContract(row) {
      this.$refs.triggerContractDialog.show(row.id);
    },
    /** 查询合同类型 */
    getContractType() {
      getContractType().then((response) => {
        this.contractTypeOptions = response.data;
      });
    },
    /** 查询合同草拟列表 */
    getList() {
      this.loading = true;
      listContractAudit(this.queryParams).then((response) => {
        this.contractAuditList = response.rows;
        this.total = response.total;
        this.loading = false;
        //判断是否有busId，并且列表有有匹配的数据，则打开该数据
        if (this.busId) {
          this.contractAuditList.forEach((item) => {
            if (item.id == this.busId) {
              this.busId = "";
              this.updateRemarks(item);
            }
          });
        }
      });
    },
    handleDelete(row) {
      const id = row.id;
      this.$modal
        .confirm('是否确认作废合同"' + row.contractName + '"？')
        .then(() => {
          this.loading = true;
          return delContractDraft(id);
        })
        .then(() => {
          this.loading = false;
          this.getList();
          this.$modal.msgSuccess("作废成功");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    deleteRemark(row) {
      const id = row.id;
      this.$modal
        .confirm('是否确认删除该备注"？')
        .then(() => {
          this.loading = true;
          return deleteRemark(id);
        })
        .then(() => {
          this.loading = false;
          getRemarks(this.openId).then((response) => {
            this.remarksList = response.data;
          });
          this.$modal.msgSuccess("删除成功");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    // 取消按钮
    cancel() {
      this.sealContractOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        contractName: undefined,
        contractType: undefined,
        projectId: undefined,
      };
      this.selectedFiles = [];
      this.resetForm("form");
    },
    refresh() {
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
      this.queryParams.contractTypeArray = undefined;
      this.queryParams.contractType = undefined;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    remarkCancel() {
      this.remarksOpen = false;
    },

    uploadSealContract(row) {
      this.$refs.uploadSealDialog.init(row);
    },
    changeMainFile(data) {
      this.$confirm("确认要修改合同主文件吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          // 遍历文件
          for (let i = 0; i < this.selectedFiles.length; i++) {
            if (this.selectedFiles[i].uid === data.uid) {
              this.selectedFiles[i].type = "0";
            } else {
              this.selectedFiles[i].type = "1";
            }
          }
        })
        .catch(() => { });
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
    // 文件上传回调
    fileUploadSuccess(file) {
      const data = {
        uid: file.uid,
        name: file.name,
        form: "upload",
        type: 1, // 0正文，1附件
      };

      if (this.selectedFiles.length == 0) {
        data.type = 0;
      }

      this.selectedFiles.push(data);
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      deptTreeSelect().then((response) => {
        this.deptPersonOptions = response.data;
      });
    },
    handleDownload(row) {
      this.$refs.triggerDocDialog.show(row.id, row.contractName);
    },
    cancelContract(row) {
      this.$refs.contractCancel.show(row);
    },
    closeForm() {
      //重新加载数据
      this.getList();
    },
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
</style>
<style lang="scss">
.el-table .warning-row {
  .warn-text {
    .cell {
      border-left: 3px solid rgb(252, 132, 82);
      margin-left: 5px;
    }
  }
}
</style>
