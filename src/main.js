/*
 * @Author: wangyang-tpri
 * @Date: 2021-06-07 10:07:49
 * @LastEditTime: 2021-11-17 09:21:14
 * @FilePath: \IAPlatform\src\main.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 项目加载的入口文件，一个全局的项目文件。主要有3个作用
 * 1.实例化vue。 2.放置项目中经常使用到的插件和css样式。 3.存储全局变量。
 */

import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import echarts from 'echarts'
import axios from 'axios'
import common from './utils/common'
Vue.config.client = true
Vue.config.productionTip = false
Vue.prototype.$echarts = echarts
Vue.prototype.axios = axios
/**将common这个公共 文件挂载到vue实例下 */
Vue.prototype.common = common

Vue.use(ElementUI, {
	Szie: 'small',
	zIndex: 3000
})

/**
 * dateTimeFormat是一个全局的对后台返回时间进行本地格式化的一个vue过滤器
 * @param {date} 2021-07-20T01:44:47.000Z
 */
Vue.filter('dateTimeFormat', (date) => {
	function addDateZero(num) {
		return num < 10 ? "0" + num : num;
	}
	let d = new Date(date);
	let formatdatetime =
		d.getFullYear() +
		"-" +
		addDateZero(d.getMonth() + 1) +
		"-" +
		addDateZero(d.getDate()) +
		" " +
		addDateZero(d.getHours()) +
		":" +
		addDateZero(d.getMinutes()) +
		":" +
		addDateZero(d.getSeconds());
	return formatdatetime;
})
Vue.filter('transMode', (mode) => {
	if (mode == 1) {
		return '自动';
	} else {
		return '手动';
	}
})
new Vue({
	el: '#app',
	router,
	components: { App },
	template: '<App/>'
})
