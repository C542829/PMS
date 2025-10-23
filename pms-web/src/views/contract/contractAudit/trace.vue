<template>
  <el-dialog
    title="合同跟踪"
    :visible.sync="remarksOpen"
    width="1000px"
    height="500px"
    append-to-body
    :close-on-click-modal="false"
    class="Jdialog Jdialog_center"
  >
    <el-tabs slot="title" v-model="activeTab" class="Jel_tabs" style="padding: 0 10px">
      <el-tab-pane name="one" label="流程跟踪" />

      <el-tab-pane name="two" label="流程信息" />

      <el-tab-pane name="three" label="跟踪信息" />
    </el-tabs>
    <el-button
      v-show="activeTab == 'one'"
      size="mini"
      icon="el-icon-chat-line-square"
      type="primary"
      @click="notifyContract()"
      v-if="drafterId == $store.state.user.userInfo.userId"
    >催办</el-button>
    <simpleFlow v-show="activeTab == 'one'" ref="simpleFlow" />
    
    <recordList v-if="recordShow && activeTab == 'one'" :list="historyList" />
    <div style="position: relative">
      <preview
        v-if="previewShow && activeTab == 'two'"
        ref="processPreview"
        :conf="designerImg"
      />
    </div>
    <el-form v-if="activeTab == 'three'" ref="remakForm" :model="remakForm" :rules="rules">
      <el-form-item label="添加跟踪信息" prop="remarks">
        <el-input
          v-model="remakForm.remarks"
          type="textarea"
          placeholder="请输入内容"
          maxlength="200"
          show-word-limit
        />
      </el-form-item>
      <div style="margin-bottom: 20px">
        <el-button
          :loading="buttonLoading"
          type="primary"
          size="small"
          @click="addRemark"
        >确 定</el-button>
        <el-button size="small" @click="remarkCancel">取 消</el-button>
      </div>
    </el-form>
    <el-table
      v-if="activeTab == 'three'"
      :data="remarksList"
      border
      style="width: 100%; margin-bottom: 50px"
    >
      >
      <el-table-column label="操作人" align="center" prop="createUserName" />
      <el-table-column label="操作时间" align="center" prop="createTime" />
      <el-table-column label="跟踪内容" align="center" prop="remarks" />
      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="deleteRemark(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script>
// vue2模板
import { mapGetters } from 'vuex'
import {
  getRemarks,
  addRemark,
  deleteRemark,
  notify
} from '@/api/contract/contractAudit'
import recordList from '@/views/workflow/form/RecordList'
import request from '@/utils/request'
import { format, formatTotalDateSub } from '@/utils/activiti/myUtil.js'
import preview from '@/components/workflow/Process/preview.vue'
import simpleFlow from '@/views/workflow/designer/simpleFlow.vue'
export default {
  name: 'Remark',
  components: {
    recordList,
    preview,
    simpleFlow
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    processId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      buttonLoading: false,
      historyList: [],
      recordShow: false,
      previewShow: false,
      designerImg: '',
      openId: undefined,
      remakForm: {},
      remarksOpen: false,
      remarksList: [],
      drafterId: undefined,
      rules: {
        remarks: [{ required: true, message: '备注不能为空', trigger: 'blur' }]
      },
      activeTab: 'one',
      historyQueryParams: {
        pageNum: 1,
        pageSize: 999,
        processInstanceId: null,
        activityName: null,
        assignee: null
      }
    }
  },
  computed: {
    ...mapGetters(['name'])
  },
  methods: {
    // 获取备注列表
    getRemarkList() {
      getRemarks(this.openId).then((res) => {
        this.remarksList = res.data
      })
    },
    getInfo(insntaceId) {
      // 查询流程图
      request({
        url: `/activiti/process/designer/` + insntaceId,
        method: 'get'
      }).then((res) => {
        this.designerImg = res.data.model.processData
        const completeTasks = res.data.completeTasks
        const tasks = res.data.tasks

        const loop = (data, taskDefKey, className) => {
          if (Array.isArray(data)) { data.forEach((d) => loop(d, taskDefKey, className)) }
          if (data.nodeId === taskDefKey) {
            data.state = className
            return
          }
          if (data.conditionNodes && Array.isArray(data.conditionNodes)) { loop(data.conditionNodes, taskDefKey, className) }
          if (data.childNode) loop(data.childNode, taskDefKey, className)
        }

        completeTasks.forEach((task) => {
          loop(this.designerImg, task, 'complete')
        })

        tasks.forEach((task) => {
          loop(this.designerImg, task, 'active')
        })

        this.previewShow = true
      })

      // 查询历史记录
      this.historyQueryParams.processInstanceId = insntaceId
      request({
        url: '/activiti/process/listHistory',
        method: 'post',
        data: this.historyQueryParams
      })
        .then((response) => {
          this.historyList = response.rows
          this.historyList.forEach((row) => {
            row.startTime = format(row.startTime, 'yyyy-MM-dd HH:mm:ss')
            row.endTime = format(row.endTime, 'yyyy-MM-dd HH:mm:ss')
            row.durationInMillis = formatTotalDateSub(
              row.durationInMillis / 1000
            )
          })
          this.recordShow = true
        })
        .then(() => {})
    },
    showRemarks(contractId, instanceId, drafterId) {
      this.activeTab = 'one'
      this.remarksOpen = true
      this.remarksList = []
      this.historyList = []
      this.openId = contractId
      this.drafterId = drafterId
      getRemarks(contractId).then((response) => {
        this.remarksList = response.data
      })
      // console.log(row);
      this.getInfo(instanceId)
      this.$nextTick(() => {
        this.$refs.simpleFlow.getData(instanceId)
      })
    },
    addRemark() {
      this.$refs['remakForm'].validate((valid) => {
        if (valid) {
          this.buttonLoading = true
          addRemark({
            contractId: this.openId,
            remarks: this.remakForm.remarks
          }).then((response) => {
            this.$modal.msgSuccess('提交成功')
            this.remakForm.remarks = undefined
            this.buttonLoading = false
            this.getRemarkList()
          })
        }
      })
    },
    // 删除备注
    deleteRemark(id) {
      this.buttonLoading = true
      deleteRemark(this.processId, id).then((res) => {
        this.buttonLoading = false
        this.$message.success('删除备注成功')
        this.getRemarkList()
      })
    },
    remarkCancel() {
      this.remarksOpen = false
    },
    deleteRemark(row) {
      const id = row.id
      this.$modal
        .confirm('是否确认删除该备注"？')
        .then(() => {
          this.loading = true
          return deleteRemark(id)
        })
        .then(() => {
          this.loading = false
          getRemarks(this.openId).then((response) => {
            this.remarksList = response.data
          })
          this.$modal.msgSuccess('删除成功')
        })
        .finally(() => {
          this.loading = false
        })
    },
    async notifyContract() {
       // 添加计数器，30秒内不重复触发，提示稍后在催办
      if (this.urgentTime) {
        return this.$message.warning('您已发起了催办，请稍后再催办！')
      }
      this.urgentTime = new Date().getTime()
      setTimeout(async() => {
        this.urgentTime = null
      }, 1800000)
      var result = await notify(this.openId);
      this.$message.success(result.msg);
    },
  }
}
</script>
<style scoped>
::v-deep .el-dialog__body {
  overflow: auto !important;
}
</style>
