<template>
    <div id="login-con">
        <div id="login-top"></div>
        <div id="login-bot"></div>
        <div id="login-cet">
            <div id="login-cet-left"></div>
            <div id="login-cet-right">
                <el-form
                    :ref="form"
                    :model="form"
                    label-width="80px"
                    style="margin-left: -40px"
                >
                    <el-form-item label="用户名">
                        <el-input v-model="form.name" style="width: 240px"></el-input>
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input v-model="form.password" style="width: 240px" type="password"></el-input>
                    </el-form-item>
                    <el-form-item>

                        <el-button type="primary" style="width: 240px" @click="submitData">登录</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    data() {
        return {
            msg: '信息应用平台登录',
            form: {
                name: '',
                password: ''
            }
        }
    },
    methods: {
        submitData(){
            console.log(this.form.name)
            console.log(this.form.password)
        }
    },
}
</script>

<style scoped>
#login-con {
    height: 100%;
    width: 100%;
    position: relative;
}
#login-top{
    height: 45%;
    background-image: linear-gradient(rgba(18,107,189,0.8), rgba(18,107,189, 0.8)),url('../assets/boxcity.png');
    background-repeat: repeat-x;
    background-size: 100% 100%;
}
#login-bot {
    height: 55%;
    background: #e7e8ea;
}
#login-cet {
    position: absolute;
    width: 700px;
    height: 300px;
    border: 1px solid white;
    top: 50%;
    left: 50%;
    margin-left: -350px;
    margin-top: -100px;
    background: white;
    padding: 10px 20px;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
#login-cet div{
    width: 45%;
}
#login-cet-left {
    background-image: url('../assets/box.png');
    background-size: 80% 60%;
    background-repeat: no-repeat;
    background-position: 40% 40%;
    background-color: white;
}
#login-cet-right {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}
</style>