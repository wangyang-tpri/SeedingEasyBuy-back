/*
 * @Author: wangyang-tpri
 * @Date: 2021-06-11 09:23:40
 * @LastEditTime: 2021-11-04 10:28:41
 * @FilePath: \IAPlatform\service\db\dbconfig.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 
 */
/**
 * @description 创建mysql连接池模块
 */
const mysql = require('mysql');

let createSelfPool = () => {
    let pool = mysql.createPool({
        host: 'localhost',
        password: 'password',
        user: 'root',
        database: 'tree_applet',
        poot: 3306
    })
    return pool;
}

module.exports = createSelfPool;