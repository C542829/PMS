<template>
  <div class="Jcommon-layout">
    <div class="Jcommon-layout-center">
      <el-row class="Jcommon-search-box" :gutter="16" type="flex">
        <el-form
          :model="queryParams"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="80px"
        >
          <el-col :span="6">
            <el-form-item
              label="合作方名称"
              prop="contractCounterpartName"
              label-width="100"
            >
              <el-input
                v-model="queryParams.contractCounterpartName"
                placeholder="请输入合作方名称"
                clearable
                @keyup.enter.native="handleQuery"
                maxlength="60"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item
              label="合作方编号"
              prop="contractCounterpartCode"
              label-width="100"
            >
              <el-input
                v-model="queryParams.contractCounterpartCode"
                placeholder="请输入合作方编号"
                clearable
                @keyup.enter.native="handleQuery"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="6">
            <el-form-item
              label="统一社会信用代码"
              prop="uscc"
              label-width="150"
            >
              <el-input
                v-model="queryParams.uscc"
                placeholder="请输入统一社会信用代码"
                clearable
                @keyup.enter.native="handleQuery"
                maxlength="18"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="经营状态" prop="businessStatus">
              <el-select
                v-model="queryParams.businessStatus"
                placeholder="请选择企业经营状态"
                clearable
              >
                <el-option
                  v-for="dict in dict.type.BUSINESS_STATUS"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item
              label="合作方类型"
              prop="companyType"
              label-width="100"
            >
              <el-select
                v-model="queryParams.companyType"
                placeholder="请选择合作方类型"
                style="width: 100%"
              >
                <el-option
                  v-for="dict in dict.type.PARTNER_TYPE"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
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
                >新增</el-button
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
          :data="contractCounterpartList"
          @selection-change="handleSelectionChange"
        >
          <el-table-column label="主键" align="center" prop="id" v-if="false" />
          <!-- <el-table-column
            label="合作方编号"
            align="center"
            prop="contractCounterpartCode"
          /> -->
          <el-table-column
            label="合作方名称"
            align="left"
            prop="contractCounterpartName"
          />
          <el-table-column
            label="统一社会信用代码"
            align="center"
            prop="uscc"
          />
          <el-table-column
            label="合作方类型"
            align="center"
            prop="companyType"
            width="100px"
          >
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.PARTNER_TYPE"
                :value="scope.row.companyType"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="经营状态"
            align="center"
            prop="businessStatus"
            width="100px"
          >
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.BUSINESS_STATUS"
                :value="scope.row.businessStatus"
              />
            </template>
          </el-table-column>

          <el-table-column
            label="可用状态"
            align="center"
            key="status"
            prop="status"
            width="100"
          >
            <template slot-scope="scope">
              <!--有管理权限可以都修改，其他人都不能修改-->
              <el-switch
                v-model="scope.row.status"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
              <span>
                <el-tag type="primary" v-if="scope.row.status == '0'"
                  >可用</el-tag
                >
                <el-tag type="danger" v-if="scope.row.status == '1'"
                  >不可用</el-tag
                >
              </span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
            width="220px"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-search"
                @click="handleView(scope.row)"
                >查看</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
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
    <!-- 添加或修改合作方对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="1000px"
      append-to-body
      :close-on-click-modal="false"
      class="Jdialog Jdialog_center"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="140px"
        label-position="right"
      >
        <div class="dialog-card-header">
          <span class="dialog-card-title">基本信息</span>
        </div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="合作方名称" prop="contractCounterpartName">
              <el-input
                v-model="form.contractCounterpartName"
                placeholder="请输入合作方名称"
                maxlength="60"
                show-word-limit
                :disabled="formDisabled"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="合作方编号" prop="contractCounterpartCode">
              <el-input
                v-model="form.contractCounterpartCode"
                placeholder="大写首字母+流水号（四位数字）"
                maxlength="50"
                show-word-limit
                :disabled="formDisabled"
              />
            </el-form-item>
          </el-col> -->
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="统一社会信用代码" prop="uscc">
              <el-input
                v-model="form.uscc"
                placeholder="请输入统一社会信用代码"
                maxlength="18"
                minlength="18"
                show-word-limit
                :disabled="formDisabled"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经营状态" prop="businessStatus">
              <el-select
                v-model="form.businessStatus"
                placeholder="请选择经营状态"
                style="width: 100%"
                :disabled="formDisabled"
              >
                <el-option
                  v-for="dict in dict.type.BUSINESS_STATUS"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人姓名" prop="legalPersonName">
              <el-input
                v-model="form.legalPersonName"
                placeholder="请输入法人姓名"
                maxlength="15"
                show-word-limit
                :disabled="formDisabled"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="法人身份证" prop="legalPersonIdCard">
              <el-input
                v-model="form.legalPersonIdCard"
                placeholder="请输入法人身份证"
                maxlength="18"
                show-word-limit
                :disabled="formDisabled"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合作方类型" prop="companyType">
              <el-select
                v-model="form.companyType"
                placeholder="请选择合作方类型"
                style="width: 100%"
                :disabled="formDisabled"
              >
                <el-option
                  v-for="dict in dict.type.PARTNER_TYPE"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="dialog-card-header">
          <span class="dialog-card-title">详细信息</span>
        </div>
        <div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="注册开始时间" prop="businessStartDate">
                <el-date-picker
                  clearable
                  v-model="form.businessStartDate"
                  style="width: 100%"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="请选择注册开始时间"
                  :disabled="formDisabled"
                >
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="注册结束时间" prop="businessEndDate">
                <el-date-picker
                  clearable
                  v-model="form.businessEndDate"
                  style="width: 70%"
                  type="date"
                  value-format="yyyy-MM-dd"
                  placeholder="请选择注册结束时间"
                  :disabled="formDisabled || form.forever == 1"
                >
                </el-date-picker>
                <el-checkbox
                  v-model="form.forever"
                  :true-label="1"
                  :false-label="0"
                  style="width: 30%; padding-left: 10px"
                  @change="changeForever"
                  :disabled="formDisabled"
                  >长期</el-checkbox
                >
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="注册资本" prop="registeredCapital">
                <el-input
                  v-model="form.registeredCapital"
                  placeholder="请输入注册资本"
                  maxlength="20"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="公司电话" prop="tel">
                <el-input
                  v-model="form.tel"
                  placeholder="请输入公司电话"
                  maxlength="20"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="公司传真" prop="fax">
                <el-input
                  v-model="form.fax"
                  placeholder="请输入公司传真"
                  maxlength="30"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="详细地址" prop="address">
                <el-input
                  v-model="form.address"
                  placeholder="请输入详细地址"
                  maxlength="200"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="营业范围" prop="businessScope">
                <el-input
                  type="textarea"
                  v-model="form.businessScope"
                  placeholder="请输入营业范围"
                  maxlength="1000"
                  show-word-limit
                  :disabled="formDisabled"
                  :rows="4"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-divider></el-divider>
        <div class="dialog-card-header">
          <span class="dialog-card-title">财务信息</span>
        </div>
        <el-card
          class="dialog-card"
          v-for="(bank, index) in form.bankJsonArray"
          :key="bank.key"
        >
          <div slot="header" class="clearfix">
            <span>{{ "财务信息" + (index + 1) }}</span>
            <span
              class="el-icon-delete-solid"
              style="float: right"
              @click="removeBank(bank)"
              v-if="viewShow"
            ></span>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item
                label="开户行"
                :prop="'bankJsonArray.' + index + '.bankName'"
                :rules="{
                  required: true,
                  message: '开户行不能为空',
                  trigger: 'blur',
                }"
                label-width="88px"
              >
                <el-input
                  v-model="bank.bankName"
                  placeholder="请输入开户行"
                  maxlength="100"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="银行帐号"
                :prop="'bankJsonArray.' + index + '.cardNo'"
                :rules="[
                  {
                    required: true,
                    message: '银行帐号不能为空',
                    trigger: 'blur',
                  },
                  {
                    pattern: /^[0-9]\d*$/,
                    message: '请输入合法的银行卡号',
                    trigger: 'blur',
                  },
                ]"
                label-width="88px"
              >
                <el-input
                  v-model="bank.cardNo"
                  placeholder="请输入银行帐号"
                  maxlength="24"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        <el-button @click="addBank" :disabled="formDisabled" v-if="viewShow"
          ><span class="el-icon-plus"></span> 添加财务信息</el-button
        >
        <el-divider></el-divider>
        <div class="dialog-card-header">
          <span class="dialog-card-title">合作方联系人</span>
        </div>
        <el-card
          class="dialog-card"
          v-for="(contact, index) in form.contactsArray"
          :key="contact.key"
        >
          <div slot="header" class="clearfix">
            <span>{{ "联系人" + (index + 1) }}</span>
            <span
              class="el-icon-delete-solid"
              style="float: right"
              @click="removeContacts(contact)"
              v-if="viewShow"
            ></span>
          </div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item
                label="姓名"
                :prop="'contactsArray.' + index + '.name'"
                :rules="{
                  required: true,
                  message: '姓名不能为空',
                  trigger: 'blur',
                }"
                label-width="88px"
              >
                <el-input
                  v-model="contact.name"
                  placeholder="请输入姓名"
                  maxlength="15"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="联系方式"
                :prop="'contactsArray.' + index + '.tel'"
                :rules="[
                  {
                    required: true,
                    message: '联系方式不能为空',
                    trigger: 'blur',
                  },
                ]"
                label-width="88px"
              >
                <el-input
                  v-model="contact.tel"
                  placeholder="请输入联系方式"
                  maxlength="15"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="邮箱"
                :prop="'contactsArray.' + index + '.email'"
                :rules="[
                  {
                    type: 'email',
                    message: '请输入正确的邮箱地址',
                    trigger: ['blur', 'change'],
                  },
                ]"
                label-width="88px"
              >
                <el-input
                  v-model="contact.email"
                  placeholder="请输入邮箱"
                  maxlength="30"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item
                label="备注"
                :prop="'contactsArray.' + index + '.remarks'"
                label-width="88px"
              >
                <el-input
                  v-model="contact.remarks"
                  type="textarea"
                  placeholder="请输入备注"
                  maxlength="200"
                  show-word-limit
                  :disabled="formDisabled"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>
        <el-button @click="addContacts" :disabled="formDisabled" v-if="viewShow"
          ><span class="el-icon-plus"></span> 添加联系人</el-button
        >
        <el-divider></el-divider>

        <div class="dialog-card-header" v-if="form.id != undefined">
          <span class="dialog-card-title">变更记录</span>
        </div>
        <el-table
          :data="form.remarkJson"
          style="width: 100%"
          v-if="form.id != undefined"
        >
          <el-table-column prop="username" label="操作人" width="100">
          </el-table-column>
          <el-table-column prop="date" label="操作时间" width="100">
          </el-table-column>
          <el-table-column prop="result" label="修改内容"> </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          :loading="buttonLoading"
          :disabled="formDisabled"
          type="primary"
          @click="submitForm"
          >确 定</el-button
        >
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listContractCounterpart,
  getContractCounterpart,
  delContractCounterpart,
  addContractCounterpart,
  updateContractCounterpart,
  changeStatus,
} from "@/api/contract/contractCounterpart";
import { getToken } from "@/utils/auth";
import { mapGetters } from "vuex";

export default {
  name: "ContractCounterpart",
  dicts: ["BUSINESS_STATUS", "sys_yes_no", "PARTNER_TYPE"],
  components: { 
    
   },
  data() {
    return {
      defaultProps: {
        value: "id",
        label: "name",
        disabled: "disabled",
      },
      formDisabled: false,
      viewShow: true,
      pickerOptions: {
        disabledDate: (time) => {
          let tempTime = 3600 * 1000 * 24;
          return time.getTime() > new Date() - tempTime;
        },
      },
      selectedUser: [],
      //部门树
      deptPersonOptions: undefined,
      fileType: ["png", "jpg", "jpeg", "pdf"],
      // 按钮loading
      buttonLoading: false,
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
      // 合作方表格数据
      contractCounterpartList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractCounterpartName: undefined,
        contractCounterpartCode: undefined,
        uscc: undefined,
        companyType: undefined,
        associated: undefined,
        companyType: undefined,
      },
      // 表单参数
      form: {
        bankJsonArray: [],
        contractCounterpartHead: "",
        contactsArray: [
          {
            key: Date.now(),
            name: "",
            tel: "",
            email: "",
            remarks: "",
          },
        ],
        attachmentsArray: [
          {
            name: "",
            files: "",
            remarks: "",
            realName: "",
            startDate: undefined,
            endDate: undefined,
            forever: 0,
            licenseNumber: "",
          },
        ],
        forever: 0,
      },
      // 表单校验
      rules: {
        id: [{ required: true, message: "主键不能为空", trigger: "blur" }],
        contractCounterpartName: [
          { required: true, message: "合作方名称不能为空", trigger: "blur" },
        ],
        contractCounterpartCode: [
          { required: true, message: "合作方编号不能为空", trigger: "blur" },
        ],
        companyType: [
          { required: true, message: "合作方类型不能为空", trigger: "blur" },
        ],
        contractCounterpartHead: [
          { required: true, message: "负责人不能为空", trigger: "change" },
        ],
        uscc: [
          //   {
          //     required: true,
          //     message: "统一社会信用代码不能为空",
          //     trigger: "blur",
          //   },
          {
            min: 18,
            max: 18,
            message: "统一社会信用代码长度为18位",
            trigger: "blur",
          },
          {
            pattern: /[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}/,
            message: "请输入正确的统一社会信用代码",
            trigger: "blur",
          },
        ],
        associated: [
          { required: true, message: "是否关联方不能为空", trigger: "change" },
        ],
        delFlag: [
          {
            required: true,
            message: "0 未删除 1 已删除不能为空",
            trigger: "blur",
          },
        ],
        legalPersonIdCard: [
          {
            pattern: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,
            message: "请输入正确的身份证号",
            trigger: "blur",
          },
        ],
        fax: [
          {
            pattern: /^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/,
            message: "请输入正确的传真号",
            trigger: "blur",
          },
        ],
        tel: [
          {
            pattern:
              /(^[0-9]{3,4}\-[0-9]{3,8}$)|(^[0-9]{3,8}$)|(^\([0-9]{3,4}\)[0-9]{3,8}$)|(^0{0,1}1[0-9]{10}$)/,
            message: "请输入正确的电话号",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询合作方列表 */
    getList() {
      this.loading = true;
      listContractCounterpart(this.queryParams).then((response) => {
        this.contractCounterpartList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        contractCounterpartName: undefined,
        uscc: undefined,
        licenseFileId: undefined,
        companyType: "0",
        businessScope: undefined,
        associated: undefined,
        registeredCapital: undefined,
        address: undefined,
        legalPersonName: undefined,
        legalPersonPhone: undefined,
        legalPersonEmail: undefined,
        businessStatus: undefined,
        businessStartDate: undefined,
        businessEndDate: undefined,
        contacts: undefined,
        attachments: undefined,
        contactsArray: [],
        attachmentsArray: [],
        bankJsonArray: [],
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenantId: undefined,
        forever: 0,
        contractCounterpartHead: undefined,
        remarkJson: [],
      };
      this.resetForm("form");
      this.formDisabled = false;
      this.viewShow = true;
    },
    viewDisabled() {
      this.formDisabled = true;
      this.viewShow = false;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
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
      this.title = "添加合作方";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids;
      getContractCounterpart(id).then((response) => {
        this.loading = false;
        response.data.contactsArray = JSON.parse(response.data.contacts);
        response.data.attachmentsArray = JSON.parse(response.data.attachments);
        response.data.bankJsonArray = JSON.parse(response.data.bankJson);
        response.data.contactsArray =
          response.data.contactsArray == null
            ? []
            : response.data.contactsArray;
        response.data.attachmentsArray =
          response.data.attachmentsArray == null
            ? []
            : response.data.attachmentsArray;
        response.data.bankJsonArray =
          response.data.bankJsonArray == null
            ? []
            : response.data.bankJsonArray;
        this.form = response.data;
        this.open = true;
        this.title = "修改合作方";
        if (response.data.remarks) {
          this.form.remarkJson = JSON.parse(response.data.remarks);
        }
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.loading = true;
      this.reset();
      this.viewDisabled();
      const id = row.id || this.ids;
      getContractCounterpart(id).then((response) => {
        this.loading = false;
        response.data.contactsArray = JSON.parse(response.data.contacts);
        response.data.attachmentsArray = JSON.parse(response.data.attachments);
        response.data.bankJsonArray = JSON.parse(response.data.bankJson);
        response.data.contactsArray =
          response.data.contactsArray == null
            ? []
            : response.data.contactsArray;
        response.data.attachmentsArray =
          response.data.attachmentsArray == null
            ? []
            : response.data.attachmentsArray;
        response.data.bankJsonArray =
          response.data.bankJsonArray == null
            ? []
            : response.data.bankJsonArray;
        this.form = response.data;
        this.open = true;
        this.title = "查看合作方";
        if (response.data.remarks) {
          this.form.remarkJson = JSON.parse(response.data.remarks);
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.buttonLoading = true;
          this.form.contacts = JSON.stringify(this.form.contactsArray);
          this.form.attachments = JSON.stringify(this.form.attachmentsArray);
          this.form.bankJson = JSON.stringify(this.form.bankJsonArray);

          if (this.form.id != null) {
            updateContractCounterpart(this.form)
              .then((response) => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              })
              .finally(() => {
                this.buttonLoading = false;
              });
          } else {
            addContractCounterpart(this.form)
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
      let contractCounterpartCode = row.contractCounterpartCode;
      this.$modal
        .confirm(
          '是否确认删除合作方编号为"' + contractCounterpartCode + '"的数据项？'
        )
        .then(() => {
          this.loading = true;
          return delContractCounterpart(ids);
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
        "conf/contractCounterpart/export",
        {
          ...this.queryParams,
        },
        `合作方_${new Date().getTime()}.xlsx`
      );
    },
    /** 表单新增银行信息 */
    addBank() {
      this.form.bankJsonArray.push({
        bankName: "",
        cardNo: "",
      });
    },
    /** 表单删除联系人操作 */
    removeBank(item) {
      var index = this.form.bankJsonArray.indexOf(item);
      if (index !== -1) {
        this.form.bankJsonArray.splice(index, 1);
      }
    },
    /** 表单新增联系人操作 */
    addContacts() {
      this.form.contactsArray.push({
        key: Date.now(),
        name: "",
        tel: "",
        email: "",
        remarks: "",
      });
    },
    /** 表单删除联系人操作 */
    removeContacts(item) {
      var index = this.form.contactsArray.indexOf(item);
      if (index !== -1) {
        this.form.contactsArray.splice(index, 1);
      }
    },
    /** 表单新增资质信息操作 */
    addAttachments() {
      this.form.attachmentsArray.push({
        key: Date.now(),
        name: "",
        files: [],
        remarks: "",
        forever: 0,
        startDate: undefined,
        endDate: undefined,
      });
    },
    /** 表单删除资质信息操作 */
    removeAttachments(item) {
      var index = this.form.attachmentsArray.indexOf(item);
      if (index !== -1) {
        this.form.attachmentsArray.splice(index, 1);
      }
    },
    fileUploadSuccess(attachment, list) {},
    // 合作方状态修改
    handleStatusChange(row) {
      let text = row.status === 0 ? "可用" : "不可用";
      this.$modal
        .confirm(
          '确认要将名称"' +
            row.contractCounterpartName +
            '"的可用状态更改为"' +
            text +
            '"吗？'
        )
        .then(function () {
          return changeStatus(row.id, row.status);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
        })
        .catch(function () {
          row.status = row.status === 0 ? 1 : 0;
        });
    },
    changeForever(value) {
      if (value == 1) {
        this.form.businessEndDate = "";
      }
    },
    changeAttachForever(value, form) {
      if (value == 1) {
        form.endDate = "";
      }
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "合作方导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download(
        this.upload.templateUrl,
        {},
        `partner_template_${new Date().getTime()}.xlsx`
      );
    },
    handleChange(value) {
      if (value == undefined) return;
      let userId = value[value.length - 1];
      // let deptId = value[value.length-2];
      this.form.contractCounterpartHead = userId;
      // this.form.custodyDepartmentId = deptId;
      // const pathLabels =  this.$refs['cascaderItem'].getCheckedNodes()[0].pathLabels;
      // this.form.custodyDepartmentName = pathLabels[pathLabels.length-2];
      // this.form.stampedByName = pathLabels[pathLabels.length-1];
    },
  },
};
</script>
<style lang="scss" scoped>
.dialog-card {
  margin-bottom: 10px;
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both;
  }
}
.dialog-card-header {
  padding-bottom: 30px;
}
.dialog-card-title {
  display: inline-block;
  padding: 0 10px;
  font-size: 16px;
  border-left: 2px solid #1890ff;
}
</style>
