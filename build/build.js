'use strict'
require('./check-versions')()

process.env.NODE_ENV = 'development'

// 在node中 有全局变量process表示的是当前的node进程  
// process.env.NODE_ENV = 'development'
// ora的作用主要是用来实现node.js命令行的loading效果，显示各种状态的图标等

const ora = require('ora')

// rimraf以包的形式删除文件或者文件夹 不管文件夹是否为空 都可删除 
// ora rimraf path 都是node的核心模块
// webpack和node是紧密相连的 在webpack的配置中有许多的配置都是需要node中的api做配合的

const rm = require('rimraf')
const path = require('path')
// 输出各种颜色配置  chalk 模块
const chalk = require('chalk')
const webpack = require('webpack')
const config = require('../config')
const webpackConfig = require('./webpack.prod.conf')
// const webpackConfig = require('./webpack.dev.conf')
const spinner = ora('building for production...')
spinner.start()

// path.join()将多个参数合并成一个路径
// 清空静态资源的二级目录下所有内容
rm(path.join(config.build.assetsRoot, config.build.assetsSubDirectory), err => {
  if (err) throw err
  webpack(webpackConfig, (err, stats) => {
    // 停止编译信息的显示
    spinner.stop()
    if (err) throw err
    // 配置编译信息的显示样式
    process.stdout.write(stats.toString({
      colors: true,
      modules: false,
      children: false, // If you are using ts-loader, setting this to true will make TypeScript errors show up during build.
      chunks: false,
      chunkModules: false
    }) + '\n\n')

    if (stats.hasErrors()) {
      console.log(chalk.red('  Build failed with errors.\n'))  // chalk设置输出颜色  
      process.exit(1)
    }

    console.log(chalk.cyan('  Build complete.\n'))
    console.log(chalk.yellow(
      '  Tip: built files are meant to be served over an HTTP server.\n' +
      '  Opening index.html over file:// won\'t work.\n'
    ))
  })
})
