<template>
    <div id="slider">
        <div class="nav-item" v-for="(item, index) in itemOptions" :key="item.value" @click="item.func(index)" 
        :class="{active: selfNum == index}"
        >
            <i :class='item.class'></i>
        </div>
    </div>
</template>
<script>
import sendFailVue from '../components/sendFail.vue';
export default {
    name: 'slider',
    data(){
        return {
            itemOptions: [
                {value: '新建信息', class: 'el-icon-circle-plus-outline', func: this.createMessage},
                {value: '发件箱', class: 'el-icon-position', func: this.sendMessage},
                // {value: '收件箱', class: 'el-icon-message', func: this.inboxMessage},
                {value: '发送成功', class: 'el-icon-check', func: this.postSuccess},
                {value: '发送失败', class: 'el-icon-close', func: this.postFail},
                {value: '用户组', class: 'el-icon-coordinate', func: this.userGroup},
                // {value: '详情页', class: 'el-icon-tickets', func: this.msgDetails},
                {value: '回收站', class:'el-icon-delete', func: this.deleteBox},
                {value: '关键词过滤', class: 'el-icon-news', func:this.keyWordFilter}
            ],
            selfNum: 0,
            activeName: '',
            currentIndex: 0,
        }
    },
    methods: {
        // 高亮左侧的导航栏
        hightItem(index){
            this.selfNum = index;
            localStorage.setItem('index', index)
        },
        createMessage(){
            this.$router.push({
                path: '/home/createMsg'
            })
            this.hightItem(0)
        },
        sendMessage(){
            this.$router.push({
                path: '/home/sendBox'
            })
            this.hightItem(1)
        },
        // inboxMessage(){
        //     this.$router.push({
        //         path: '/home/inbox'
        //     })
        //     this.hightItem(2)
        // },
        postSuccess(){
            
            this.$router.push({
                path: '/home/sendSuccess'
            })
            this.hightItem(2)
        },
        postFail(){
            this.$router.push({
                path: '/home/sendFail'
            })
            this.hightItem(3)
        },
        userGroup(){
            
            this.$router.push({
                path: '/home/userGroup'
            })
            this.hightItem(4)
        },
        msgDetails(){
            
            this.$router.push({
                path: '/home/msgDetail'
            })
            this.hightItem(5)
        },
        deleteBox(){
            
            this.$router.push({
                path: '/home/deleteBox'
            })
            this.hightItem(5)
            window.location.reload();
        },
        keyWordFilter(){
            this.$router.push({
                path: '/home/wordFilter',
            })
            this.hightItem(6)
            window.location.reload()
        }
    },
    created() {
        var selfNum = localStorage.getItem('index');
        !selfNum && selfNum == 0;
        var itemList = document.getElementsByClassName('nav-item');
        itemList.length > 0 && itemList[Number(selfNum)].classList.add('active');
    },
    updated() {
        var selfNum = localStorage.getItem('index');
        var itemList = document.getElementsByClassName('nav-item');
        itemList[Number(selfNum)].classList.add('active');
    },
    mounted() {
        var selfNum = this.selfNum = localStorage.getItem('index');
        !selfNum && selfNum == 0;
        var itemList = document.getElementsByClassName('nav-item');
        itemList[Number(selfNum)].classList.add('active');
        sessionStorage.getItem('loginName') != "tpri" && this.itemOptions.splice(5, 1)
    },
}
</script>

<style scoped>
#slider {
    width: 100%;
    float: left;
    padding: 20px 0;
}
i {
    font-size: 20px;
    display: inline-block;
    cursor: pointer;
    vertical-align: middle;
    margin-right: 10px;
}
.nav-item {
    height: 40px;
    margin: 0 6px;
    margin-bottom: 10px;
    border-radius: 3px;
    padding-left: 25px;
    cursor: pointer;
    line-height: 40px;
    text-align: left;
}
.nav-item:hover{
    background: #fff;
}
.nav-item.active{
    color: #fff;
    background: #0066ff;
}
.self-delete{
    background: url(../assets/ashbin.png) no-repeat 5px 5px;
}
</style>