<!--
 * @Author: wangyang-tpri
 * @Date: 2021-07-30 08:51:00
 * @LastEditTime: 2021-07-30 14:29:47
 * @FilePath: \firstVue\src\components\vueApi.vue
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 学习vue框架中api的文件
-->

<template>
    <div>
        {{ msg }}
        <div id="mount">创建的vue构造器挂载带 元素mount上</div>
        <button @click="updateMessage">nextTick</button>
    </div>
</template>

<script>
/**
 *  vue无法检测property的添加和移除 所以property必须在data对象上存在才能让vue将它
 *  转换成响应式的
 *  数据驱动
 * 单vue文件中的this指向全局 代表的是vue根实例  不是当前的 $el
 */
import Vue from "vue";
export default {
    name: "vueApi",
    data() {
        return {
            msg: "学习vue中api的",
            profile: "",
            items: ["a", "b", "c"],
            next: "未更新",
        };
    },
    methods: {
        changeItemList() {
            this.items[1] = 10;
        },
        createInstaceVue() {
            /**
             * vue.extend()
             * 并且回调函数中的this将自动绑定到当前实例上
             */
            this.profile = Vue.extend({
                template: "<div>{{name}}</div>",
                data() {
                    return {
                        name: "extend",
                    };
                },
            });
            new this.profile().$mount("#mount");
        },
        updateMessage() {
            console.log(this.common)
            this.next = "已更新";
            console.log(this.next);
            this.$nextTick(() => {
                console.log(this.next);
            });
        },
    },

    mounted() {
        this.createInstaceVue();
        this.changeItemList();
        console.log(this.items[1]);
        this.updateMessage();
    },
};
</script>