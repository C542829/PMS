<template>
  <div>
    <el-drawer
      title=""
      class="custom-drawer"
      :visible.sync="drawerVisible"
      :size="width"
      append-to-body
      destroy-on-close
      v-if="loaded"
      v-loading="loading"
    >
      <div class="task-header" slot="title">
        <div>
          <div>
            <span class="task-title"
              >{{ contractDetail.draft.contractName }}
              <el-tag
                type="warn"
                v-if="contractDetail.draft.contractStatus == '0'"
                style="margin-left: 20px"
                >拟定中</el-tag
              >
              <el-tag
                type="primary"
                v-if="contractDetail.draft.contractStatus == '2'"
                style="margin-left: 20px"
                >已作废</el-tag
              >
              <el-tag
                type="warn"
                v-if="contractDetail.draft.contractStatus == '3'"
                style="margin-left: 20px"
                >审批中</el-tag
              >
              <el-tag
                type="warn"
                v-if="contractDetail.draft.contractStatus == '4'"
                style="margin-left: 20px"
                >待签署</el-tag
              >
              <el-tag
                type="warn"
                v-if="contractDetail.draft.contractStatus == '5'"
                style="margin-left: 20px"
                >已签署</el-tag
              >
              <el-tag
                type="success"
                v-if="
                  contractDetail.draft.contractStatus == '6' &&
                  contractDetail.fulfilStatus == '1'
                "
                style="margin-left: 20px"
                >未生效</el-tag
              >
              <el-tag
                type="success"
                v-if="
                  contractDetail.draft.contractStatus == '6' &&
                  contractDetail.fulfilStatus == '0'
                "
                style="margin-left: 20px"
                >履行中</el-tag
              >
              <el-tag
                type="success"
                v-if="
                  contractDetail.draft.contractStatus == '6' &&
                  contractDetail.fulfilStatus == '2'
                "
                style="margin-left: 20px"
                >已生效</el-tag
              >
              <el-tag
                type="success"
                v-if="
                  contractDetail.draft.contractStatus == '6' &&
                  contractDetail.fulfilStatus == '3'
                "
                style="margin-left: 20px"
                >已截止</el-tag
              >
              <el-tag
                type="success"
                v-if="
                  contractDetail.draft.contractStatus == '6' &&
                  contractDetail.fulfilStatus == '4'
                "
                style="margin-left: 20px"
                >变更中</el-tag
              >
              <el-tag
                type="primary"
                v-if="contractDetail.draft.contractStatus == '7'"
                style="margin-left: 20px"
                >中止</el-tag
              >
              <el-tag
                type="warn"
                v-if="contractDetail.draft.contractStatus == '8'"
                style="margin-left: 20px"
                >已结束</el-tag
              >
              <el-tag
                type="warn"
                v-if="contractDetail.draft.contractStock == '1'"
                style="margin-left: 20px"
                >存量合同</el-tag
              >
            </span>
          </div>
        </div>
        <div style="padding-right: 35px">
          <div class="task-title-right">{{ contractDetail.contractType }}</div>
          <div class="task-title-right">
            {{ contractDetail.draft.contractCode }}
          </div>
        </div>
      </div>
      <el-descriptions
        title=""
        direction="vertical"
        :colon="false"
        label-class-name="custom-label"
        content-class-name="custom-content"
      >
        <el-descriptions-item label="签约主体">{{
          contractDetail.form.subjectNames
        }}</el-descriptions-item>
        <el-descriptions-item label="相对方">{{
          contractDetail.form.partnerNames
        }}</el-descriptions-item>
        <el-descriptions-item label="合同负责人">
          {{ contractDetail.draft.contractChargeName }}
        </el-descriptions-item>
      </el-descriptions>
      <el-tabs v-model="activeName" type="border-card" @tab-click="handleClick">
        <el-tab-pane-ext label="合同信息" name="first">
          <span slot="label"><i class="el-icon-document"></i> 合同信息</span>
          <template v-slot:content>
            <el-descriptions
              title="基础信息"
              direction="vertical"
              :colon="false"
              label-class-name="custom-label"
              content-class-name="custom-content"
            >
              <el-descriptions-item label="签约主体">
                {{ contractDetail.form.subjectNames }}
              </el-descriptions-item>
              <el-descriptions-item label="相对方">
                {{ contractDetail.form.partnerNames }}
              </el-descriptions-item>
              <el-descriptions-item label="关联合同">
                <el-link
                  :underline="false"
                  @click="showRelatedContract"
                  type="primary"
                  >{{ contractDetail.form.relatedContractName }}</el-link
                >
              </el-descriptions-item>
              <el-descriptions-item label="收支类型">
                {{
                  selectDictLabel(
                    dict.type.CONTRACT_IN_OUT_CASH,
                    contractDetail.draft.inouttype
                  )
                }}
              </el-descriptions-item>
              <el-descriptions-item label="合同金额（含税）">
                {{ moneyFormat(contractDetail.draft.amount) }}({{
                  contractDetail.currencyName
                }})
              </el-descriptions-item>
              <el-descriptions-item label="税率">
                {{ moneyFormat(contractDetail.draft.tax) }}%
              </el-descriptions-item>
              <el-descriptions-item label="生效时间">
                {{
                  parseTime(contractDetail.draft.effectiveTime, "{y}-{m}-{d}")
                }}
              </el-descriptions-item>
              <el-descriptions-item label="截止时间">
                {{ parseTime(contractDetail.draft.endTime, "{y}-{m}-{d}") }}
              </el-descriptions-item>
              <el-descriptions-item label="合同负责人">
                {{ contractDetail.draft.contractChargeName }}
              </el-descriptions-item>
              <el-descriptions-item label="合同发起人">
                {{ contractDetail.applyUser }}
              </el-descriptions-item>
              <el-descriptions-item label="盖章顺序">
                <span v-if="contractDetail.form.stampOrder == '1'">
                  我方先盖章
                </span>
                <span v-if="contractDetail.form.stampOrder == '2'">
                  他方先盖章
                </span>
              </el-descriptions-item>
              <el-descriptions-item label="印章类型">
                {{ contractDetail.stamps }}
              </el-descriptions-item>
              <el-descriptions-item label="合同摘要">
                <el-tooltip
                  :content="contractDetail.form.remarks"
                  placement="top"
                  effect="light"
                >
                  <span class="largeText3">
                    {{ contractDetail.form.remarks }}
                  </span>
                </el-tooltip>
              </el-descriptions-item>
              <el-descriptions-item label="是否为公司模板">
                <dict-tag
                  :options="dict.type.sys_yes_no"
                  :value="contractDetail.draft.isTemplate"
                />
              </el-descriptions-item>
              <el-descriptions-item label="账期">
                {{ contractDetail.draft.moneyPeriod }}
              </el-descriptions-item>
            </el-descriptions>

            <el-descriptions
              title="合同文件"
              direction="vertical"
              :colon="false"
              label-class-name="custom-label"
              content-class-name="custom-content"
              :column="2"
            >
              <el-descriptions-item label="正文(需要用印)">
                <div v-for="(item, index) of contractFiles" :key="index">
                  <el-row v-if="item.type == '0'" style="padding-right: 10px">
                    <el-col :span="3">
                      <el-button type="danger" size="mini">F</el-button>
                    </el-col>
                    <el-col :span="21">
                      <el-link
                        :underline="false"
                        type="primary"
                        @click="editDoc(item.uid)"
                        >{{ item.name }}</el-link
                      >
                    </el-col>
                  </el-row>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="附件(需要用印)">
                <div v-for="(item, index) of contractFiles" :key="index">
                  <el-row
                    v-if="item.type == '1'"
                    style="padding-right: 10px; padding-bottom: 5px"
                  >
                    <el-col :span="3">
                      <el-button type="danger" size="mini">F</el-button>
                    </el-col>
                    <el-col :span="21">
                      <el-link
                        :underline="false"
                        type="primary"
                        @click="editDoc(item.uid)"
                        >{{ item.name }}</el-link
                      >
                    </el-col>
                  </el-row>
                </div>
              </el-descriptions-item>
            </el-descriptions>
            
          </template>
        </el-tab-pane-ext>
      </el-tabs>
    </el-drawer>
  </div>
</template>

<script>
import request from "@/utils/request";
import { format, formatTotalDateSub } from "@/utils/activiti/myUtil.js";
import recordList from "@/views/workflow/form/RecordList";
export default {
  dicts: [
    "SEAL_TYPE",
    "CONTRACT_SEAL_STATUS",
    "CONTRACT_CHANGE_TYPE",
    "CONTRACT_FILE_TYPE",
    "CONTRACT_FILING_STATUS",
    "CONTRACT_IN_OUT_CASH",
    "sys_yes_no",
    "INVOICE_TYPE",
  ],
  components: {
    recordList,
  },
  data() {
    return {
      // 加载状态
      loading: false,
      loaded: false,
      // 遮罩层
      drawerVisible: false,
      // 活动tab key
      activeName: "first",
      archiveActiveName: "first",
      coordinationShow: false,
      auditShow: false,
      changeShow: false,
      stampShow: false,
      handoverShow: false, //交接记录
      archiveShow: false,
      taskShow: false,
      contractDetail: {},
      contractFiles: [],
      auditFiles: [],
      auditList: [],
      activeAudit: ["0"],
      activeSeal: [0],
      taskData: [],
      workList: [],
      historyList: [],
      form: {},
      historyQueryParams: {
        pageNum: 1,
        pageSize: 999,
        processInstanceId: null,
        activityName: null,
        assignee: null,
      },
    };
  },
  props: {
    width: {
      type: String,
      default: "60%",
    },
  },
  methods: {
    show(contractId) {
      this.loading = true;
      this.activeName = "first";
      this.archiveActiveName = "first";
      let that = this;
      request({
        url: "/contract/contractDetail",
        method: "get",
        params: { contractId: contractId },
      })
        .then((res) => {
          this.drawerVisible = true;
          this.auditShow = true;
          this.auditFiles = JSON.parse(res.data.form.uploadFile);
          this.form = res.data.form;
          this.contractFiles = JSON.parse(res.data.files.fileGroupsJson);
          this.contractDetail = res.data;
          if (
            this.contractDetail.auditForms.length > 0 &&
            res.data.draft.contractStock == "0"
          ) {
            this.auditShow = true;
          } else {
            this.auditShow = false;
          }

          this.loading = false;
          this.loaded = true;
        })
        .catch(function (error) {
          console.log(error);
          that.loading = false;
          that.drawerVisible = false;
        });
    },
    searchHis(instanceId) {
      //查询历史记录
      this.historyQueryParams.processInstanceId = instanceId;
      request({
        url: "/activiti/process/listHistory",
        method: "post",
        data: this.historyQueryParams,
      })
        .then((response) => {
          this.historyList = response.rows;
          this.historyList.forEach((row) => {
            row.startTime = format(row.startTime, "yyyy-MM-dd HH:mm:ss");
            row.endTime = format(row.endTime, "yyyy-MM-dd HH:mm:ss");
            row.durationInMillis = formatTotalDateSub(
              row.durationInMillis / 1000
            );
          });
          this.recordShow = true;
        })
        .then(() => {});
    },
    showRelatedContract() {
      if (this.contractDetail.form.relatedContractId) {
        this.$nextTick(() => {
          this.show(this.contractDetail.form.relatedContractId);
        });
      }
    },
    handleClick(tab, event) {},
    editDoc(file) {
      //提示从列表下载，这里的功能待完善。
      this.$message({
        message: '暂不支持在线编辑,请从列表下载',
        type: 'warning'
      });
    },
    //显示合同变更单
    showDetail(row) {
      let data = {
        processDefinitionKey: "",
        instanceId: row.instanceId,
        taskId: "",
        taskDefKey: "",
        flowType: "change",
      };
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-drawer__header {
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}
::v-deep .el-drawer__body {
  padding: 0 20px;
}
::v-deep .el-tabs--border-card {
  height: calc(100% - 75px);
  overflow-y: scroll;
}

::v-deep .el-descriptions__header {
  padding: 10px;
  justify-content: center;
  background-color: #f2f6fc;
}
::v-deep .custom-label {
  font-size: 14px !important;
  color: #909399 !important;
}

::v-deep .custom-content {
  font-size: 16px !important;
  color: #303133 !important;
  font-weight: bold !important;
}
::v-deep .el-button--mini {
  padding: 7px;
}

.task-header {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.task-title {
  display: inline-block;
  margin-right: 15px;
  font-size: 20px;
  font-weight: bold;
}
.task-title-right {
  font-size: 14px;
  color: #909399;
}
</style>
