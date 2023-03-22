<template>
    <div :key="keyNum">
        <div>
            <date-search
                :delDate="selectDate"
                status="1" 
                @upDate="getChildDate"
            ></date-search>
        </div>
        <v-box 
            :tableData="tableData" 
            :distinguishBox="1"
            @selectDateChild="getSelectDate"
            :key="keyNum" 

        ></v-box>        
    </div>
</template>
<script>
import box from './box.vue'
import dateSearch from './dateSearch.vue'
import selfRequest from '../utils/api'
/**
 * 对单vue文件中的根div元素使用id选择器
 * 声明组件使用字母全大写 再DOM中使用时字母间用 - 连接 
 * 会有一个新的组件dateSearch  包含起始时间 查询 删除 
 *      从子组件中dateSearch中请求根据起始时间获取到的数据 
 *          然后根据父子组件间的通信 将子组件dateSearch中获取到的数据 传递给 父组件 inbox
 *              父组件 inbox 再将数据data传递给子组件 box 表格进行渲染
*/
export default {
    components: {
        'v-box': box,
        'date-search': dateSearch
    },
    name: 'inbox',
    data() {
        return {
            tableData: '',
            selectDate: '',
            keyNum: 0
        }
    },
    methods: {

        getChildDate(date){
            date === 'rowId' ? this.keyNum++ : this.tableData = date;
            // this.tableData = date
        },
        getSelectDate(date){
            console.log(date)
            this.selectDate = date;
        },
        // 通过axios请求后台的数据 通过prop将获取到的数据传递到子组件中
        // 子组件中将父组件传递过来的数据进行渲染
        getInBoxData(){
            selfRequest.getInbox().then((res) => {
                this.tableData = res.data;       
            })
        }
    },
    mounted() {
        this.getInBoxData()
    },
    updated() {
    },
}
</script>

