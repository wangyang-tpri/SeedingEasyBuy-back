/*
 * @Author: wangyang-tpri
 * @Date: 2021-06-07 10:07:49
 * @LastEditTime: 2021-12-14 16:52:56
 * @FilePath: \IAPlatform\src\router\index.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: vue项目的路由管理文件 项目中的所有路由都在此处进行管理
 */



import Vue from 'vue'
import Router from 'vue-router'
import createMsg from '@/components/createMsg'
import sendBox from '@/components/sendBox'
import msgDetail from '@/components/msgDetail'
import inbox from '@/components/inbox'
import sendSuccess from '@/components/sendSuccess'
import sendFail from '@/components/sendFail'
import home from '@/views/home'
import {dynamicRouter} from './dynamicRouter'
// vue在全局中使用router 录用
Vue.use(Router)
let homeChildren = [
	{
		path: '/home/msgDetail',
		component: msgDetail
	}, {
		path: '/home/createMsg',
		component: createMsg
	},
	{
		path: '/home/sendBox',
		component: sendBox
	},
	{
		path: '/home/inbox',
		component: inbox
	},
	{
		path: '/home/sendSuccess',
		component: sendSuccess
	},
	{
		path: '/home/sendFail',
		component: sendFail
	},
	{
		path: '/home/msgDetail',
		component: msgDetail
	}
];
homeChildren = homeChildren.concat(dynamicRouter)
let authorityMenu = {
	path: '/home',
	name: 'home',
	component: home,
	children: homeChildren
}
let routes = [
	{
		path: '/',
		name: 'login',
		component: (resolve) => require(['@/views/login'], resolve)
	},
	{
		path: '/login',
	}

]
routes = new Router({
	routes
})

routes.addRoute(authorityMenu)
export default routes
// export default new Router({
// 	routes: routes
// })
