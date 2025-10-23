<template>
    <el-dialog
      title="下载文档"
      :visible.sync="dialogVisible"
      :width="width"
      append-to-body
      destroy-on-close
      class="Jdialog Jdialog_center"
    >
      <el-table
        v-loading="loading"
        :data="docsList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column v-if="false" label="主键" align="center" prop="docId" />
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column property="type" width="150" label="文件类型">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type == '0'">正文</el-tag>
            <el-tag v-else type="info">附件</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="文件名称" prop="name">
          <template slot-scope="scope">
            <!--
              <el-button v-if="scope.row.type == '0'" type="primary" size="mini">正</el-button>
              <el-button v-if="scope.row.type == '1'" type="warning" size="mini" icon="el-icon-paperclip"></el-button>
              -->
            <i class="el-icon-notebook-2" style="color: black" />
            {{ scope.row.name }}
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleDownload">下 载</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </template>
  
  <script>
  import request from "@/utils/request";
  import { getContractDraft } from "@/api/contract/contractDraft";
  export default {
    props: {
      width: {
        type: String,
        default: "800px",
      },
    },
    data() {
      return {
        // 加载状态
        loading: false,
        // 遮罩层
        dialogVisible: false,
        contractId: undefined,
        contractName: undefined,
        // 文档表格数据
        docsList: [],
        // 选中数组
        ids: [],
        // 表单
        form: {},
      };
    },
    methods: {
      show(contractId, contractName, contractCode) {
        this.contractName = contractName;
        this.contractId = contractId;
        this.getList({
          contractId: contractId,
          contractCode: contractCode,
        });
        this.ids = [];
        this.dialogVisible = true;
      },
      changeFileShow(changeId, contractName) {
        this.contractName = contractName;
        this.getChangeFile(changeId);
        this.ids = [];
        this.dialogVisible = true;
      },
      getChangeFile(changeId) {
        this.loading = true;
        request({
          url: "/contract/contractDetail/changeFiles",
          method: "get",
          params: { changeId: changeId },
        }).then((response) => {
          this.docsList = JSON.parse(response.data.files);
          this.loading = false;
        });
      },
      getList(param) {
        this.loading = true;
        request({
          url: "/contract/contractDetail/contractFiles",
          method: "get",
          params: param,
        }).then((response) => {
          this.docsList = JSON.parse(response.data.files.fileGroupsJson);
          if (response.data.form) {
            var auditFiles = JSON.parse(response.data.form.uploadFile);
            if (auditFiles && auditFiles.length > 0) {
              this.docsList = this.docsList.concat(auditFiles);
            }
          }
          this.loading = false;
        });
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map((item) => item.uid);
      },
      // 下载文档
      handleDownload() {
        if (this.ids.length == 0) {
          this.$message.error("请先选择要下载的文件");
        } else {
          this.$download.zipPost(
            "/conf/file/downloadZip/",
            this.ids,
            this.contractName,
            this.contractId
          );
        }
      },
      checkExtension(fileName){
        // 检查文件名是否以.doc或.docx结尾
        if (fileName.endsWith('.doc')) {
            return false;
        } else if (fileName.endsWith('.docx')) {
            return false;
        } else {
            return true;
        }
      },
      convertToPdfExtension(fileName) {
        // 检查文件名是否以.doc或.docx结尾
        if (fileName.endsWith('.doc')) {
            // 移除.doc后缀
            return fileName.slice(0, -4) + '.pdf';
        } else if (fileName.endsWith('.docx')) {
            // 移除.docx后缀
            return fileName.slice(0, -5) + '.pdf';
        } else {
            // 如果不是.doc或.docx文件，返回原始文件名
            return fileName;
        }
      },
      handleDownloadPdf(row) {
        this.download(
          "conf/file/downloadPdf",
          {
            fileId: row.uid,
            contractId: this.contractId,
            contractName: this.contractName,
          },
          this.convertToPdfExtension(row.name)
        );
      },
    },
  };
  </script>
  <style lang="scss" scoped></style>
  