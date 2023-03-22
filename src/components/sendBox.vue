<!--
 * @Author: wangyang-tpri
 * @Date: 2021-11-04 09:01:58
 * @LastEditTime: 2021-12-08 10:46:59
 * @FilePath: \IAPlatform\src\components\sendBox.vue
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 
-->
<template>
    <div :key="keyNum">
        <date-search
            :status="2"
            :delDate="selectSendDate"
            @upDate="getChildDate"
        ></date-search>
        <v-box 
            :tableData="tableData"
            :distinguishBox="selfName"
            :total="total"
            @selectDateChild="getSelectDate"    
            :key="keyNum"
        ></v-box>
    </div>
</template>

<script>
import box from './box.vue'
import dateSearch from './dateSearch.vue'
import selfRequest from '../utils/api'
export default {
    components: {
        'v-box': box,
        'date-search': dateSearch,
    },
    data() {
        return {
            tableData: [
                {
                    userName: "tpri",
                    title: "发件箱",
                    date: "2021-06-09",
                    pid: 1
                },
                {
                    userName: "tpri2",
                    title: "信息应用平台",
                    date: "2021-05-09"
                }
            ],
            multipleSelection: [],
            selectRow: [],
            selectSendDate: '',
            keyNum: 0,
            total: 0,
            userId: 1,
            selfName: 'send'
        };
    },

    methods: {
        delectSelect(){

        },
        getChildDate(date){
            date == 'rowId' ? this.keyNum++ : this.tableData = date;
        },
        getSelectDate(date){
            this.selectSendDate = date;
        },
        getSendData(){
            selfRequest.getSendBox(this.userId).then(res => {
                // 对从后台返回的数据进行处理
                this.tableData = res.data;
                this.total = this.tableData.length;
            })
        }
    },
    // 计算属性
    computed:{

    },
    // 实例挂载时进行 数据的请求 并且进行渲染
    mounted() {
        this.userId = sessionStorage.getItem('userId')
        this.getSendData()
    },
    updated() {
    },
};
</script>

<style scoped>

</style>