<template>
  <div class="upload-file">

    <!-- 文件列表 -->
    <transition-group
      v-if="showFiles"
      class="upload-file-list el-upload-list el-upload-list--text"
      name="el-fade-in-linear"
      tag="ul"
    >
      <li
        v-for="(file, index) in fileList"
        :key="file.uid"
        class="el-upload-list__item ele-upload-list__item-content"
      >
        <!--<el-link :href="`${baseUrl}${file.url}`" :underline="false" target="_blank">
          <span class="el-icon-document"> {{ file.name }} </span>
        </el-link>-->
        <span class="el-icon-document"> {{ file.name }} </span>
        <div class="ele-upload-list__item-content-action">
          <el-link
            v-if="canPreview(index)"
            :underline="false"
            type="primary"
            @click="previewImage(index)"
          >查看</el-link>
          <el-link
            v-if="!canPreview(index)"
            :underline="false"
            type="primary"
            @click="showDoc(index)"
          >查看</el-link>
          <el-link
            :underline="false"
            type="primary"
            @click="downloadFile(index)"
          >下载</el-link>
        </div>
      </li>
    </transition-group>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'

export default {
  name: 'FileUpload',
  props: {
    // 值
    value: [String, Object, Array],
    // 数量限制
    limit: {
      type: Number,
      default: 5
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 50
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ['doc', 'xls', 'ppt', 'txt', 'pdf']
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    showFiles: {
      type: Boolean,
      default: true
    },
    uploadModule: {
      type: String,
      default: 'contract'
    }
  },
  data() {
    return {
      baseUrl: process.env.VUE_APP_BASE_API,
      uploadFileUrl: process.env.VUE_APP_BASE_API + '/conf/file/upload/' + this.uploadModule, // 上传的图片服务器地址
      downloadFileUrl: '/conf/file' + '/download/',
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      fileList: []
    }
  },
  computed: {
    // 是否显示提示
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize)
    }
  },
  watch: {
    value: {
      handler(val) {
        if (val) {
          this.fileList = val
        } else {
          this.fileList = []
          return []
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    // 上传前校检格式和大小
    handleBeforeUpload(file) {
      // 校检文件类型
      if (this.fileType) {
        let fileExtension = ''
        if (file.name.lastIndexOf('.') > -1) {
          fileExtension = file.name.slice(file.name.lastIndexOf('.') + 1)
        }
        const isTypeOk = this.fileType.some((type) => {
          if (fileExtension == type) return true
          //   if (file.type.indexOf(type) > -1) return true;
          //   if (fileExtension && fileExtension.indexOf(type) > -1) return true;
          return false
        })
        if (!isTypeOk) {
          this.$message.error(
            `文件格式不正确, 请上传${this.fileType.join('/')}格式文件!`
          )
          return false
        }
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize
        if (!isLt) {
          this.$message.error(`上传文件大小不能超过 ${this.fileSize} MB!`)
          return false
        }
      }
      return true
    },
    // 文件个数超出
    handleExceed() {
      this.$message.error(`上传文件数量不能超过 ${this.limit} 个!`)
    },
    // 上传失败
    handleUploadError(err) {
      this.$message.error('上传失败, 请重试')
    },
    // 上传成功回调
    handleUploadSuccess(res, file) {
      this.$message.success('上传成功')
      this.fileList.push({
        uid: res.data.fileId,
        name: res.data.fileName,
        url: res.data.url
      })
      this.$emit('input', this.fileList)
      this.$emit('uploadSuccess', {
        uid: res.data.fileId,
        name: res.data.fileName,
        url: res.data.url
      })
    },
    // 删除文件
    handleDelete(index) {
      this.fileList.splice(index, 1)
      this.$emit('input', this.fileList)
    },
    // 获取文件名称
    getFileName(name) {
      if (name.lastIndexOf('/') > -1) {
        return name.slice(name.lastIndexOf('/') + 1).toLowerCase()
      } else {
        return ''
      }
    },
    canPreview(index) {
      const fileName = this.fileList[index].name
      const ext = fileName.substring(fileName.lastIndexOf('.')).toLowerCase()
      if (ext == '.png' || ext == '.jpg' || ext == '.jpeg' || ext == '.gif') {
        return true
      } else {
        return false
      }
    },
    // 图片预览
    previewImage(index) {
      const fileName = this.fileList[index].name
      const ext = fileName.substring(fileName.lastIndexOf('.')).toLowerCase()
      if (ext == '.png' || ext == '.jpg' || ext == '.jpeg' || ext == '.gif') {
        this.fetchImageUrl(this.replaceUrlPrefix(this.fileList[index].url, this.baseUrl)).then(res => {
          const url = URL.createObjectURL(res.data);
          this.$previewImgs([url], 0)
        }).catch((error) => {
          this.$message.error('下载图片失败！')
        });
       
      } else {
        this.$message.error('该文件类型不能在线查看')
      }
    },
    replaceUrlPrefix(url, newPrefix) {
        // 使用正则表达式匹配/profile前面的内容，并替换为newPrefix
        const pattern = /^(http[s]?:\/\/[^\/]+)/;
        const newUrl = url.replace(pattern, newPrefix);
        return newUrl;
    },
    showDoc(index) {
      const fileId = this.fileList[index].uid
      const pathInfo = this.$router.resolve({
        path: '/fileView',
        query: { file: fileId }
      })
      window.open(pathInfo.href, '_document')
    },
    downloadFile(index) {
      this.download(
        this.downloadFileUrl +
          this.fileList[index].uid,
        {},
        this.fileList[index].name
      )
    },
    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = ''
      separator = separator || ','
      for (const i in list) {
        strs += list[i].url + separator
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : ''
    }
  }
}
</script>

<style lang="scss"></style>
<style scoped lang="scss">
.upload-file-uploader {
  margin-bottom: 5px;
}
.upload-file-list .el-upload-list__item {
  border: 1px solid #e4e7ed;
  line-height: 2;
  margin-bottom: 10px;
  position: relative;
}
.upload-file-list .ele-upload-list__item-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: inherit;
}
.ele-upload-list__item-content-action .el-link {
  margin-right: 10px;
}
</style>
