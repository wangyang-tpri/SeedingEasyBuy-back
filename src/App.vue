<template>
    <div id="app" style="height: 100%">
        <router-view></router-view>
        <!-- <el-container>
      <el-header style=" height: 72px; padding: 0;">
        <v-header></v-header>
      </el-header>
      <el-container style="height: 100%">
        <div :style="slideCla">
          <el-aside style="width: 240px">
            <v-slider></v-slider>
          </el-aside>

        </div>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>


    </el-container>     -->
    </div>
</template>

<script>
import footer from "./footer";
import leftSlider from "./leftSlider/leftSlider";
import header from "./header.vue";
export default {
    name: "App",
    // 在components这个对象中对要使用的组件进行声明
    components: {
        "v-footer": footer,
        "v-slider": leftSlider,
        "v-header": header,
    },
    data() {
        return {
            ba: {
                background: "yellow",
            },
            slideCla: {
                width: "240px",
                height: "100%",
                background: "#f2f2f2",
                position: "staticky",
                top: "10px",
            },
        };
    },
    created() {
        this.slideCla.height = document.body.clientHeight - 72 + "px";
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
</style>
