<template>
  <el-input :value="formatValue" v-bind="$attrs" v-on="inputListeners" />
</template>
<script>
export default {
  inheritAttrs: false,
  props: {
    value: {
      type: [String, Number],
      default: null,
      desc: '数值'
    }
  },
  data: () => {
    return {
      formatValue: ''
    }
  },
  computed: {
    inputListeners: function() {
      var vm = this
      // `Object.assign` 将所有的对象合并为一个新对象
      return Object.assign(
        {},
        // 我们从父级添加所有的监听器
        this.$listeners,
        // // 然后我们添加自定义监听器，
        // // 或覆写一些监听器的行为
        {
          // 这里确保组件配合 `v-model` 的工作
          input: function(event) {
            vm.formatValue = event
            vm.$emit('input', parseFloat(vm.fmoney(event).replace(/,/g, '')))
          },
          blur: function(event) {
            vm.formatValue = vm.fmoney(vm.value)
            if (vm.$listeners.blur) {
              vm.$listeners.blur(event)
            }
          },
          focus: function(event) {
            vm.formatValue = vm.value
          }
        }
      )
    }
  },
  created() {
    this.formatValue = this.fmoney(this.value)
  },
  methods: {
    init(amount) {
      this.formatValue = this.fmoney(amount)
    },
    fmoney(s) {
      if (s === 0) {
        return '0.00'
      } else if (s === '' || s == null) {
        return '0.00'
      }
      var n = 2
      var b = parseFloat(s)
      if (isNaN(b)) {
        return '0.00'
      }
      n = n > 0 && n <= 20 ? n : 2
      if (b < 0) {
        s =
          (-1 * parseFloat((s + '').replace(/[^\d\.-]/g, ''))).toFixed(n) + ''
      } else {
        s = parseFloat((s + '').replace(/[^\d\.-]/g, '')).toFixed(n) + ''
      }
      var l = s.split('.')[0].split('').reverse()
      var r = s.split('.')[1]
      var t = ''
      for (var i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 === 0 && i + 1 !== l.length ? ',' : '')
      }
      if (b < 0) {
        return '-' + t.split('').reverse().join('') + '.' + r
      } else {
        return t.split('').reverse().join('') + '.' + r
      }
    }
  }
}
</script>
