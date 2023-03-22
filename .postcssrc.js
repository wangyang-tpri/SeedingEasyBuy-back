// https://github.com/michael-ciniawsky/postcss-load-config
// 其实就是postcss-loader包的一个配置 
// 
module.exports = {
  "plugins": {
    "postcss-import": {},
    "postcss-url": {},
    // to edit target browsers: use "browserslist" field in package.json
    "autoprefixer": {}  // postcss 添加css前缀组件
  }
}
