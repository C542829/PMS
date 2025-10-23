<template>
  <el-dialog
    :title="title"
    :visible.sync="sealContractOpen"
    width="600px"
    append-to-body
    :close-on-click-modal="false"
    class="Jdialog Jdialog_center"
  >
    <el-form
      ref="sealContractForm"
      :model="sealContractForm"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="负责人" prop="contractChargeId">
        <el-cascader
          ref="cascaderItem"
          v-model="sealContractForm.contractChargeId"
          :options="deptPersonOptions"
          :props="defaultProps"
          :filterable="true"
          :show-all-levels="false"
          placeholder="请选择合同负责人"
          @change="handleChange"
        />
      </el-form-item>
      <el-form-item label="生效时间" prop="contractEffectiveTime">
        <el-date-picker
          v-model="sealContractForm.contractEffectiveTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd 00:00:00"
          placeholder="请选择生效时间"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item
        label="截止时间"
        v-if="sealContractForm.endTimeType == 'open'"
      >
        无固定期限
      </el-form-item>
      <el-form-item
        label="截止时间"
        prop="contractEndTime"
        v-if="sealContractForm.endTimeType == 'fixed'"
      >
        <el-date-picker
          v-model="sealContractForm.contractEndTime"
          clearable
          type="date"
          value-format="yyyy-MM-dd 00:00:00"
          placeholder="请选择截止时间"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="双章合同文件" prop="fileGroupsId">
        <FileUploadContract
          upload-module="contract"
          :limit="20"
          :file-size="70"
          :file-type="fileType"
          :show-files="false"
          style="width: 100%"
          @uploadSuccess="fileUploadSuccess($event)"
        />
      </el-form-item>
      <el-form-item label="" prop="">
        <el-table :data="selectedFiles" :show-header="false">
          <el-table-column prop="name" label="文件名" width="200" />
          <el-table-column label="文件类型" width="70">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.type == 0" size="mini">正文</el-tag>
              <el-tag v-if="scope.row.type == 1" size="mini" type="success"
                >附件</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column label="操作" width="122.5">
            <template slot-scope="scope">
              <el-button
                :disabled="scope.row.type == 0"
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
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button :loading="buttonLoading" type="primary" @click="submitForm"
        >确 定</el-button
      >
      <el-button @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import {
  deptTreeSelect,
  uploadSealContract,
} from "@/api/contract/contractAudit";
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      defaultProps: {
        value: 'id',
        label: 'name',
        disabled: 'disabled'
      },
      // 弹出层标题
      title: '',
      // 文件类型
      fileType: ['pdf'],
      sealContractOpen: false,
      // 部门树
      deptPersonOptions: undefined,
      // 按钮loading
      buttonLoading: false,
      selectedFiles: [],
      sealContractForm: {
        contractEffectiveTime: undefined,
        contractEndTime: undefined,
        endTimeType: undefined,
      },
      rules: {
        contractChargeId: [
          { required: true, message: "负责人不能为空", trigger: "blur" },
        ],
        contractEffectiveTime: [
          { required: true, message: "生效时间不能为空", trigger: "blur" },
        ],
        contractEndTime: [
          { required: true, message: "截止时间不能为空", trigger: "blur" },
        ],
        remarks: [{ required: true, message: "备注不能为空", trigger: "blur" }],
      },
    };
  },
  methods: {
    init(row) {
      this.getTreeselect();
      this.sealContractOpen = true;
      this.sealContractForm.contractChargeId = row.contractChargeId;
      this.sealContractForm.contractEffectiveTime = row.effectiveTime;
      this.sealContractForm.contractEndTime = row.endTime;
      this.sealContractForm.endTimeType = row.endTimeType;
      this.openId = row.id;
      this.selectedFiles = [];
      this.title = "上传双章合同";
    },
    // 取消按钮
    cancel() {
      this.sealContractOpen = false;
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
    handleChange(value) {
      const userId = value[value.length - 1];
      const deptId = value[value.length - 2];
      this.sealContractForm.contractChargeId = userId;
    },
    /** 提交按钮 */
    submitForm(row) {
      this.sealContractForm.fileGroupsId = JSON.stringify(this.selectedFiles);
      this.$refs["sealContractForm"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          this.sealContractForm.fileGroupsId = JSON.stringify(
            this.selectedFiles
          );
          this.sealContractForm.contractDraftId = this.openId;
          uploadSealContract(this.sealContractForm)
            .then((response) => {
              //通知刷新待办
              this.$EventBus.$emit("updateTipCount");
              this.$modal.msgSuccess("提交成功");
              this.sealContractOpen = false;
              this.$emit("close");
            })
            .finally(() => {
              this.buttonLoading = false;
            });
        }
      });
    },
  },
};
</script>
