/*
 * @Author: wangyang-tpri
 * @Date: 2021-06-07 14:23:03
 * @LastEditTime: 2021-12-16 10:59:42
 * @FilePath: \IAPlatform\src\utils\api.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: API接口调用模块 项目中的所有接口都调用都会在此处进行处理
 */


import axios from 'axios'
import { Loading, Message } from 'element-ui'
import common from './common'

/** 创建了一个axios的实例  在这个实例中可以进行自定义的配置*/

let selfInstance = axios.create();

selfInstance.defaults.baseURL = '';
selfInstance.defaults.headers.post['Content-Type'] = "application/x-www-form-urlencoded";
sessionStorage.getItem('token') && (selfInstance.defaults.headers.Authorization = sessionStorage.getItem('token'));
/**
 * 对response返回的状态码 进行统一处理
 * 添加响应拦截器 只有status === 200 时 请求才能到达具体的页面进行处理
 * 其他的响应状态码 则会在拦截器中进行对应的处理
 */
selfInstance.interceptors.response.use((res) => {
    return res;
}, (err) => {
    let resStatus;
    resStatus = err.response.status;
    if(resStatus == 404) {
        Message.error(common.notFound);
    } else if (resStatus == 401) {
        sessionStorage.clear();
        let redirctHref = location.href.split('#')[0];
        location.href = redirctHref;
    } else if (resStatus == 500) {
        Message.error(common.serverErr)
    }
    return Promise.reject(err);
})
export default {
    /**
     * 获取已经发送的 信息
     * @returns {Object} promise
     */
    selfInstance: selfInstance,
    getSendBox: (userId) => {
        return selfInstance({
            method: 'get',
            url: '/sendBox',
            params: {
                userId: userId
            }
        });
    },

    /**
     * 收件箱
     * @returns {Object} promise
     */
    getInbox: () => {
        return selfInstance.get('/inBox');
    },

    getDatePeriodSendBox: (sDate, eDate, userId) => {
        return selfInstance({
            type: 'post',
            url: '/sendBox',
            params: {
                sTime: sDate,
                eTime: eDate,
                userId: userId
            }
        })
    },
    getDatePeriodInbox: (sDate, eDate) => {
        return selfInstance({
            type: 'post',
            url: '/inBox',
            params: {
                sTime: sDate,   
                eTime: eDate
            }
        })
    },
    /**
     * 获取 发送失败 的所有信息
     * @returns {Object} promise
     */
    getFailBox: () => {
        return selfInstance.get('/failBox');
    },

    /**
     * 用户注册的接口
     * @returns {Object} promise
     */
    registerUser: () => {
        return selfInstance({
            type: 'post',
            url: '/registerUser',
            params: {
                userName: '',
                passward: ''
            }
        })
    },

    /**
     * 根据起始时间 查询发送失败的信息
     * @returns {Object} promise
     */
    getFailDatePeriod: (sDate, eDate) => {
        return selfInstance({
            type: 'post',
            url: '/failBox',
            params: {
                sTime: sDate,
                eTime: eDate
            }
        })
    },

    /**
     * 根据起始时间 查询发送成功的信息
     * @returns {Object} promise
     */
    getSuccessDatePeriod: (sDate, eDate, userId) => {
        return selfInstance({
            type: 'get',
            url: '/successBox',
            params: {
                sTime: sDate,
                eTime: eDate,
                userId: userId
            }
        })
    },
    /**
     * 发送成功的接口 /successbox
     * @returns {Object} promise
    */

    getSuccessBox: (userId) => {
        return selfInstance({
            method: 'get',
            url: '/successBox',
            params: {
                userId: userId
            }
        })
    },

    deleteRowInBox: (rowId) => {
        return selfInstance({
            type: 'post',
            url: '/deleteRowInBox',
            params: {
                rowId: rowId
            }
        })
    },
    deleteRowSendBox: (rowId, reciveName, context, title) => {
        return selfInstance({
            method: 'post',
            url: '/deleteRowSendBox',
            data: {
                rowId: rowId,
                reciveName: reciveName,
                context: context,
                title: title
            }
        })
    },
    deleteRowFailBox: (rowId) => {
        return selfInstance({
            type: 'post',
            url: '/deleteRowFail',
            params: {
                rowId: rowId
            }
        })
    },
    deleteRowSuccessBox: (rowId, reciveName, context, title) => {
        return selfInstance({
            method: 'post',
            url: '/deleteRowSuccess',
            data: {
                rowId: rowId,
                reciveName: reciveName,
                context: context,
                title: title
            }
        })
    },
    
    /**
     * @description 将用户创建的信息插入到数据库中
     * @param {String} userName 
     * @param {String} title 
     * @param {String} context 
     * @param {String} touser
     * @param {String} toparty
     * @param {String} totag
     * @param {String} mediaId
     * @param {String} digest
     * @returns Promise
     */

    insertBox: (userName, title, context, touser, toparty, totag, mediaId, digest, externalLinks, userId) => {
        return selfInstance({
            method: 'post',
            url: '/insertBox',
            data: {
                reviceN: userName,
                title: title,
                context: context,
                touser: touser,
                toparty: toparty,
                totag: totag,
                mediaId: mediaId,
                digest: digest,
                externalLinks: externalLinks,
                userId: userId
            }
        })
    },
    loginUser: (userName, password) => {
        return selfInstance({
            method: 'post',
            url: '/login', 
            data: {
                userName: userName,
                password: password
            }
        })
    },
    
    /**
     * @description 创建用户组
     * @param {String} gName 
     * @param {String} gDesc 
     * @param {String} gUsers 
     * @returns Promise
     */
    createUserGroup: (gName, gDesc, gUsers, userId) => {
        return selfInstance({
            method: 'post',
            url: '/group/create',
            data: {
                groupName: gName,
                groupDesc: gDesc,
                groupUsers: gUsers,
                userId: userId
            }
        })
    },
    getUserGroup: (userId) => {
        return selfInstance({
            method: 'get',
            url: '/group/infos',
            params: {
                userId: userId
            }
        })
    },
    deleteUserGroup: (groupName) => {
        return selfInstance({
            method: 'get',
            url: '/group/delete',
            params: {
                groupName: groupName
            }
        })
    },
    /**更新用户组中的数据 */
    updateUserGroup: (gName, gUsers) => {
        return selfInstance({
            method: 'post',
            url: '/group/update',
            data: {
                gName: gName,
                gUsers: gUsers
            }
        })
    },
    callWeChartAccessToken: () => {
        return selfInstance({
            method: 'get',
            url: '/access/token',
            params: {
               
            }
        })
    },
    getDeparmentList: () => {
        return selfInstance({
            method: 'get',
            url: '/department/all',
            params: {
            }
        })
    },
    updateDepartmentData: () => {
        return selfInstance({
            method: 'get',
            url: '/department/update',
            params: {}
        })
    },
    getEmployee: (listid) => {
        return selfInstance({
            method: 'get',
            url: '/employee/list',
            params: {
    
            }
        })
    },
    getAddressToken: () => {
        return selfInstance({
            method: 'get',
            url: '/addressToken',
            params:{
                
            }
        })
    },
    chartMsg: (token, content, title, employee, department, tag, digest, mediaId, url) => {
        return selfInstance({
            method: 'post',
            url: '/sendChartMsg',
            data: {
                access_token: token,
                title: title,
                content: content,
                employee: employee,
                department: department,
                tag: tag,
                digest: digest,
                mediaId: mediaId,
                url: url
            }
        })
    },
    tagList: () => {
        return selfInstance({
            method: 'get',
            url: '/tag/list',
            params: {

            }
        })
    },
    tagMem: (tagId) =>{
        return selfInstance({
            method: 'get',
            url: '/tag/members',
            params: {
                tagId: tagId
            }
        })
    },
    /**
     * 创建用户
     */
    createUser: (userName, password, userControl, gUsers, gName) => {
        
        return selfInstance({
            method: "post",
            url: '/user/create',
            data: {
                userName: userName,
                password: password,
                userControl: userControl,
                gUsers: gUsers,
                gName: gName
            }
        })
    },
    deleteUser: (gName, user) => {
        return selfInstance({
            method: 'post',
            url: '/user/delete',
            data: {
                gName: gName,
                user: user
            }
        })
    },
    deleteAllGroupUsers: (groupName) => {
        return selfInstance({
            method: 'post',
            url: '/user/allGroup',
            data: {
                groupName: groupName
            }
        })
    },
    getUserInfo: (groupName) => {
        return selfInstance({
            method: 'post',
            url: '/user/info',
            data: {
                groupName: groupName
            }
        })
    },
    addTemplate: (name, user, title, digest, url, content, userId, employee,departy, totag)=> {
        return selfInstance({
            method: 'post',
            url: '/tem/add',
            data: {
                name:name, 
                user: user,
                title: title, 
                digest: digest, 
                url: url,
                content: content,
                userId: userId,
                employee: employee,
                departy: departy,
                totag: totag
            }
        })
    },
    getAllTem: (userId) => {
        return selfInstance({
            method: 'get',
            url: '/tem/all',
            params: {
                userId: userId
            }
        })
    },
    getSelfTem: (userId, tem_name) => {
        return selfInstance({
            method: 'get',
            url: '/tem/self',
            params: {
                userId: userId,
                tem_name: tem_name
            }
        })
    },
    getDeleteData: () => {
        return selfInstance({
            method: 'get',
            url: '/delete/all',
            params: {
                
            }
        })
    },
    deleteRowDeleteBox: (id) => {
        return selfInstance({
            method: 'get',
            url: '/delete/data',
            params: {
                id: id
            }
        })
    },
    saveKeyWords: (words) => {
        return selfInstance({
            method: 'post',
            url: '/keyWords/save',
            data: {
                keyWords: words
            }
        })
    },
    getKeyWords: () => {
        return selfInstance({
            method: 'get',
            url: '/keyWords/get',
            params: {}
        })

    },
    deleteKeyWord: (word) => {
        return selfInstance({
            method: 'post',
            url: '/keyWords/delete',
            data: {
                word: word
            }
        })
    },
    saveFailBox: (reciveName, content, title, employee, departmentData, tag, digest) => {
        return selfInstance({
            method: 'post',
            url: '/fail/save',
            data: {
                reciveName: reciveName,
                content: content,
                title: title,
                employee: employee,
                departmentData: departmentData,
                tag: tag,
                digest: digest
            }
        })
    }
}