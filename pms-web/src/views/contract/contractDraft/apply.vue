<template>
  <transition name="el-zoom-in-center">
    <div class="Jpreview-main flow-form-main nohead">
      <div class="btns">
        <template>
          <el-button type="primary" @click="submitForm()" :disabled="isSubmit">提 交</el-button>
          <el-button type="primary" @click="browserSave()" :disabled="isSubmit">临时保存</el-button>
        </template>
        <el-button @click="goBack()">返 回</el-button>
      </div>
      <el-tabs class="Jel_tabs" v-model="activeTab" style="padding: 0 10px">
        <el-tab-pane label="发起合同审批">
          <div class="flow-form">
            <el-form ref="form" :model="form" :rules="rules" label-width="0" label-position="left"
              v-loading="pageLoading" size="small">
              <h3>合同文件:</h3>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="合同文件（需要用印）" label-width="200">
                    <el-table :data="contractInfo.fileGroupJson">
                      <el-table-column prop="name" label="文件名" width="250" align="left">
                      </el-table-column>
                      <el-table-column label="文件类型" width="120" align="center">
                        <template slot-scope="scope">
                          <el-tag size="mini" v-if="scope.row.type == 0">正文</el-tag>
                          <el-tag size="mini" v-if="scope.row.type == 1" type="success">附件</el-tag>
                        </template>
                      </el-table-column>
                      <el-table-column label="是否协同" width="120" align="center">
                        <template slot-scope="scope">
                          <span class="text-success" v-if="contractInfo.coordinationStatus != '0'">已协同</span>
                          <span class="text-danger" v-if="contractInfo.coordinationStatus == '0'">未协同</span>
                        </template>
                      </el-table-column>
                    </el-table>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="附件（无需用印）" label-width="200">
                    <FileUploadContract v-model="form.uploadFileArray" uploadModule="contract" :limit="10"
                      :fileType="fileType" :fileSize="70" style="width: 100%" />
                  </el-form-item>
                </el-col>
              </el-row>

              <h3>关联合同:</h3>
              <div class="applyForm">
                <table style="width: 100%; table-layout: fixed" cellspacing="0">
                  <tr>
                    <td class="tdLabel" style="width：15%">关联合同</td>
                    <td style="width: 35%">
                      <el-form-item label="合同名" prop="relatedContractId">
                        <el-input v-model="form.relatedContractName" placeholder="请选择关联合同" :readonly="true"
                          show-word-limit>
                          <el-button slot="append" type="primary" icon="el-icon-search"
                            @click="selectContractDialogShow"></el-button>
                        </el-input>
                      </el-form-item>
                    </td>
                    <td style="width：15%"></td>
                    <td style="width: 35%"></td>
                  </tr>
                </table>
              </div>

              <h3>基本信息:</h3>
              <div class="applyForm">
                <table style="width: 100%" cellspacing="0">
                  <tr>
                    <td class="tdLabel" style="width：15%">
                      <span style="color: #ff6060">*</span>&nbsp;签约主体
                    </td>
                    <td style="width: 35%">
                      <el-form-item label="签约主体" prop="subjectsArray">
                        <el-select v-model="form.subjectsArray" multiple filterable remote reserve-keyword
                          placeholder="请输入关键词" :remote-method="searchSubjects" :loading="subjectsLoading"
                          style="width: 100%">
                          <el-option v-for="item in subjectsOptions" :key="item.id" :label="item.signingSubjectName"
                            :value="item.id">
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;相对方
                    </td>
                    <td>
                      <el-form-item label="相对方" prop="partnersArray">
                        <el-select v-model="form.partnersArray" multiple filterable remote reserve-keyword
                          placeholder="请输入关键词" :remote-method="searchPartner" :loading="partnerLoading"
                          style="width: 100%">
                          <el-option v-for="item in partnerOptions" :key="item.id" :label="item.contractCounterpartName"
                            :value="item.id">
                          </el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;合同名
                    </td>
                    <td>
                      <el-form-item label="合同名" prop="contractName">
                        <el-input v-model="form.contractName" placeholder="请输入名" show-word-limit maxlength="40"
                          width="100%" :readonly="true" />
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;合同编号
                    </td>
                    <td>
                      <el-form-item label="合同编号" prop="contractCode">
                        <el-input v-model="form.contractCode" width="100%" :readonly="true" placeholder="自动生成" />
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;收支类型
                    </td>
                    <td>
                      <el-form-item label="收支类型" prop="inouttype">
                        <el-select v-model="form.inouttype" placeholder="请选择收支类型" style="width: 100%"
                          @change="inouttypeChange">
                          <el-option v-for="dict in dict.type.CONTRACT_IN_OUT_CASH" :key="dict.value"
                            :label="dict.label" :value="dict.value"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;合同金额(含税)
                    </td>
                    <td>
                      <el-form-item label="合同金额" prop="amount">
                        <MoneyInput v-model="form.amount" placeholder="请输入合同金额" maxlength="16" show-word-limit
                          ref="moneyInput" style="width: 70%" :disabled="this.form.inouttype == '0'"
                          @change="changeAmount" />
                        <el-select v-model="form.currency" placeholder="请选择币种" style="width: 30%">
                          <el-option v-for="dict in dict.type.CONTRACT_CURRENCY" :key="dict.value" :label="dict.label"
                            :value="dict.value"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;开票类型
                    </td>
                    <td style="width: 35%">
                      <el-form-item label="开票类型" prop="invoiceType">
                        <el-select v-model="form.invoiceType" placeholder="请选择收支类型" style="width: 100%">
                          <el-option v-for="dict in dict.type.INVOICE_TYPE" :key="dict.value" :label="dict.label"
                            :value="dict.value"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;税率
                    </td>
                    <td style="width: 35%">
                      <el-form-item label="税率" prop="tax">
                        <el-input v-model="form.tax" placeholder="请输入税率" maxlength="30" @change="changeTax">
                          <template slot="append">%</template>
                        </el-input>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;合同金额（未税）
                    </td>
                    <td style="width: 35%">
                      <el-form-item label="结算金额（未税）" prop="excludeTaxAmount">
                        <MoneyInput v-model="form.excludeTaxAmount" placeholder="请输入结算金额（未税）" maxlength="16"
                          show-word-limit ref="moneyInput2" :disabled="this.form.inouttype == '0'"
                          @change="changeTaxExcludedAmount" />
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;增值税额
                    </td>
                    <td style="width: 35%">
                      <el-form-item label="增值税额" prop="taxAmount">
                        <el-input v-model="form.taxAmount" placeholder="根据税率自动计算" maxlength="30" :readonly="true"
                          :disabled="this.form.inouttype == '0'" />
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;生效时间
                    </td>
                    <td>
                      <el-form-item label="生效时间" prop="effectiveTime">
                        <el-date-picker clearable v-model="form.effectiveTime" type="date"
                          value-format="yyyy-MM-dd 00:00:00" placeholder="请选择生效时间" style="width: 100%">
                        </el-date-picker>
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%"><span style="color: #ff6060">*</span>&nbsp;截止时间</td>
                    <td>
                      <el-form-item label="截止时间" prop="endTime">
                        <el-date-picker clearable v-model="form.endTime" type="date" value-format="yyyy-MM-dd 00:00:00"
                          placeholder="请选择截止时间" style="width: 100%">
                        </el-date-picker>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;印章类型
                    </td>
                    <td>
                      <el-form-item label="印章类型" prop="stampTypeArray">
                        <el-select v-model="form.stampTypeArray" placeholder="请选择印章类型" multiple style="width: 100%">
                          <el-option v-for="dict in dict.type.SEAL_TYPE" :key="dict.value" :label="dict.label"
                            :value="dict.value"></el-option>
                        </el-select>
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;盖章顺序
                    </td>
                    <td>
                      <el-form-item label="盖章顺序" prop="stampOrder">
                        <el-radio-group v-model="form.stampOrder">
                          <el-radio v-for="dict in dict.type.CONTRACT_STAMP_ORDER" :key="dict.value"
                            :label="dict.value">{{
                            dict.label }}</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </td>
                  </tr>
                  <tr>
                    <td class="tdLabel">
                      <span style="color: #ff6060">*</span>&nbsp;合同摘要
                    </td>
                    <td>
                      <el-form-item label="合同摘要" prop="remarks">
                        <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" show-word-limit
                          maxlength="300" :rows="4" />
                      </el-form-item>
                    </td>
                    <td></td>
                    <td></td>
                  </tr>
                  <tr>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;是否为公司模板
                    </td>
                    <td>
                      <el-form-item label="项目编号" prop="isTemplate">
                        <el-radio-group v-model="form.isTemplate">
                          <el-radio v-for="dict in dict.type.sys_yes_no" :key="dict.value" :label="dict.value">{{
                            dict.label
                            }}</el-radio>
                        </el-radio-group>
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">
                      <span style="color: #ff6060">*</span>&nbsp;账期
                    </td>
                    <td>
                      <el-form-item label="账期" prop="moneyPeriod">
                        <el-input v-model="form.moneyPeriod" placeholder="请输入账期" maxlength="20" />
                      </el-form-item>
                    </td>
                  </tr>
                </table>
              </div>
              <h3>发起人&流程信息:</h3>
              <div class="applyForm">
                <table style="width: 100%" cellspacing="0">
                  <tr>
                    <td class="tdLabel" style="width：15%">申请人</td>
                    <td style="width: 35%">
                      <el-form-item label="申请人" prop="applyUserName">
                        <el-input v-model="form.applyUserName" placeholder="请输入申请人" show-word-limit :disabled="true" />
                      </el-form-item>
                    </td>
                    <td class="tdLabel" style="width: 15%">申请部门</td>
                    <td style="width: 35%">
                      <el-form-item label="申请部门" prop="applyDeptName">
                        <el-input v-model="form.applyDeptName" placeholder="请输入申请部门" show-word-limit :disabled="true" />
                      </el-form-item>
                    </td>
                  </tr>
                </table>
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>
      <flowPane v-if="showFlow" ref="flowPane" @choiceFlow="choiceFlow" />
      <selectContractDialog ref="selectContractDialog" @addContract="addRelatedContract"
        @deleteContract="unSelectedContract" />
    </div>
  </transition>
</template>
<script>
import request from "@/utils/request";
import {
  searchPartner,
  searchSubject,
  addContractAudit,
  searchMerchantById
} from "@/api/contract/contractAudit";
import orgSelect from "@/components/workflow/FormControls/OrgSelect";
import flowPane from "@/views/workflow/myApply/create";
import { getContractDraft } from "@/api/contract/contractDraft";
import { mapGetters } from "vuex";
import { firstErrorFocus } from "@/utils/validate";
import selectContractDialog from "./selectContractDialog.vue";
import {
  highPrecisionAdd,
  highPrecisionReduce,
  highPrecisionMul,
  highPrecisionDiv,
} from "@/utils/math";
export default {
  components: { flowPane, selectContractDialog },
  dicts: [
    "CONTRACT_CURRENCY",
    "SEAL_TYPE",
    "CONTRACT_IN_OUT_CASH",
    "CONTRACT_STAMP_ORDER",
    "INVOICE_TYPE",
    "sys_yes_no",
    "CERTIFICATE_TYPE",
  ],
  data() {
    return {
      flowChooseDisabled: false, //是否禁用流程选择按钮
      activeTab: 0,
      pageLoading: false,
      showFlow: false,
      fileType: [
        "doc",
        "docx",
        "wps",
        "xls",
        "xlsx",
        "pdf",
        "png",
        "jpg",
        "jpeg",
      ],
      contractInfo: {},
      subjectsLoading: false,
      subjectsOptions: [],
      partnerLoading: false,
      partnerOptions: [],
      projectLoading: false,
      projectOptions: [],
      isSubmit: false,
      // 表单参数
      form: {
        contractId: undefined,
        contractTypeId: undefined,
        currency: "CNY",
        applyUserId: undefined,
        applyDept: undefined,
        applyUserName: undefined,
        applyDeptName: undefined,
        stampTypeArray: [],
        certificateType: [],
        subjectsArray: [],
        partnersArray: [],
        certificateTypeArray: [],
        uploadFileArray: [],
        contractName: undefined,
        amount: 0,
        instanceDefKey: "",
        instanceDefKeyName: "",
        relatedContractId: undefined,
        relatedContractName: undefined,
        effectiveTime: undefined,
        endTime: undefined,
        inouttype: undefined,
        taxAmount: "0",
        excludeTaxAmount: "0",
        tax: "0",
        stampOrder: undefined,
        invoiceType: "special",
        remarks: "",
        projectId: "",
        isTemplate: undefined,
        renewal: undefined,
        renewalText: undefined,
        projectManager: undefined,
        moneyPeriod: "",
      },
      // 表单校验
      rules: {
        contractName: [
          { required: true, message: "合同名不能为空", trigger: "blur" },
        ],
        contractId: [
          { required: true, message: "合同id不能为空", trigger: "blur" },
        ],
        subjectsArray: [
          { required: true, message: "签约主体不能为空", trigger: "change" },
        ],
        partnersArray: [
          { required: true, message: "相对方不能为空", trigger: "change" },
        ],
        currency: [
          { required: true, message: "币种不能为空", trigger: "change" },
        ],
        amount: [
          { required: true, message: "合同金额不能为空", trigger: "blur" },
        ],
        inouttype: [
          { required: true, message: "收支类型不能为空", trigger: "change" },
        ],
        effectiveTime: [
          { required: true, message: "生效时间不能为空", trigger: "blur" },
        ],
        endTime: [
          { required: true, message: "截止时间不能为空", trigger: "blur" },
        ],
        stampOrder: [
          { required: true, message: "盖章顺序不能为空", trigger: "change" },
        ],
        stampTypeArray: [
          { required: true, message: "印章类型不能为空", trigger: "change" },
        ],
        instanceDefKey: [
          { required: true, message: "审批流程不能为空", trigger: "change" },
        ],
        tax: [
          { required: true, message: "税率不能为空", trigger: "blur" },
          {
            pattern: /^(([1-9]{1}\d{0,9})|(0{1}))(\.\d{0,2})?$/,
            message: "税率格式不正确",
            trigger: "blur",
          },
        ],
        excludeTaxAmount: [
          {
            required: true,
            message: "金额（不含税）不能为空",
            trigger: "blur",
          },
          {
            pattern: /^(([1-9]{1}\d{0,9})|(0{1}))(\.\d{0,2})?$/,
            message: "金额（不含税）格式不正确",
            trigger: "blur",
          },
        ],
        taxAmount: [
          { required: true, message: "增值税额不能为空", trigger: "change" },
        ],
        invoiceType: [
          { required: true, message: "开票类型不能为空", trigger: "change" },
        ],
        isTemplate: [
          {
            required: true,
            message: "请选择是否为公司模板",
            trigger: "change",
          },
        ],
        moneyPeriod: [
          { required: true, message: "账期是必填信息", trigger: "change" },
        ],
        renewal: [
          { required: true, message: "请选择是否自动续签", trigger: "change" },
        ],
        renewalText: [
          { required: true, message: "请输入续签条款", trigger: "blur" },
        ],
        remarks: [
          { required: true, message: "请输入合同摘要", trigger: "blur" },
        ],
      },
      taskData: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  mounted() { },
  methods: {
    init(contractId) {
      this.pageLoading = true;
      this.form.applyUserId = this.userInfo.userId;
      this.form.applyUserName = this.userInfo.nickName;
      this.form.applyDept = this.userInfo.deptId;
      this.form.applyDeptName = this.userInfo.dept.deptName;
      this.form.contractId = contractId;

      getContractDraft(contractId).then((response) => {
        response.data.fileGroupJson = JSON.parse(response.data.fileGroup);
        this.contractInfo = response.data;
        var oldDynamicForm = undefined;
        let code = response.data.form.instanceDefKey;
        if (code != null) {
          this.flowChooseDisabled = true;
        }
        if (this.contractInfo.auditForm) {
          this.form.tax = this.contractInfo.auditForm.tax;
          this.form.amount = this.contractInfo.auditForm.amount;
          this.form.currency = this.contractInfo.auditForm.currency;
          this.form.endTime = this.contractInfo.auditForm.endTime;
          this.form.contractType = this.contractInfo.auditForm.contractType;
          this.form.projectId = this.contractInfo.auditForm.projectId;
          this.form.projectName = this.contractInfo.auditForm.proejctName;
          this.form.projectCode = this.contractInfo.auditForm.projectCode;
          this.form.projectManager = this.contractInfo.auditForm.projectManager;
          this.form.effectiveTime = this.contractInfo.auditForm.effectiveTime;
          this.form.isTemplate = this.contractInfo.auditForm.isTemplate;
          this.form.moneyPeriod = this.contractInfo.auditForm.moneyPeriod;
          this.form.renewalText = this.contractInfo.auditForm.renewalText;
          this.form.renewal = this.contractInfo.auditForm.renewal;
          this.form.certificateTypeArray = JSON.parse(
            this.contractInfo.auditForm.certificateType
          );

          //初始化相对方和签约主体
          let subjectNamesArray =
            this.contractInfo.auditForm.subjectNames.split("、");
          let partnerNamesArray =
            this.contractInfo.auditForm.partnerNames.split("、");
          let subjectsArray = JSON.parse(this.contractInfo.auditForm.subjects);
          let partnersArray = JSON.parse(this.contractInfo.auditForm.partners);

          for (var i = 0; i < subjectsArray.length; i++) {
            this.subjectsOptions.push({
              id: subjectsArray[i],
              signingSubjectName: subjectNamesArray[i],
            });
          }
          for (var i = 0; i < partnersArray.length; i++) {
            this.partnerOptions.push({
              id: partnersArray[i],
              contractCounterpartName: partnerNamesArray[i],
            });
          }

          this.form.subjectsArray = JSON.parse(
            this.contractInfo.auditForm.subjects
          );
          this.form.partnersArray = JSON.parse(
            this.contractInfo.auditForm.partners
          );
          this.form.uploadFileArray = JSON.parse(
            this.contractInfo.auditForm.uploadFile
          );
          this.form.stampTypeArray = JSON.parse(
            this.contractInfo.auditForm.stampType
          );
          this.form.certificateType = JSON.parse(
            this.contractInfo.auditForm.certificateType
          );
          this.form.tax = this.contractInfo.auditForm.tax;
          this.form.taxAmount = this.contractInfo.auditForm.taxAmount;
          this.form.invoiceType = this.contractInfo.auditForm.invoiceType;
          this.form.excludeTaxAmount =
            this.contractInfo.auditForm.excludeTaxAmount;
          this.form.stampOrder = this.contractInfo.auditForm.stampOrder;
          this.form.inouttype = this.contractInfo.auditForm.inouttype;
          this.form.remarks = this.contractInfo.auditForm.remarks;
          oldDynamicForm = JSON.parse(this.contractInfo.auditForm.extForm);
        }
        this.form.contractTypeId = this.contractInfo.form.id;
        this.form.contractName = this.contractInfo.contractName;
        this.form.contractCode = this.contractInfo.contractCode;
        this.form.relatedContractId = this.contractInfo.relatedContractId;
        this.form.relatedContractName = this.contractInfo.relatedContractName;
        this.pageLoading = false;
        this.$refs.moneyInput.init(this.form.amount);
        this.$refs.moneyInput2.init(this.form.excludeTaxAmount);

        //拉取临时保存的数据
        this.browserGet();
      });
    },
    selectFlow() {
      //显示流程面板
      this.showFlow = true;
      this.$nextTick(() => {
        this.$refs.flowPane.init("audit");
      });
    },
    searchSubjects(text) {
      if (text !== "") {
        this.subjectsLoading = true;
        searchSubject({ text: text }).then((res) => {
          this.subjectsOptions = res.data;
          this.subjectsLoading = false;
        });
      } else {
        this.subjectsOptions = [];
      }
    },
    searchPartner(text) {
      if (text !== "") {
        this.partnerLoading = true;
        searchPartner({ text: text }).then((res) => {
          this.partnerOptions = res.data;
          this.partnerLoading = false;
        });
      } else {
        this.partnerOptions = [];
      }
    },
    inouttypeChange(data) {
      if (data == "0") {
        this.form.amount = "0";
        this.form.taxAmount = "0";
        this.form.excludeTaxAmount = "0";
        this.form.tax = "0";

        this.$refs.moneyInput.init(this.form.amount);
        this.$refs.moneyInput2.init(this.form.excludeTaxAmount);
      }
    },
    //临时保存
    browserSave() {
      localStorage.setItem(
        "contract" + this.form.contractId,
        JSON.stringify(this.form)
      );
      this.$modal.msgSuccess("保存成功");
    },
    browserRemove() {
      localStorage.removeItem("contract" + this.form.contractId);
      localStorage.removeItem("contract_form" + this.form.contractId);
    },
    browserGet() {
      var contract = localStorage.getItem("contract" + this.form.contractId);
      var data = localStorage.getItem("contract_form" + this.form.contractId);
      if (contract) {
        let cacheData = JSON.parse(contract);
        searchMerchantById({ partner: cacheData.partnersArray, subject: cacheData.subjectsArray }).then(
          (res) => {
            for (var i = 0; i < res.data.subject.length; i++) {
              this.subjectsOptions.push(res.data.subject[i]);
            }
            for (var i = 0; i < res.data.partner.length; i++) {
              this.partnerOptions.push(res.data.partner[i]);
            }
            //console.log(res);
            this.form = JSON.parse(contract);
            this.$refs.moneyInput.init(this.form.amount);
            this.$refs.moneyInput2.init(this.form.excludeTaxAmount);
          });

      }
    },
    goBack() {
      this.$emit("close");
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          let that = this;
          that.isSubmit = true;
          that.form.subjects = JSON.stringify(that.form.subjectsArray);
          that.form.partners = JSON.stringify(that.form.partnersArray);
          that.form.stampType = JSON.stringify(that.form.stampTypeArray);
          that.form.certificateType = JSON.stringify(
            that.form.certificateTypeArray
          );
          that.form.uploadFile = JSON.stringify(that.form.uploadFileArray);

          //提交数据
          addContractAudit(that.form)
            .then((res) => {
              that.isSubmit = false;
              this.browserRemove();
              that.$modal.msgSuccess("合同提交成功");
              that.$emit("refresh");
            })
            .catch(function (reason) {
              that.isSubmit = false;
            });

        } else {
          firstErrorFocus();
        }
      });
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
    },
    changeTax() {
      if (this.form.amount && this.form.tax) {
        //计算不含税金额
        this.form.excludeTaxAmount = highPrecisionDiv(
          this.form.amount,
          highPrecisionAdd(1, highPrecisionDiv(this.form.tax, 100, 4), 4)
        ).toFixed(2);
        this.$refs.moneyInput2.init(this.form.excludeTaxAmount);
        this.form.taxAmount = highPrecisionReduce(
          this.form.amount,
          this.form.excludeTaxAmount
        );
      } else if (this.form.tax && this.form.excludeTaxAmount) {
        //计算含税金额
        this.form.amount = highPrecisionMul(
          this.form.excludeTaxAmount,
          highPrecisionAdd(1, highPrecisionDiv(this.form.tax, 100, 4), 4)
        ).toFixed(2);
        this.$refs.moneyInput.init(this.form.amount);
        this.form.taxAmount = highPrecisionReduce(
          this.form.amount,
          this.form.excludeTaxAmount
        );
      }
    },
    changeAmount() {
      if (this.form.tax) {
        //计算不含税金额
        this.form.excludeTaxAmount = highPrecisionDiv(
          this.form.amount,
          highPrecisionAdd(1, highPrecisionDiv(this.form.tax, 100, 4), 4)
        ).toFixed(2);
        this.$refs.moneyInput2.init(this.form.excludeTaxAmount);
        this.form.taxAmount = highPrecisionReduce(
          this.form.amount,
          this.form.excludeTaxAmount
        );
      }
    },
    changeTaxExcludedAmount() {
      if (
        this.form.tax &&
        this.form.excludeTaxAmount &&
        this.form.amount &&
        this.form.amount != 0
      ) {
        this.form.taxAmount = highPrecisionReduce(
          this.form.amount,
          this.form.excludeTaxAmount
        );
      }
      if (
        this.form.tax &&
        this.form.excludeTaxAmount &&
        (this.form.amount == undefined || this.form.amount == 0)
      ) {
        //计算含税金额
        this.form.amount = highPrecisionMul(
          this.form.excludeTaxAmount,
          highPrecisionAdd(1, highPrecisionDiv(this.form.tax, 100, 4), 4)
        ).toFixed(2);
        this.$refs.moneyInput.init(this.form.amount);
        this.form.taxAmount = highPrecisionReduce(
          this.form.amount,
          this.form.excludeTaxAmount
        );
      }
    },
  },
};
</script>
<style lang="scss" scoped>
h3 {
  background-color: rgba(249, 249, 249, 1);
  color: #409eff;
  font-size: 14px;
  font-weight: 700;
  height: 40px;
  line-height: 40px;
  padding-left: 10px;
}

table {
  border-collapse: collapse;

  td {
    border: 1px solid rgb(153, 153, 153);
    padding: 5px;
  }
}

.applyForm {
  padding: 10px;

  ::v-deep .el-form-item__label {
    display: none;
  }

  ::v-deep .el-form-item:not(.is-error) {
    margin-bottom: 0;
  }
}
</style>
