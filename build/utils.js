'use strict'
//  对文件中的所有css文件进行的一个处理
const path = require('path')
// require()的机制  就是直接在文件夹中找对应的文件 直到找到为止
const config = require('../config') // 为什么就直接引用的是index.js

const ExtractTextPlugin = require('extract-text-webpack-plugin')
// package.json文件中的browserlist配置表示提供的目标浏览器环境，
// 智能的添加css前缀，js的polyfill垫片，来兼容低版本的浏览器
// 避免冗余兼容代码  提高编译速度 很好的控制代码的大小
// 只需要在package.json文件中配置好browserlist对象
// webpack打包过程中，不同的loader根据不同的目标浏览器来使用不同的策略对源码进行编译处理
const packageConfig = require('../package.json')

exports.assetsPath = function (_path) {
  const assetsSubDirectory = process.env.NODE_ENV === 'production'
    ? config.build.assetsSubDirectory
    : config.dev.assetsSubDirectory
  return path.posix.join(assetsSubDirectory, _path)  
  // path.join()返回的是完整路径  path.posix.join()返回的是完整路径的相对根路径  
  // 返回的是一个干净的相对根路径
}

exports.cssLoaders = function (options) {
  options = options || {}

  const cssLoader = {
    loader: 'css-loader',
    options: {
      sourceMap: options.sourceMap
    }
  }

  const postcssLoader = {
    // postcss-loader 是一个css兼容处理的loader
    // 根据根路径下的package.json中的浏览器最低需要兼容的版本的设置
    // 进行css的hack的补充 不用再手动的去添加css的属性的前缀
    loader: 'postcss-loader',
    options: {
      sourceMap: options.sourceMap
    }
  }

  // generate loader string to be used with extract text plugin
  function generateLoaders (loader, loaderOptions) {
    const loaders = options.usePostCSS ? [cssLoader, postcssLoader] : [cssLoader]

    if (loader) {
      loaders.push({
        loader: loader + '-loader',
        options: Object.assign({}, loaderOptions, {
          sourceMap: options.sourceMap
        })
      })
    }

    // Extract CSS when that option is specified
    // (which is the case during production build)
    if (options.extract) {
      return ExtractTextPlugin.extract({
        use: loaders,
        fallback: 'vue-style-loader'
      })
    } else {
      return ['vue-style-loader'].concat(loaders)
    }
  }

  // https://vue-loader.vuejs.org/en/configurations/extract-css.html
  return {
    css: generateLoaders(),
    postcss: generateLoaders(),
    less: generateLoaders('less'),
    sass: generateLoaders('sass', { indentedSyntax: true }),
    scss: generateLoaders('sass'),
    stylus: generateLoaders('stylus'),
    styl: generateLoaders('stylus')
  }
}

// Generate loaders for standalone style files (outside of .vue)
exports.styleLoaders = function (options) {
  const output = []
  const loaders = exports.cssLoaders(options)

  for (const extension in loaders) {
    const loader = loaders[extension]
    output.push({
      test: new RegExp('\\.' + extension + '$'),
      use: loader
    })
  }

  return output
}

exports.createNotifierCallback = () => {
  // node-notifier 是一个消息通知模块
  const notifier = require('node-notifier')  

  return (severity, errors) => {
    if (severity !== 'error') return

    const error = errors[0]
    const filename = error.file && error.file.split('!').pop()

    notifier.notify({
      title: packageConfig.name,
      message: severity + ': ' + error.name,
      subtitle: filename || '',
      icon: path.join(__dirname, 'logo.png')
    })
  }
}



