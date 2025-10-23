<template>
    <el-dialog
      title="选择合同"
      :visible.sync="dialogVisible"
      :width="width"
      append-to-body
      destroy-on-close
      :close-on-click-modal="false"
      class="Jdialog Jdialog_center"
    >
      <div
        class="Jcommon-layout-center"
        style="height: 500px; margin-bottom: 20px"
      >
        <el-row class="Jcommon-search-box" :gutter="16">
          <el-form
            v-show="showSearch"
            ref="queryForm"
            :model="queryParams"
            size="small"
            :inline="true"
            label-width="68px"
          >
            <el-col :span="8">
              <el-form-item label="合同编号" prop="contractCode">
                <el-input
                  v-model="queryParams.contractCode"
                  placeholder="请输入合同编号"
                  maxlength="40"
                  @keyup.enter.native="handleQuery"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合同名称" prop="contractName">
                <el-input
                  v-model="queryParams.contractName"
                  placeholder="请输入合同名称"
                  maxlength="100"
                  @keyup.enter.native="handleQuery"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合作方" prop="partnerNames">
                <el-input
                  v-model="queryParams.partnerNames"
                  placeholder="请输入合作方名"
                  maxlength="60"
                  @keyup.enter.native="handleQuery"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-search"
                  size="mini"
                  @click="handleQuery"
                >搜索</el-button>
                <el-button
                  icon="el-icon-refresh"
                  size="mini"
                  @click="resetQuery"
                >重置</el-button>
                <el-button
                  icon="el-icon-delete"
                  size="mini"
                  @click="deleteSelect"
                >清空</el-button>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>
        <div class="Jcommon-layout-main Jflex-main">
          <JTable
            v-loading="loading"
            :has-c="false"
            :has-n-o="false"
            :data="contractDraftList"
            empty-text="请输入条件搜索合同"
          >
            <el-table-column
              label="合同名称"
              align="center"
              prop="contractName"
              min-width="130"
            />
            <el-table-column
              label="合同编号"
              align="center"
              prop="contractCode"
              min-width="130"
            />
            <el-table-column label="我方主体" align="center" prop="subjectNames" min-width="130"/>
            <el-table-column label="合作方" align="center" prop="partnerNames" min-width="130"/>
            <el-table-column
              label="合同类型"
              align="center"
              prop="contractTypeName"
              width="130"
            />
            <el-table-column
              label="合同有效期"
              align="center"
              prop="contractTypeName"
              width="180"
            >
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.effectiveTime, '{y}-{m}-{d}') }}</span>
                <span>至</span>
                <span v-if="scope.row.endTimeType == 'fixed'">{{ parseTime(scope.row.endTime, "{y}-{m}-{d}") }}</span>
                <span v-if="scope.row.endTimeType == 'open'">无固定截止时间</span>
              </template>
  
            </el-table-column>
            <el-table-column
              label="操作"
              align="center"
              class-name="small-padding fixed-width"
              width="80"
              fixed="right"
            >
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-position"
                  @click="selectContract(scope.row)"
                >选择</el-button>
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
    </el-dialog>
  </template>
  
  <script>
  import { relatedContractList } from '@/api/contract/contractDraft'
  import { getContractType } from '@/api/contract/contractType'
  import contractInfoDialog from '../ContractInfoDialog/index.vue'
  export default {
    components: { contractInfoDialog },
    dicts: ['CONTRACT_CURRENCY', 'CONTRACT_STATUS'],
    props: {
      width: {
        type: String,
        default: '1000px'
      }
    },
    data() {
      return {
        // 加载状态
        loading: false,
        // 遮罩层
        dialogVisible: false,
        contractId: undefined,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 合同草拟表格数据
        contractDraftList: [],
        // 合同类型
        contractTypeOptions: [],
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          contractName: undefined,
          contractCode: undefined,
          partnerNames: undefined,
          filter: '全部'
        },
        total: 0
      }
    },
    created() {
      //this.getList()
      this.getContractType()
    },
    methods: {
      show() {
        this.dialogVisible = true
      },
      /** 查询合同类型 */
      getContractType() {
        getContractType().then((response) => {
          this.contractTypeOptions = response.data
        })
      },
      /** 查看合同明细 */
      handleContractView(id) {
        this.$refs.triggerContractDialog.show(id)
      },
      changeFilter() {
        this.getList()
      },
      /** 查询合同草拟列表 */
      getList() {
        this.loading = true
        relatedContractList(this.queryParams).then((response) => {
          this.contractDraftList = response.rows
          this.total = response.total
          this.loading = false
        }).catch(() => {
          this.loading = false
        })
      },
      getCurrencyName(name) {
        return this.selectDictLabel(this.dict.type.CONTRACT_CURRENCY, name)
      },
      // 取消按钮
      cancel() {
        this.open = false
        this.reset()
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1
        this.getList()
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm('queryForm')
        this.handleQuery()
      },
      selectContract(row) {
        this.$emit('addContract', row)
        this.dialogVisible = false
      },
      deleteSelect() {
        this.$emit('deleteContract')
        this.dialogVisible = false
      }
    }
  }
  </script>
  <style lang="scss" scoped></style>
  