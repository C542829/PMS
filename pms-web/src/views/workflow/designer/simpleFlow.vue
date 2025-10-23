<template>
  <div style="padding: 20px">
    <el-steps :active="nodeActive" align-center finish-status="success">
      <el-step
        v-for="(item, index) in nodeList"
        :key="index"
        :title="item.nodeName"
        :status="getStatus(item)"
      >
        <template slot="description">
          <p style="color: #999999 !important;margin:0">{{item.userShow.join(', ')}}</p>
        </template>
      </el-step>
    </el-steps>
  </div>
</template>
<script>
import request from '@/utils/request'
export default {
  name: 'Node',
  props: ['instanceId'],
  data() {
    return {
      nodeActive: 0,
      nodeList: []
    }
  },
  mounted() {},
  methods: {
    getStatus(item) {
      if (item.status == 'prev') {
        return 'success'
      }
      if (item.status == 'next') {
        return 'wait'
      }
      if (item.status == 'current' && !item.reject) {
        return 'process'
      }
      if (item.status == 'current' && item.reject) {
        return 'error'
      }
    },
    getData(instanceId) {
      // 查询简版流程图
      this.nodeList = []
      request({
        url: `/activiti/process/simpleWorkflow`,
        method: 'get',
        params: { processInstanceId: instanceId }
      }).then((res) => {
        this.nodeList = res.data
        for (let i = 0; i < this.nodeList.length; i++) {
          if (this.nodeList[i].status == 'current') {
            this.nodeActive = i
            if (this.nodeList[i].reject) {
              this.nodeActive = i + 1
            }
            return
          }
        }
        // 没有current
        this.nodeActive = this.nodeList.length
      })
    }
  }
}
</script>
