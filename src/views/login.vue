<template>
    <div style="height: 100%; width: 100%; position: relative">
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
                            <el-input
                                v-model="form.name"
                                style="width: 240px"
                                ref = "inputName"
                            ></el-input>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-input
                                v-model="form.password"
                                style="width: 240px"
                                type="password"
                                @input="inputPassword"
                            ></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button
                                id="loginBtn"
                                type="primary"
                                style="width: 240px"
                                @click="submitData"
                                :disabled="loginDisa"
                                >登录</el-button
                            >
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>
        <div id="mask">
            <div id="jigsaw"></div>
        </div>
    </div>
</template>

<script>
import selfRequest from "../utils/api";
import { Message } from "element-ui";
import drawImage from '../utils/slider'
export default {
    data() {
        return {
            msg: "信息应用平台登录",
            form: {
                name: "",
                password: "",
            },
            loginDisa: true,
            showMask: false,
            jigsawEle: "",
            el: ''
        };
    },
    methods: {
        submitData() {
            this.showMask = true;
            document.getElementById('mask').style.display = 'block';
            document.getElementById('jigsaw').style.display = 'block';            
        },
        checkUser(){
            selfRequest.loginUser(this.form.name, this.form.password).then(
                (res) => {
                    if (res.data.code == 2) {
                        Message.warning("用户名或密码错误");
                        setTimeout(() => {location.reload()}, 2000)
                    } else {
                        sessionStorage.setItem("token", res.data.token);
                        sessionStorage.setItem("userId" ,res.data.userId);
                        sessionStorage.setItem("loginName", this.form.name);
                        sessionStorage.setItem("userControl", res.data.role);
                        selfRequest.selfInstance.defaults.headers.Authorization =
                            res.data.token;
                        this.$router.push({
                            path: "/home/createMsg",
                        });
                    }
                },
                (err) => {
                    location.reload();
                }
            );
        },
        inputPassword(){
            this.loginDisa = !(this.form.name && this.form.password)
        },
        /**
         * 回车事件
         */
        keyW(){
            document.addEventListener('keydown', (e) => {
                if (this.loginDisa) return;
                let thisEvent = e || window.event;
                let keyCode = thisEvent.keyCode || thisEvent.which || thisEvent.charCode;
                if (keyCode == 13){
                    this.submitData();
                }
            })

        }
    },
    mounted() {
        this.el = document.getElementById('jigsaw');
        localStorage.setItem('index', 0)
        drawImage.init({
            el: this.el,
            onsuccess: this.checkUser
        })
        this.keyW();
        this.$nextTick(() => {
            this.$refs.inputName.focus();
        })
    },
};
</script>

<style>
#login-con {
    height: 100%;
    width: 100%;
    position: relative;
}
#login-top {
    height: 45%;
    background-image: linear-gradient(
            rgba(18, 107, 189, 0.8),
            rgba(18, 107, 189, 0.8)
        ),
        url("../assets/city2.jpg");
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
#login-cet div {
    width: 45%;
}
#login-cet-left {
    background-image: url("../assets/box.png");
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
div#jigsaw {
    width: 310px;
    height: 200px;
    position: absolute;
    top: 50%;
    left: 50%;
    margin-top: -50px;
    margin-left: -150px;
    border: 1px #ddd solid;
    background: #ddd;
    z-index: 310;
    border-radius: 3px;
    display: none;
}
div#mask {
    width: 100%;
    height: 100%;
    z-index: 300;
    background: rgba(0, 0, 0, 0.5);
    position: absolute;
    top: 0;
    display: none;
}

.block {
    position: absolute;
    left: 0;
    top: 0;
}

.sliderContainer {
    position: relative;
    text-align: center;
    width: 310px;
    height: 40px;
    line-height: 40px;
    /* margin-top: 15px; */
    background: #f7f9fa;
    color: #45494c;
    border: 1px solid #e4e7eb;
}

.sliderContainer_active .slider {
    height: 38px;
    border: 1px solid #1991fa;
}

.sliderContainer_active .sliderMask {
    height: 38px;
    border-width: 1px;
}

.sliderContainer_success .slider {
    height: 38px;
    top: -1px;
    border: 1px solid #52ccba;
    background-color: #52ccba !important;
}

.sliderContainer_success .sliderMask {
    height: 38px;
    background-color: #d2f4ef;
}

.sliderContainer_success .sliderIcon {
    background-position: 0 0 !important;
}

.sliderContainer_fail .slider {
    height: 38px;
    top: -1px;
    border: 1px solid #f57a7a;
    background-color: #f57a7a !important;
}

.sliderContainer_fail .sliderMask {
    height: 38px;
    border: 1px solid #f57a7a;
    background-color: #fce1e1;
}

.sliderContainer_fail .sliderIcon {
    top: 14px;
    background-position: 0 -82px !important;
}
.sliderContainer_active .sliderText,
.sliderContainer_success .sliderText {
    display: none;
}

.sliderMask {
    position: absolute;
    left: 0;
    top: 0;
    height: 40px;
    background: #d1e9fe;
}

.slider {
    position: absolute;
    top: 0;
    left: 0;
    width: 40px;
    height: 39px;
    background: #fff;
    box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
    cursor: pointer;
    transition: background 0.2s linear;
}

.slider:hover {
    background: #1991fa;
}

.slider:hover .sliderIcon {
    background-position: 0 -13px;
}

.sliderIcon {
    position: absolute;
    top: 15px;
    left: 13px;
    width: 14px;
    height: 12px;
    background: url("../assets/icon_right.png") 0 -26px;
    background-size: 34px 471px;
}

.refreshIcon {
    position: absolute;
    right: 0;
    top: 0;
    width: 34px;
    height: 34px;
    cursor: pointer;
    background: url("../assets/icon_right.png") 0 -437px;
    background-color: green;
    color: black;
    background-size: 34px 471px;
}
</style>