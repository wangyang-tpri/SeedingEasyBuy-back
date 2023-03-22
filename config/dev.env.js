'use strict'
// 开发环境变量配置 
// require 就是node中最初的commonJS规范
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')
// merge()的作用 就是连接数组并合并对象，而不是覆盖组合
// 
module.exports = merge(prodEnv, {
  NODE_ENV: '"development"'
})
