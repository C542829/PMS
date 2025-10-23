<template>
  <el-dialog
    title="上传新版本"
    :visible.sync="dialogVisible"
    width="70%"
    v-loading="loading"
    :before-close="handleClose"
    class="Jdialog Jdialog_center transfer-dialog"
  >
    <el-form style="padding:20px;">
      <el-row :gutter="40">
        <el-col :span="12">
          <el-form-item label="版本号" label-width="200">
            <el-input v-model="oldVersion" readonly />
          </el-form-item>
          <el-form-item label="合同文件" label-width="200">
            <el-table :data="oldFiles">
              <el-table-column
                prop="name"
                label="文件名"
                width="250"
                align="left"
              >
              </el-table-column>
              <el-table-column label="文件类型" width="120" align="center">
                <template slot-scope="scope">
                  <el-tag size="mini" v-if="scope.row.type == 0">正文</el-tag>
                  <el-tag size="mini" v-if="scope.row.type == 1" type="success"
                    >附件</el-tag
                  >
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="新版本号" label-width="200">
            <el-input v-model="newVersion"/>
          </el-form-item>
         <el-form-item label="上传文件" prop="fileGroup">
              <FileUploadContract
                uploadModule="contract"
                :limit="20"
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
        </el-col>
      </el-row>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="submit">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getContractDraft,addNewVersion } from "@/api/contract/contractDraft";

export default {
  data() {
    return {
      dialogVisible: false,
      loading: true,
      oldFiles: [],
      oldVersion: "",
      newFiles: [],
      newVersion: "",
      fileType: ["doc", "docx", "wps", "xls", "xlsx", "pdf", "png", "jpg", "jpeg"],
      selectedFiles: [],
      form:{

      }
    };
  },
  methods: {
    showDialog(id) {
      this.dialogVisible = true;
      this.loading = true;
      this.selectedFiles = [];
      getContractDraft(id).then((response) => {
        response.data.contractTypeArray = JSON.parse(
          response.data.contractType
        );
        this.oldFiles = JSON.parse(response.data.fileGroup);
        this.oldVersion = response.data.fileGroupsVersionName;
        var newVersion = "V" + (parseInt(this.oldVersion.slice(1,2)) + 1) +".0";
        this.newVersion = newVersion;

        this.form.contractDraftId = response.data.id;
        this.loading = false;
      });
    },
    submit() {
        this.form.fileGroupsVersionName = this.newVersion;
        this.form.fileGroupsJson = JSON.stringify(this.selectedFiles);
        addNewVersion(this.form).then(
            res => {
                this.$modal.msgSuccess("上传新版成功");
                this.$emit("reload");
                this.dialogVisible = false;
            }
        )
    },
    handleClose(done) {
        done();
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
      this.selectedFiles.push(data);
    },
  },
};
</script>
