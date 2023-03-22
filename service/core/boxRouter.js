
/**
 * @description 业务路由模块
 */
const allInterfaces = require('../core/boxService')
// express框架下 专门用来获取 form-data中 的数据的
const connectMultiparty = require('connect-multiparty')();
const router = require('express').Router();
router.get('/failBox', allInterfaces.getFailBox);
router.get('/sendBox', allInterfaces.getSendBox);
router.get('/inBox', allInterfaces.getInBox);
router.get('/successBox', allInterfaces.getSuccessBox)  
router.get('/deleteRowInBox', allInterfaces.deleteInBox)
router.post('/deleteRowSendBox', allInterfaces.deleteSendBox)
router.post('/deleteRowSuccess', allInterfaces.deleteSuccess)
router.get('/deleteRowFail', allInterfaces.deleteFail)
router.post('/insertBox', allInterfaces.insertBox)
router.post('/login', allInterfaces.loginUser)

router.post('/group/create', allInterfaces.createUserGroup)
router.get('/group/infos', allInterfaces.getGroupInfo)
router.get('/group/delete', allInterfaces.deleteUserGroup)
router.post('/group/update', allInterfaces.updateUserGroup)
router.get('/access/token',allInterfaces.getWeChartInfo)
router.get('/department/all', allInterfaces.getList)
/** 手动更新部门列表的数据*/
router.get('/department/update',allInterfaces.getWeChartInfo)

router.get('/employee/list', allInterfaces.getEmployeeList)
router.get('/addressToken', allInterfaces.getAddressTokenForClient)
router.post('/sendChartMsg', allInterfaces.sendChartMsg)
/** 获取企业微信标签中的数据 */
router.get('/tag/list', allInterfaces.getTagList)
router.get('/tag/members', allInterfaces.getTagMem)

/**
 * 上传图片
 */
router.post('/upload/image', connectMultiparty, allInterfaces.uploadImage)

/**
 * 创建用户
 */
router.post('/user/create', allInterfaces.createUser)
router.post('/user/delete', allInterfaces.deleteUser)
router.post('/user/allGroup', allInterfaces.deleteAllGroupUsers)
router.post('/user/info', allInterfaces.getUserInfo)

router.post('/tem/add', allInterfaces.addTemplate)
router.get('/tem/all', allInterfaces.getAllTem)
router.get('/tem/self', allInterfaces.getSelfTem)
router.get('/delete/all', allInterfaces.getAllDeleteData)
router.get('/delete/data', allInterfaces.deleteDataFromDeleteBox)

router.post('/keyWords/save', allInterfaces.saveKeyWords)
router.get('/keyWords/get', allInterfaces.getKeyWords)
router.post('/keyWords/delete', allInterfaces.deleteKeyWord)
router.post('/fail/save', allInterfaces.saveFailBox)
module.exports = router;
