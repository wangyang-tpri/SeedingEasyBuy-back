/*
 * @Author: wangyang-tpri
 * @Date: 2021-11-16 10:35:45
 * @LastEditTime: 2021-12-14 08:47:00
 * @FilePath: \IAPlatform\src\router\dynamicRouter.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 动态路由页面
 */

/**
 * 根据用户的登录情况 获取到当前登录用户的权限
 * 然后根据不同的权限 对动态路由进行筛选
 * 
 *  deleteBox 只有 tpri 才展示
 *  userGroup 只有 admin 才展示
 *  在此处根据 权限实现了 路由的动态配置
 */

import deleteBox from '@/components/deleteBox'
import userGroup from '@/components/userGroup'
import keyWord from '@/components/keyWord'
let dynamicRouter = [{
    path: '/home/deleteBox',
    component: deleteBox
}, {
    path: '/home/wordFilter',
    component: keyWord
}, {
    path: '/home/userGroup',
    component: userGroup
}];
dynamicRouter = sessionStorage.getItem('loginName') == "tpri" ?
    dynamicRouter : 
    dynamicRouter[2]
export {
    dynamicRouter
}