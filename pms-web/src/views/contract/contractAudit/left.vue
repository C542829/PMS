<template>
  <div class="Jcommon-layout-left">
    <div class="Jcommon-title">
      <h2 style="margin: 0">组织机构</h2>
    </div>
    <el-scrollbar class="Jcommon-el-tree-scrollbar">
      <div class="head-container">
        <el-input
          v-model="deptName"
          placeholder="请输入部门名称"
          clearable
          size="small"
          prefix-icon="el-icon-search"
          style="margin-bottom: 20px"
        />
      </div>
      <div class="head-container">
        <el-tree
          ref="tree"
          :data="deptOptions"
          :props="defaultProps"
          :expand-on-click-node="false"
          :filter-node-method="filterNode"
          default-expand-all
          @node-click="handleNodeClick"
        />
      </div>
    </el-scrollbar>
  </div>
</template>
<script>
export default {
  components: { Treeselect, newTreeSelect },
  data() {
    return {
      deptName: "",
      deptOptions: [],
      defaultProps: {
        children: "children",
        label: "label",
      },
    };
  },
  created() {
    this.getTreeselect();
  },
  methods: {
    getTreeselect() {
      treeselect().then((response) => {
        this.deptOptions = response.data;
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleNodeClick(data) {
      //console.log(data);
    },
  },
};
</script>
