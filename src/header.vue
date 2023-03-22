<template>
    <div id="header" class="header">
        <div class="header-left">{{ msg }}</div>
        <div class="header-right">
            <div @click="showDropItem">
                {{ userName }}

                <i class="el-icon-arrow-down" style="font-weight: bold"></i>
            </div>
            <div class="dropdown-list layer" v-show="isDropItem">
                <div
                    class="drop-item"
                    v-for="item in userItem"                                
                    :key="item.value"
                    @click="item.func"   
                >
                    {{ item.value }}
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            msg: "信息应用平台",
            userName: "",
            userItem: [{ value: "个人中心", func: this.userInfo }, { value: "退出", func: this.loginOut }],
            isDropItem: false,
        };
    },
    methods: {
        showDropItem(){
            this.isDropItem = !this.isDropItem;
        },
        userInfo(){
            return '跳转到用户个人信息页面';
        },  
        loginOut(){
            let outHref = location.href.split('#')[0];
            location.href = outHref;
            sessionStorage.setItem('token', ''); 
            location.reload();
        }
    },
    mounted(){
        this.userName = sessionStorage.getItem('loginName');
    }
    
    
};
</script>
<style lang="">
.header {
    position: relative;
    display: flex;
    justify-content: space-between;
    background-image:  linear-gradient(rgba(100,100,0,0.3), rgba(50, 50,0, 0.0)),url('./assets/headerPng.jfif');
    background-repeat: repeat-x;
    background-size: 100% 100%;
}
.header-left {
    display: flex;
    align-items: center;
    font-size: 24px;
    padding-left: 20px;
    color: #0066ff;
    font-weight: bold;
    letter-spacing: 5px;
}
.header-right {
    display: flex;
    cursor: pointer;
    margin-right: 20px;
    padding-right: 20px;
    text-align: center;
}
.dropdown-list {
    min-width: 100px;
    padding: 6px 0;
    right: 20px;
    position: absolute;
    top: 52px;
    border-radius: 3px;
    z-index: 500;
    background: #fff;
    box-shadow: 0 2px 8px rgb(0 0 0 / 20%);
}
.drop-item {
    display: block;
    cursor: pointer;
    line-height: 30px;
    white-space: nowrap;
}
.drop-item:hover {
    background: #ddd;
    color: black;
}
</style>