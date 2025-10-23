<template>
  <div class="Jcommon-layout">
    <div class="Jcommon-layout-center">
      <el-row class="Jcommon-search-box" :gutter="16">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
          <el-col :span="6">
            <el-form-item label="签约主体编号" prop="signingSubjectCode" label-width="96px">
              <el-input
                v-model="queryParams.signingSubjectCode"
                placeholder="请输入签约主体编号"
                clearable
                @keyup.enter.native="handleQuery"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="签约主体名称" prop="signingSubjectName" label-width="96px">
              <el-input
                v-model="queryParams.signingSubjectName"
                placeholder="请输入签约主体名称"
                clearable
                @keyup.enter.native="handleQuery"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="企业经营状态" prop="businessStatus" label-width="96px">
              <el-select v-model="queryParams.businessStatus" placeholder="请选择企业经营状态" clearable>
                <el-option
                  v-for="dict in dict.type.BUSINESS_STATUS"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="6">
            <el-form-item label="统一社会信用代码" prop="uscc">
              <el-input
                v-model="queryParams.uscc"
                placeholder="请输入统一社会信用代码"
                clearable
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="6">
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-row>
      <div class="Jcommon-layout-main Jflex-main">
        <div class="Jcommon-head">
          <el-row :gutter="10" class="mb8" style="width:100%;">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleAdd"
                v-hasPermi="['conf:signingSubject:add']"
              >新增</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
          </el-row>
        </div>
        
        <JTable v-loading="loading" :hasC="false" :hasNO="true" :data="signingSubjectList" @selection-change="handleSelectionChange">
          <el-table-column label="主键" align="center" prop="id" v-if="false"/>
          <el-table-column label="签约主体编号" align="center" prop="signingSubjectCode" />
          <el-table-column label="签约主体名称" align="center" prop="signingSubjectName" />
          <el-table-column label="统一社会信用代码" align="center" prop="uscc" width="220"/>
          <el-table-column label="经营状态" align="center" prop="businessStatus" width="100">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.BUSINESS_STATUS" :value="scope.row.businessStatus"/>
            </template>
          </el-table-column>
          <el-table-column label="可用状态" align="center" key="status" prop="status" width="100">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['conf:signingSubject:edit']"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
               icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['conf:signingSubject:remove']"
              >删除</el-button>
            </template>
          </el-table-column>
        </JTable>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <!-- 添加或修改签约主体对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false" class="Jdialog Jdialog_center">
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="签约主体编号" prop="signingSubjectCode">
          <el-input v-model="form.signingSubjectCode" placeholder="请输入签约主体编号" maxlength="20" show-word-limit/>
          <p>例：签约公司大写首字母+流水号（四位数）</p>
        </el-form-item>
        <el-form-item label="签约主体名称" prop="signingSubjectName">
          <el-input v-model="form.signingSubjectName" placeholder="请输入签约主体名称" maxlength="50" show-word-limit />
        </el-form-item>
        <el-form-item label="统一社会信用代码" prop="uscc" >
          <el-input v-model="form.uscc" placeholder="请输入统一社会信用代码" maxlength="18" show-word-limit/>
        </el-form-item>
        <el-form-item label="企业经营状态">
          <el-select v-model="form.businessStatus" placeholder="请选择企业经营状态">
            <el-option
              v-for="dict in dict.type.BUSINESS_STATUS"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="法人姓名" prop="legalPersonName" >
          <el-input v-model="form.legalPersonName" placeholder="请输入法人姓名" maxlength="20" show-word-limit/>
        </el-form-item>
        <el-form-item label="法人身份证号" prop="legalPersonIdCard" >
          <el-input v-model="form.legalPersonIdCard" placeholder="请输入法人身份证号" maxlength="20" show-word-limit/>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" placeholder="请输入内容" maxlength="200" show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSigningSubject, getSigningSubject, delSigningSubject, addSigningSubject, updateSigningSubject, changeStatus } from "@/api/contract/signingSubject";
import moment from 'moment';

export default {
  dicts: ['BUSINESS_STATUS'],
  data() {
    return {
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
      // 签约主体表格数据
      signingSubjectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        signingSubjectCode: undefined,
        signingSubjectName: undefined,
        businessStatus: undefined,
        uscc: undefined,
        remarks: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          { required: true, message: "主键不能为空", trigger: "blur" }
        ],
        signingSubjectCode: [
          { required: true, message: "签约主体编号不能为空", trigger: "blur" }
        ],
        signingSubjectName: [
          { required: true, message: "签约主体名称不能为空", trigger: "blur" }
        ],
        businessStatus: [
          { required: true, message: "企业经营状态不能为空", trigger: "blur" }
        ],
        uscc: [
          { required: true, message: "统一社会信用代码不能为空", trigger: "blur" },
          { min: 18, max: 18, message: '统一社会信用代码长度为18位', trigger: 'blur' },
          {pattern:/[0-9A-HJ-NPQRTUWXY]{2}\d{6}[0-9A-HJ-NPQRTUWXY]{10}/, message: '请输入正确的统一社会信用代码', trigger: 'blur'}
        ],
        version: [
          { required: true, message: "版本号不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询签约主体列表 */
    getList() {
      this.loading = true;
      listSigningSubject(this.queryParams).then(response => {
        this.signingSubjectList = response.rows;
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
        signingSubjectCode: undefined,
        signingSubjectName: undefined,
        businessStatus: undefined,
        uscc: undefined,
        version: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        delFlag: undefined,
        remarks: undefined,
        tenantId: undefined
      };
      this.resetForm("form");
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加签约主体";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getSigningSubject(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改签约主体";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          // 处理日期格式
          const formData = { ...this.form };
          if (formData.createTime) {
            formData.createTime = this.$moment(formData.createTime).format('YYYY-MM-DD HH:mm:ss');
          }
          if (formData.updateTime) {
            formData.updateTime = this.$moment(formData.updateTime).format('YYYY-MM-DD HH:mm:ss');
          }
          
          if (this.form.id != null) {
            updateSigningSubject(formData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addSigningSubject(formData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      let signingSubjectCode = row.signingSubjectCode;
      this.$modal.confirm('是否确认删除签约主体编号为"' + signingSubjectCode + '"的数据项？').then(() => {
        this.loading = true;
        return delSigningSubject(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('demo/signingSubject/export', {
        ...this.queryParams
      }, `signingSubject_${new Date().getTime()}.xlsx`)
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "可用" : "不可用";
      this.$modal.confirm('确认要将名称"' + row.signingSubjectName + '"的可用状态更改为"' + text + '"吗？').then(function() {
        return changeStatus(row.id, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0";
      });
    }
  }
};
</script>
