
<template>
    <div class="hello">
        <echarts_k/>
        <h1>{{ msg }}</h1>
        <h2>Essential Links</h2>
        <ul>
            <li>
                <a href="https://vuejs.org" target="_blank"> Core Docs </a>
            </li>
            <li>
                <a href="https://forum.vuejs.org" target="_blank"> Forum </a>
            </li>
            <li>
                <a href="https://chat.vuejs.org" target="_blank">
                    Community Chat
                </a>
            </li>
            <li>
                <a href="https://twitter.com/vuejs" target="_blank">
                    Twitter
                </a>
            </li>
            <br />
            <li>
                <a
                    href="http://vuejs-templates.github.io/webpack/"
                    target="_blank"
                >
                    Docs for This Template
                </a>
            </li>
        </ul>
        <h2>Ecosystem</h2>

        <ul>
            <li>
                <a href="http://router.vuejs.org/" target="_blank">
                    vue-router
                </a>
            </li>
            <li>
                <a href="http://vuex.vuejs.org/" target="_blank"> vuex </a>
            </li>
            <li>
                <a href="http://vue-loader.vuejs.org/" target="_blank">
                    vue-loader
                </a>
            </li>
            <li>
                <a href="https://github.com/vuejs/awesome-vue" target="_blank">
                    awesome-vue
                </a>
            </li>
        </ul>
        <router-link to="/drag">
          <button>跳转到drag页面</button>
        </router-link>
        <button @click="jumpToDrag()">drag</button>
        <h2 @click="getUrlQuery()">获取从drag页面传递过来的参数</h2>
        <h1>{{ urlMsg }}</h1>
    </div>
</template>

<script>
import echarts_k from './componetK/drag'
// export default 中可以写很多东西 包括变量 方法 对象等
// data指的是在该组件中定义的模板数据 
// 每一个vue组件都是挂载在一个vue实例上面的   
// 除了数据property vue实例还暴露了一些有用的实例property方法。 他们都有$前缀
// 就是为了和用户自己的property进行区分
export default {
    name: "HelloWorld",
    components: {
        echarts_k
    },
    data() {
        return {
            msg: "Welcome to Your Vue.js App",
            urlMsg: null
        };
    },
    methods: {
        jumpToDrag() {
            //跳转到drag页面
            console.log(this.$route.query)

        },
        getUrlQuery(){
            this.urlMsg = this.$route.query.message
        }
    },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* // scoped表示样式现在只作用于当前作用域下的模块  用于实现样式私有化的目的
// 为什么要谨慎使用 
    // 样式不易修改   */

    /* 谨慎使用  不是不用 而是要带着谨慎的态度去使用   */
h1,
h2 {
    font-weight: normal;
}
ul {
    list-style-type: none;
    padding: 0;
}
li {
    display: inline-block;
    margin: 0 10px;
}
a {
    color: #42b983;
}
</style>
