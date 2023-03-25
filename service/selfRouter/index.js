/**
 * @description 自定义路由模块
 * 路由模块定义完成后，在app.js中引入 进行全局配置
 * 然后各个业务模块的http请求都从 路由进入
 */
const express = require('express');
const boxRouter = require('../core/boxRouter')
const router = express.Router();
router.use('', boxRouter)
module.exports = router;