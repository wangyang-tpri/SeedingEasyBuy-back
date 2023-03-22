/*
 * @Author: wangyang-tpri
 * @Date: 2021-09-18 08:36:34
 * @LastEditTime: 2021-10-19 09:39:53
 * @FilePath: \firstVue\service\utils\chartConfig.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 获取企业微信接口信息的配置文件
 */
const queryStr = require("querystring");
const data = {
    corpid: 'wxd8c18dbb36567a1a',
    corpsecret: '0GAstXZKbfDtOmFuVLwTw8JqCmhrXfUs1hH-pG10F_A'
}
const addressData = {
    corpid: 'wxd8c18dbb36567a1a',
    corpsecret: 'NmIgCX-PhjHttih3VtFT9uiQkZCrJizjtwUXQiRiGSQ'
}
let createOpt = (method, path) => {
    let obj = {};
    obj.hostname = 'qyapi.weixin.qq.com';
    obj.method = method;
    obj.path = path;
    return obj;
}
let weChartConfig = {
    tokenOptions: {
        hostname: 'qyapi.weixin.qq.com',
        path: '/cgi-bin/gettoken?' + queryStr.stringify(data),
        method: 'get',
        famliy: 4
    },
    departOptions:(data) => {
        let path = '/cgi-bin/department/list?' + queryStr.stringify(data);
        return createOpt('get', path)
    },
    employeeOptions: (data) => {
        let path = '/cgi-bin/user/simplelist?' + queryStr.stringify(data);
        return createOpt('get', path);
    },
    sendMsgOption: (data) => {
        let path = '/cgi-bin/message/send?' + queryStr.stringify(data);
        return createOpt('post', path);

    },
    addressOption: {
        hostname: 'qyapi.weixin.qq.com',
        path: '/cgi-bin/gettoken?' + queryStr.stringify(addressData),
        method: 'get',
        /**
         * family 这个参数可以指定解析host和hostname的所使用的ip
         * 地址族
         */
        famliy: 4
    },
    tagOption: (data) => {
        let path = '/cgi-bin/tag/list?' + queryStr.stringify(data); 
        return createOpt('get', path);
    },
    tagMemOption: (data) => {
        let path = '/cgi-bin/tag/get?' + queryStr.stringify(data);
        return createOpt('get', path);
    },
    uploadOption: (data) => {
        let path = '/cgi-bin/media/upload?' + queryStr.stringify(data);
        return createOpt('post', path)
    }
    

}
module.exports = weChartConfig