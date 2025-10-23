<template>
  <div class="Jcommon-layout" style="flex-direction: column">
    <el-tabs
      v-show="tabsShow"
      v-model="activeName"
      style="padding-left: 15px; padding-right: 15px; background-color: white"
    >
      <el-tab-pane label="我的合同" name="auditList" />
    </el-tabs>
    <auditList v-show="activeName == 'auditList'" ref="auditList" @hideTab="hideTab" @showTab="showTab" />
  </div>
</template>

<script>
import auditList from './auditList.vue'
export default {
  components: { auditList },
  data() {
    return {
      activeName: 'auditList',
      tabsShow: true,
      changeCount: 0,
    }
  },
  created() {

  },
  methods: {
    hideTab() {
      this.tabsShow = false
    },
    showTab() {
      this.tabsShow = true
    },
    setChangeCount(count){
      this.changeCount = count
    }
  }
}
</script>
