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
          <el-col :span="6" style="height: 47px">
            <el-form-item label="合同名称" prop="contractName">
              <el-input
                v-model="queryParams.contractName"
                placeholder="请输入合同名称"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="合同编号" prop="contractCode">
              <el-input
                v-model="queryParams.contractCode"
                placeholder="请输入合同编号"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="合同类型" prop="contractTypeArray">
              <el-cascader
                v-model="queryParams.contractTypeArray"
                :options="contractTypeOptions"
                collapse-tags
                clearable
                :props="{
                  children: 'children',
                  value: 'id',
                  label: 'name',
                  checkStrictly: true,
                  multiple: true,
                }"
                placeholder="请选择合同类型"
              ></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="合同状态" prop="contractStatus">
              <el-select
                v-model="queryParams.contractStatus"
                placeholder="请选择合同状态"
                clearable
                multiple
                collapse-tags
              >
                <el-option
                  v-for="dict in dict.type.CONTRACT_STATUS"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="收支类型" prop="inouttype">
              <el-select
                v-model="queryParams.inouttype"
                placeholder="请选择收支类型"
                clearable
              >
                <el-option
                  v-for="dict in dict.type.CONTRACT_IN_OUT_CASH"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="生效时间" prop="effectiveTimeRange">
              <el-date-picker
                v-model="queryParams.effectiveTimeRange"
                type="daterange"
                range-separator="至"
                start-placeholder="生效日期"
                end-placeholder="结束日期"
                value-format="timestamp"
                clearable
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="签约主体" prop="subject">
              <el-input
                v-model="queryParams.subject"
                placeholder="请输入签约主体"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="相对方" prop="partner">
              <el-input
                v-model="queryParams.partner"
                placeholder="请输入合同相对方"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="合同摘要" prop="remarks">
              <el-input
                v-model="queryParams.remarks"
                placeholder="请输入合同摘要"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
            <el-form-item label="负责人" prop="contractChargeName">
              <el-input
                v-model="queryParams.contractChargeName"
                placeholder="请输入合同负责人"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col>
          <el-col :span="6" style="height: 47px">
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
          :hasNO="false"
          :data="contractDraftList"
          @sort-change="onSortChange"
        >
          <el-table-column
            label="合同编号"
            align="center"
            prop="contractCode"
            fixed
            width="180"
          />
          <el-table-column
            label="名称"
            align="center"
            prop="contractName"
            fixed
            width="150"
          />
          <el-table-column
            label="状态"
            align="center"
            prop="contractStatusName"
            width="100"
          />
          <el-table-column
            label="合同类型"
            align="center"
            prop="contractTypeName"
            width="120"
          />
          <el-table-column
            label="合同摘要"
            align="center"
            prop="remarks"
            width="120"
          >
            <template slot-scope="scope">
              <el-tooltip
                :content="scope.row.remarks"
                placement="top"
                effect="light"
              >
                <span class="largeText1">
                  {{ scope.row.remarks }}
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            label="签约主体"
            align="center"
            prop="auditForm.subjectNames"
            width="180"
          />
          <el-table-column
            label="相对方"
            align="center"
            prop="auditForm.partnerNames"
            width="180"
          />
          <el-table-column
            label="合同递交日期"
            align="center"
            prop="createTime"
            width="180"
            sortable="custom"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.auditForm">{{
                parseTime(scope.row.auditForm.createTime, "{y}-{m}-{d}")
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="审批通过时间"
            align="center"
            width="150"
            sortable="custom"
            prop="contractApprovePassTime"
          >
            <template slot-scope="scope">
              <span>{{
                parseTime(scope.row.contractApprovePassTime, "{y}-{m}-{d}")
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="生效时间"
            align="center"
            width="150"
            sortable="custom"
            prop="effectiveTime"
          >
            <template slot-scope="scope">
              <span>{{
                parseTime(scope.row.effectiveTime, "{y}-{m}-{d}")
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="150" sortable="custom">
            <template slot-scope="scope">
              <span v-if="scope.row.auditForm">{{
                parseTime(scope.row.endTime, "{y}-{m}-{d}")
              }}</span>
            </template>
          </el-table-column>
          <el-table-column label="收支类型" align="center" prop="inouttype" />
          <el-table-column label="金额" align="right" prop="amount" width="180">
            <template slot-scope="scope">
              {{ moneyFormat(scope.row.amount) }}
            </template>
          </el-table-column>
          <el-table-column label="币种" align="center" prop="currency" />
          <el-table-column
            label="负责人"
            align="center"
            prop="contractChargeName"
            width="100"
          />
          <el-table-column
            label="拟稿人"
            align="center"
            prop="drafterName"
            width="100"
          />
          <el-table-column label="有无自动续签" align="center" width="100">
            <template slot-scope="scope">
              <dict-tag
                :options="dict.type.sys_yes_no"
                :value="scope.row.renewal"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="押金、保证金"
            align="center"
            prop="drafterName"
            width="100"
          >
            <template slot-scope="scope">
              <span v-if="scope.row.extData">{{
                moneyFormat(getExtData(scope.row.extData, "ext_deposit")) 
              }}</span>
                         </template>
          </el-table-column>
          <el-table-column
            label="关联合同"
            align="center"
            prop="drafterName"
            width="100"
          >
            <template slot-scope="scope">
              <el-link
                :underline="false"
                @click="handleContractView(scope.row.relatedContractId)"
                type="primary"
                >{{ scope.row.relatedContractName }}</el-link
              >
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            fixed="right"
            class-name="small-padding fixed-width"
            width="100"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-document"
                @click="handleContractView(scope.row.id)"
                >查看</el-button
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
    <contractInfo-dialog ref="triggerContractDialog" />
  </div>
</template>

<script>
import { listBook } from "@/api/contract/book";
import { getContractType } from "@/api/contract/contractType";
import contractInfoDialog from "@/views/contract/ContractInfoDialog/index.vue";
export default {
  components: { contractInfoDialog },
  dicts: ["CONTRACT_IN_OUT_CASH", "CONTRACT_STATUS", "sys_yes_no"],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 合同类型
      contractTypeOptions: [],
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractName: undefined,
        contractType: undefined,
        subject: undefined,
        partner: undefined,
        contractTypeArray: undefined,
        effectiveTimeRange: undefined,
        createTimeOrder: "descending", //创建时间排序
        effectiveTimeOrder: undefined, //生效时间排序
        contractApprovePassTimeOrder: undefined, //审批通过时间排序
        endTimeOrder: undefined, //截止时间排序
        contractCode: undefined,
        remarks: undefined,
        inouttype: undefined,
        projectName: undefined,
        contractChargeName: undefined, //合同的负责人
      },
    };
  },
  created() {
    this.getList();
    this.getContractType();
  },
  methods: {
    /** 查询合同草拟列表 */
    getList() {
      this.loading = true;
      listBook(this.queryParams).then((response) => {
        this.contractDraftList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getExtData(json, key) {
      var Data = JSON.parse(json);
      return Data[key];
    },
    /** 查询合同类型 */
    getContractType() {
      getContractType().then((response) => {
        this.contractTypeOptions = response.data;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "contract/contractBook/export",
        {
          ...this.queryParams,
        },
        `合同台账_${new Date().getTime()}.xlsx`
      );
    },
    /**查看合同明细 */
    handleContractView(id) {
      this.$refs.triggerContractDialog.show(id);
    },
    onSortChange(column) {
      this.column = column.prop;
      if (column.prop == "createTime") {
        this.queryParams.createTimeOrder = column.order;
      }
      if (column.prop == "effectiveTime") {
        this.queryParams.effectiveTimeOrder = column.order;
        this.queryParams.createTimeOrder = undefined;
      }
      if (column.prop == "contractApprovePassTime") {
        this.queryParams.contractApprovePassTimeOrder = column.order;
        this.queryParams.createTimeOrder = undefined;
      }
      if (column.prop == "endTime") {
        this.queryParams.endTimeOrder = column.order;
        this.queryParams.createTimeOrder = undefined;
      }
      this.getList();
      this.queryParams.createTimeOrder = undefined;
      this.queryParams.effectiveTimeOrder = undefined;
      this.queryParams.contractApprovePassTimeOrder = undefined;
      this.queryParams.endTimeOrder = undefined;
    },
  },
};
</script>
