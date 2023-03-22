<template>
    <div style="height: 100%">
        <el-container style="height: 100%">
            <el-header style="height: 72px; padding: 0">
                <v-header></v-header>
            </el-header>
            <el-container >
                <div :style="[slideCla]" :class="{ iconWidth: showIcon }" class="leftBackImage">
                    <el-aside :style="{ width: (showIcon ? 80 : 190) + 'px' }">
                        <v-slider v-if="!showIcon"></v-slider>
                        <v-icon v-if="showIcon"></v-icon>
                    </el-aside>
                    <span
                        class="icon-parent"
                        :style="{ left: (showIcon ? 90 : 95) + '%' }"
                    >
                        <i
                            class="icon"
                            @click="showSlideIcon"
                            :class="[!showIcon ? leftIcon : rightIcon]"
                            :style="{ left: (showIcon ? 0 : 0) + '%' }"
                        ></i>
                    </span>
                </div>
                <el-main>
                    <router-view></router-view>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
import footer from "../footer";
import leftSlider from "../leftSlider/leftSlider";
import sliderIcon from "../leftSlider/sliderIcon";
import header from "../header.vue";
export default {
    name: "App",
    components: {
        "v-footer": footer,
        "v-slider": leftSlider,
        "v-header": header,
        "v-icon": sliderIcon,
    },
    data() {
        return {
            ba: {
                background: "yellow",
            },
            iconWith: { width: "90px" },
            slideCla: {
                width: "200px",
                height: "100%",
                position: "relative",
            },
            rightIcon: "el-icon-caret-right",
            leftIcon: "el-icon-caret-left",
            rightIconPosi: {
                left: "10%",
            },
            showIcon: false,
            itemConut: 0
        };
    },
    methods: {
        showSlideIcon() {
            this.showIcon = !this.showIcon;
        },
    },
    created() {
        // this.slideCla.height = document.body.clientHeight - 72 + "px";
        // this.slideCla.height = 
    },
    mounted() {
    },
};
</script>

<style>
html,
body {
    margin: 0;
    height: 100%;
}
#app {
    font-family: "Avenir", Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    width: 100%;
}
#header {
    width: 100%;
    height: 72px;
    background-color: #f8f8f8;
    line-height: 72px;
    box-shadow: 0 0 10px rgb(0 0 0 / 15%);
}
#container {
    width: 100%;
    margin-top: 10px;
    margin-bottom: 10px;
}
#tem {
    width: 89.3%;
    float: left;
    background: #e5e9f2;
    margin-left: 10px;
}
.iconWidth {
    width: 90px !important;
}
.icon {
    cursor: pointer;
    font-size: 26px;
    position: absolute;
    top: 20%;
}
.icon-parent {
    background: #fff;
    width: 30px;
    height: 50px;
    display: inline-block;
    position: absolute;
    top: 40%;
    left: 90%;
}
.leftBackImage{
    background-image: linear-gradient(rgba(130,120,0,0.1), rgba(70, 150,0, 0.3)), url('../assets/left.jpg');
    background-repeat: repeat-x;
    background-size: 100% 100%;
}
</style>
