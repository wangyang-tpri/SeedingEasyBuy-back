<!--
 * @Author: wangyang-tpri
 * @Date: 2021-11-04 09:01:57
 * @LastEditTime: 2021-12-08 10:51:11
 * @FilePath: \IAPlatform\src\components\deleteBox.vue
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 
-->
<template>
    <div :key="keyNum">
        <date-search
            :delDate="selectSendData"
            :status="3"
            @upDate="getChildDate"
        >

        </date-search>
        <v-box 
            :tableData="data"
            :total="total"
            @selectDateChild="getSelectDate"    
            :key="keyNum"
            :distinguishBox="selfName"
        ></v-box>
    </div>
</template>

<script>
import selfRequest from '../utils/api'
import box from './box.vue'
import dateSearchVue from './dateSearch.vue'
export default {
    name: 'deleteBox',
    components: {
        'v-box': box,
        'date-search': dateSearchVue
    },
    data() {
        return {
            keyNum: 0,
            total: 0,
            data: "",
            selectSendData: '',
            selfName: 'delete'
        }
    }, 
    methods: {
        getDeleteData(){
            selfRequest.getDeleteData().then((res) => {
                this.data = res.data;
                this.total = this.data.length;
            })
        },
        getSelectDate(val){
            this.selectSendData = val;
        },
        getChildDate(val){
            val == 'rowId' ? this.keyNum++ : this.data = val;
        },
    },
    mounted(){
        this.getDeleteData();
    }   
}
</script>

<style scoped>

</style>