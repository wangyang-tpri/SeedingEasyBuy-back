/*
 * @Author: wangyang-tpri
 * @Date: 2021-06-07 15:01:07
 * @LastEditTime: 2021-12-10 08:27:22
 * @FilePath: \IAPlatform\service\app.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 
 */
/**
 * @description 通过express 快速的搭建一个web服务器
 */
const express = require('express');
const app = express();
const conn = require('./utils/connecSql')
// const router = require('./selfRouter');
// const constant = require('./utils/constant');
// const expressJWT = require('express-jwt');
// const jsonwebtoken = require('jsonwebtoken');
// const multer = require('multer');

// const queryStr = require('querystring')

// /**
//  * 在服务启动时 直接调用获取企业微信中的接口  
//  */

// const weChart = require('./utils/weChart')
// weChart.getAccessToken();  //获取企业微信的全局token
// weChart.getAddressToken(); // 获取企业微信的统续录 应用的 token
// setInterval(() => {
//     weChart.getAccessToken();
//     weChart.getAddressToken();
// }, 7000 * 1000)
// let storageUpload = multer.memoryStorage();
// let upload = multer({
//     storage: storageUpload
// })
// app.use(express.urlencoded({extended: true}))
// app.use(express.json())
// const allInterfaces = require('./core/boxService');
// app.post('/insertBox',upload.single('file'), (req, res) => {
//     allInterfaces.insertBox(req, res)
// })

// app.use(express.static('../dist'))
// let verifyToken = (token) => {
//     let result = jsonwebtoken.verify(token, constant.PRIMARY_KEY, { algorithm: 'HS256' }) || {};
//     return result;
// }

// app.use(expressJWT({
//     secret: constant.PRIMARY_KEY,
//     algorithms: ['HS256']
// }).unless({
//     path: ['/login']
// }))

// app.use((err, req, res, next) => {
//     try {
//         let reqToken = req.headers.authorization;
//         if (!reqToken || verifyToken(reqToken) == 'err') {
//             res.status(401).send({
//                 code: -1,
//                 msg: 'token验证失败'
//             })
//         } else {
//             next()
//         }
//     } catch (error) {
//         if (error.name == 'TokenExpiredError' || error.name == 'JsonWebTokenError') {
//             res.status(401).send({
//                 code: -1,
//                 msg: 'token过期'
//             })
//         }
//     }

// })

// app.use('/', router)

/**
 * @description 设置监听端口 
 */
app.get('/a', (req, res) => {
    let sqlStr =  "INSERT INTO tree_user ( user_name,password, mobile, user_account, right_control, nick_name ) VALUES ('王洋','0205', '13609257752', '0205','all', 'wy0205')";
    conn( sqlStr ).then(( result) => {
        console.log( result )
        res.send( '数据插入成功')
    }, ( err ) => {
        console.log( err )
        res.send( '数据插入失败')
    })
})
app.listen(8081, () => {
    console.log('服务已启动: localhost:8081')
})

