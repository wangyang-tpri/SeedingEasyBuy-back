'use strict'
const utils = require('./utils')
const config = require('../config')
const isProduction = process.env.NODE_ENV === 'production'
const sourceMapEnabled = isProduction
  ? config.build.productionSourceMap
  : config.dev.cssSourceMap

module.exports = {
  loaders: utils.cssLoaders({
    sourceMap: sourceMapEnabled, // 是否开启sourceMap的调试模式
    extract: isProduction  // 是否需要单独的将抽取css文件
  }),
  cssSourceMap: sourceMapEnabled,  // 记录压缩的代码，用来找到源码的位置
  cacheBusting: config.dev.cacheBusting, // 是否破坏缓存

  // vue-loader会把对应的属性自动require之后传给组件
  transformToRequire: {
    video: ['src', 'poster'],
    source: 'src',
    img: 'src',
    image: 'xlink:href'
  }
}
