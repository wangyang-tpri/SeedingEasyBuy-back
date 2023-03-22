<template>
    <div :key="refreshNum">
        <div id="sbox-header">
            <div>
                <span class="demonstration">开始时间</span>
                <el-date-picker
                    v-model="startDate"
                    type="date"
                    :placeholder="dateHolder"
                    value-format="yyyy-MM-dd"
                >
                </el-date-picker>
            </div>
            <div>
                <span class="demonstration">结束时间</span>
                <el-date-picker
                    v-model="endDate"
                    type="date"
                    :placeholder="dateHolder"
                    value-format="yyyy-MM-dd"
                    @change="checkDate"
                >
                </el-date-picker>
            </div>
            <div>
                <el-button
                    type="primary"
                    @click="search()"
                    :disabled="btnDisabled"
                    >查询</el-button
                >
                <el-button type="primary" @click="delDataFunc"
                    >删除</el-button
                >
            </div>
        </div>

        <s-box
            :tableData="data"
            :total="total"
            @selectChildData="getChildSelectData"
            :key="refreshNum"
            :boxStatus="1"
        ></s-box>
    </div>
</template>
<script>
import sBox from "./sBox.vue";
import common from "../utils/common";
import selfRequest from "../utils/api";
export default {
    components: {
        "s-box": sBox,
    },
    data() {
        return {
            msg: "发件成功",
            data: "",
            dateHolder: common.dateHolder,
            startDate: "",
            endDate: "",
            alertDateError: {
                message: "开始时间不能大于结束时间",
                type: "error",
            },
            btnDisabled: true,
            selectData: [],
            refreshNum: 0,
            total: 0,
            userId: 1
        };
    },
    methods: {
        search() {
            common.timeEfficiency(this.startDate, this.endDate) &&
                this.$message(this.alertDateError);
            this.getSuccessDatePeriod(this.startDate, this.endDate);
            // 点击查询按钮 从后台请求给定时间段中的数据
        },
        checkDate() {
            if (common.timeEfficiency(this.startDate, this.endDate)) {
                this.$message(this.alertDateError);
                this.btnDisabled = true;
            } else {
                this.btnDisabled = false;
            }
        },
        async getSuccessDatePeriod(sDate, eDate) {
           let res =  await selfRequest.getSuccessDatePeriod(sDate, eDate, this.userId);
           this.data = res.data;
        },
        async getSuccessData() {
            let res = await selfRequest.getSuccessBox(this.userId);
            this.data = res.data;
            this.total = this.data.length;
        },
        getChildSelectData(val) {
            this.selectData = val;
        },
        delDataFunc() {
            let data = this.selectData;
            this.common.confirmDelete(this, data, selfRequest.deleteRowSuccessBox)
        },
    },
    mounted() {
        this.userId = sessionStorage.getItem('userId');
        this.getSuccessData();
    },
};
</script>

<style scoped>
#sbox-header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-bottom: 20px;
}
</style>