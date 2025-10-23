/**
 * 判断是否为一个整数
 * @param {object} obj 字符串
 * @return {boolean} true/false
 */
export function isInteger(obj) {
  return Math.floor(obj) === obj;
}

/**
 * 将一个浮点数转成整数，返回整数和倍数。如 3.14 >> 314，倍数是 100
 * @param {object} floatNum 小数
 * @return {object} {times:100, num: 314}
 */
export function toInteger(floatNum) {
  var ret = { times: 1, num: 0 };
  var isNegative = floatNum < 0;
  if (isInteger(floatNum)) {
    ret.num = floatNum;
    return ret;
  }
  var strfi = floatNum + "";
  var dotPos = strfi.indexOf(".");
  var len = strfi.substr(dotPos + 1).length;
  var times = Math.pow(10, len);
  var intNum = parseInt(Math.abs(floatNum) * times + 0.5, 10);
  ret.times = times;
  if (isNegative) {
    intNum = -intNum;
  }
  ret.num = intNum;
  return ret;
}

/**
 * 浮点型数字运算精度丢失修复
 * @param {object} num 小数
 * @param {number} s 小数位数
 * @return {object} s位小数
 */
export function toFixed(num, s) {
  var times = Math.pow(10, s);
  var des = num * times + 0.5;
  des = parseInt(des, 10) / times + "";

  var pos_decimal = des.indexOf(".");
  if (pos_decimal > 0) {
    while (des.length <= pos_decimal + s) {
      des += "0";
    }
  }

  return des;
}

/**
 * 浮点型数字运算精度丢失修复
 * 核心方法，实现加减乘除运算，确保不丢失精度
 * 思路：把小数放大为整数（乘），进行算术运算，再缩小为小数（除）
 * @param a {number} 运算数1
 * @param b {number} 运算数2
 * @param dig {number} 精度，保留的小数点数，比如 2, 即保留为两位小数
 * @param op {string} 运算类型，有加减乘除（add/subtract/multiply/divide）
 * @return {object} dig位小数
 */
function operation(a, b, dig, op) {
  var o1 = toInteger(a);
  var o2 = toInteger(b);
  var n1 = o1.num;
  var n2 = o2.num;
  var t1 = o1.times;
  var t2 = o2.times;
  var max = t1 > t2 ? t1 : t2;
  var result = null;
  switch (op) {
    case "add":
      if (t1 === t2) {
        // 两个小数位数相同
        result = n1 + n2;
      } else if (t1 > t2) {
        // o1 小数位 大于 o2
        result = n1 + n2 * (t1 / t2);
      } else {
        // o1 小数位 小于 o2
        result = n1 * (t2 / t1) + n2;
      }
      return toFixed(result / max, dig);
    case "subtract":
      if (t1 === t2) {
        result = n1 - n2;
      } else if (t1 > t2) {
        result = n1 - n2 * (t1 / t2);
      } else {
        result = n1 * (t2 / t1) - n2;
      }
      return toFixed(result / max, dig);
    case "multiply":
      result = (n1 * n2) / (t1 * t2);
      return toFixed(result, dig);
    case "divide":
      if (n2 === 0) result = 0;
      else result = (n1 / n2) * (t2 / t1);
      return toFixed(result, dig);
  }
}

/**
 * 加法
 * @param a {number} 运算数1
 * @param b {number} 运算数2
 * @return {object} s位小数
 */
export function add(a, b, s) {
  return operation(a, b, s | 2, "add");
}

/**
 * 减法
 * @param a {number} 运算数1
 * @param b {number} 运算数2
 * @return {object} s位小数
 */
export function subtract(a, b, s) {
  return operation(a, b, s | 2, "subtract");
}

/**
 * 乘法
 * @param a {number} 运算数1
 * @param b {number} 运算数2
 * @return {object} s位小数
 */
export function multiply(a, b, s) {
  return operation(a, b, s | 2, "multiply");
}

/**
 * 除法
 * @param a {number} 运算数1
 * @param b {number} 运算数2
 * @return {object} s位小数
 */
export function divide(a, b, s) {
  return operation(a, b, s | 2, "divide");
}

export function highPrecisionAdd(data1, data2) {
  // 加法
  let r1, r2, m;
  // 获取每个参数的小数的位数
  try {
    r1 = data1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = data2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  // 计算底数为10以最大小数位数为次幂的值
  m = Math.pow(10, Math.max(r1, r2));
  // 把所有参数转为整数后相加再除以次幂的值
  return (data1 * m + data2 * m) / m;
}

export function highPrecisionMul(data1, data2) {
  // 乘法
  let m = 0,
    s1 = data1.toString(),
    s2 = data2.toString();
  // 获取所有参数小数位长度之和
  try {
    m += s1.split(".")[1].length;
  } catch (e) {}
  try {
    m += s2.split(".")[1].length;
  } catch (e) {}
  // 替换掉小数点转为数字相乘再除以10的次幂值
  return (
    (Number(s1.replace(".", "")) * Number(s2.replace(".", ""))) /
    Math.pow(10, m)
  );
}

export function highPrecisionReduce(data1, data2) {
  // 减法
  let r1, r2, m, n;
  // 获取每个参数的小数的位数
  try {
    r1 = data1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = data2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  // 计算底数为10以最大小数位数为次幂的值
  m = Math.pow(10, Math.max(r1, r2));
  //精度长度以最大小数位数为长度
  n = r1 >= r2 ? r1 : r2;
  return ((data1 * m - data2 * m) / m).toFixed(n);
}

export function highPrecisionDiv(data1, data2) {
  // 除法
  let t1 = 0,
    t2 = 0,
    r1,
    r2;
  // 获取每个参数的小数的位数
  try {
    t1 = data1.toString().split(".")[1].length;
  } catch (e) {}
  try {
    t2 = data2.toString().split(".")[1].length;
  } catch (e) {}
  // 把所有参数的小数点去掉转为整数
  r1 = Number(data1.toString().replace(".", ""));
  r2 = Number(data2.toString().replace(".", ""));
  return (r1 / r2) * Math.pow(10, t2 - t1);
}
