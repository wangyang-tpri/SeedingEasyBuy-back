
/**
 * @description 业务路由模块
 */
const allInterfaces = require('../core/boxService')
const connectMultiparty = require('connect-multiparty')()
const router = require('express').Router();
router.post('/upload/image', connectMultiparty, allInterfaces.uploadTreeImage)
router.get('/keyWord/search', allInterfaces.search)
router.post('/stored/info', allInterfaces.storedInfo)
router.post('/user/info', allInterfaces.userInfo)
router.get('/stored/info', allInterfaces.getTree)
router.get('/accessToken',allInterfaces.getWeChartToken)
router.get('/eachTreeInfo', allInterfaces.getEachTreeInfo)
router.get('/eachTreeImage', allInterfaces.getEachTreeImage)
module.exports = router;
