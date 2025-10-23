<template>
  <el-dialog
    :title="title"
    :visible.sync="cancelOpen"
    width="600px"
    height="500px"
    append-to-body
    :close-on-click-modal="false"
    class="Jdialog Jdialog_center"
  >
    <el-form ref="cancelForm" :model="cancelForm" :rules="rules">
      <el-form-item label="作废原因" prop="cancelRemark">
        <el-input
          v-model="cancelForm.cancelRemark"
          type="textarea"
          placeholder="请输入作废原因"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button
        :loading="buttonLoading"
        type="primary"
        @click="addCancelRemark"
      >确 定</el-button>
      <el-button @click="cancelCancel">取 消</el-button>
    </div>
  </el-dialog>
</template>
<script>
import {
  addCancelRemark
} from '@/api/contract/contractAudit'
export default {
  data() {
    return {
      cancelOpen: false,
      cancelForm: {
        cancelRemark: ''
      },
      buttonLoading: false,
      title: '',
      openId: '',
      rules: {
        cancelRemark: [
          { required: true, message: '请输入作废原因', trigger: 'blur' }
        ]
      }

    }
  },
  methods: {
    show(row) {
      this.cancelOpen = true
      this.title = '合同作废'
      const contractId = row.id
      this.openId = contractId
    },
    addCancelRemark() {
      this.$refs['cancelForm'].validate((valid) => {
        if (valid) {
          addCancelRemark({
            id: this.openId,
            contractCancelRemark: this.cancelForm.cancelRemark
          }).then((response) => {
            this.$modal.msgSuccess('提交成功')
            this.cancelOpen = false
            this.openId = undefined
            this.auditId = undefined
            this.cancelForm.cancelRemark = undefined
            this.buttonLoading = false
            this.$emit('refresh')
          })
        }
      })
    },
    cancelCancel() {
      this.cancelOpen = false
      this.cancelForm.cancelRemark = undefined
    }
  }
}
</script>
