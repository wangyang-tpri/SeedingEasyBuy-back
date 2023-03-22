<!--
 * @Author: wangyang-tpri
 * @Date: 2021-12-14 08:40:59
 * @LastEditTime: 2021-12-15 09:58:41
 * @FilePath: \IAPlatform\src\components\keyWord.vue
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 关键词过滤组件
-->
<template>
    <div>
        <el-button type="primary" size="big" @click.prevent.native="addKeyWord"
            >新增关键词</el-button
        >
        <el-dialog :visible.sync="isShow" width="40%">
            <el-input v-model="word" placeholder="请输入关键词"></el-input>
            <span slot="footer" class="dialog-footer">
                <el-button @click="isShow = false">{{ cancelBtn }}</el-button>
                <el-button @click="confirmDialog" type="primary">{{
                    checkBtn
                }}</el-button>
            </span>
        </el-dialog>
        <div style="margin-top:20px">
            <span
                v-show="keyWordArr.length > 0"
                v-for="item in keyWordArr"
                :key="item"
                style="margin-left: 20px"
                ><el-tag v-show="item" closable @close="handleClose(item)">{{
                    item
                }}</el-tag></span
            >
        </div>
    </div>
</template>
<script>
import selfRequest from '../utils/api'
export default {
    name: "keyWord",
    data() {
        return {
            msg: "关键词过滤页面",
            keyWordArr: [],
            isShow: false,
            word: "",
            cancelBtn: "取消",
            checkBtn: "确认",
        };
    },
    methods: {
        addKeyWord() {
            this.isShow = true;
        },
        confirmDialog() {
            this.isShow = false;
            this.keyWordArr.push(this.word);
            selfRequest.saveKeyWords(this.word).then((res) => {})
            this.word = '';
        },
        async handleClose(tag) {
            let index = this.keyWordArr.indexOf(tag);
            this.keyWordArr.splice(index, 1);
            let res = await selfRequest.deleteKeyWord(tag);
        },
        async getKeyWords(){
            let res = await selfRequest.getKeyWords();
            let data = res.data;
            data.forEach(element => {
                this.keyWordArr.push(element.words)
            });
        },
    },
    mounted(){
        this.getKeyWords()
    }
};
</script>
