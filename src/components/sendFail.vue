<template>
    <div :key="refreshNum">
        <div id="sbox-header">
            <div>
                <span class="demonstration">{{ sTitle }}</span>
                <el-date-picker
                    v-model="startDate"
                    type="date"
                    :placeholder="dateHolder"
                    value-format="yyyy-MM-dd"
                >
                </el-date-picker>
            </div>
            <div>
                <span class="demonstration">{{ eTitle }}</span>
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
                <el-button type="primary" @click="delDataFunc">删除</el-button>
            </div>
        </div>
        <s-box
            :tableData="data"
            :total="total"
            :boxStatus="0"
            @selectChildData="getSelectChildData"
            :key="refreshNum"
        ></s-box>
    </div>
</template>

<script>
import sBox from "./sBox.vue";
import selfRequest from "../utils/api";
import common from "../utils/common";
export default {
    components: {
        "s-box": sBox,
    },
    data() {
        return {
            sTitle: common.sTitle,
            eTitle: common.eTitle,
            data: "",
            dateHolder: common.dateHolder,
            startDate: "",
            endDate: "",
            btnDisabled: true,
            alertDateError: "开始时间不能大于结束时间",
            childData: [],
            refreshNum: 0,
            total: 0,
        };
    },
    methods: {
        getFailData() {
            selfRequest.getFailBox().then((res) => {
                this.data = res.data;
                this.total = this.data.length;
            });
        },
        search() {
            common.timeEfficiency(this.startDate, this.endDate) &&
                this.$message(this.alterDateError);
            this.getFailDatePeriodBack(this.startDate, this.endDate);
            // 点击查询按钮 从后台请求给定时间段中的数据
        },
        checkDate() {
            if (common.timeEfficiency(this.startDate, this.endDate)) {
                this.$message.error(this.alertDateError);
                this.btnDisabled = true;
            } else {
                this.btnDisabled = false;
            }
        },
        getFailDatePeriodBack(sDate, eDate) {
            selfRequest.getFailDatePeriod(sDate, eDate).then((res) => {
                this.data = res.data;
            });
        },
        getSelectChildData(val) {
            this.childData = val;
        },
        delDataFunc() {
            let data = this.childData;
            this.common.confirmDelete(this, data, selfRequest.deleteRowFailBox);
        },
    },
    created() {},
    mounted() {
        this.getFailData();
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