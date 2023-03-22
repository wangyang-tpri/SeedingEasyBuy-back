<template>
    <div id="dateContainer">
        <div>
            <span>{{ sTitle }}</span>
            <el-date-picker
                v-model="startDate"
                type="date"
                :placeholder="dateHolder"
                value-format="yyyy-MM-dd"
            ></el-date-picker>
        </div>
        <div>
            <span>{{ eTitle }}</span>
            <el-date-picker
                v-model="endDate"
                type="date"
                :placeholder="dateHolder"
                value-format="yyyy-MM-dd"
                @change="changeDate"
            ></el-date-picker>
        </div>
        <div>
            <el-button
                type="primary"
                @click="passUpDate"
                :disabled="btnDisabled"
                >{{ searchBtn }}</el-button
            >
            <el-button type="primary" @click="delDateFunc">{{
                deleteBtn
            }}</el-button>
        </div>
    </div>
</template>

<script>
/**
 * status == 1 代表的是收件箱
 * status == 2 代表的是发件箱
 */
import common from "../utils/common";
import selfRequest from "../utils/api";

/**
 * 在此组件中进行父组件 inbox 中数据的查询 和删除功能
 * $emit() 触发当前实例上的事件 附件参数都会传给监听器回调
 * 一个父组件下面会有多个子组件
 * 一套代码 多端运行
 */
export default {
    name: "DateSearch",
    props: ["delDate", "status"],
    data() {
        return {
            dateHolder: common.dateHolder,
            sTitle: common.sTitle,
            eTitle: common.eTitle,
            deleteBtn: common.deleteBtn,
            searchBtn: common.searchBtn,
            startDate: "",
            endDate: "",
            btnDisabled: true,
            date: [],
            selfDelDate: this.delDate,
            alertDateError: "开始时间不能大于结束时间",
            userId: 1,
            parentFunc: ['', selfRequest.deleteRowInBox, selfRequest.deleteRowSendBox, selfRequest.deleteRowDeleteBox]
        };
    },
    /**
     * resolve
     * reject
     * pending
     */
    methods: {
        passUpDate() {
            let dataObject;
            common.timeEfficiency(this.startDate, this.endDate) &&
                this.$message(this.alertDateError);
            if (Number(this.status) === 1) {
                dataObject = selfRequest.getDatePeriodInbox(
                    this.startDate,
                    this.endDate,
                    this.userId
                );
            } else {
                dataObject = selfRequest.getDatePeriodSendBox(
                    this.startDate,
                    this.endDate,
                    this.userId
                );
            }
            this.disposeCallBackData(dataObject);
        },
        delDateFunc() {
            // 在此对选择的信息进行批量删除
            let selfStatus = this.status;
            let delDate = this.delDate;
            let self = this;
            this.$confirm("确认要删除这些信息吗?", "提示", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
            })
                .then(() => {
                    for (let i = 0; i < delDate.length; i++) {
                        let rowData = delDate[i];
                        let parent = Number(selfStatus);
                        this.parentGetDataBasedOnTime(rowData, parent);
                    }
                    setTimeout( () => {
                        location.reload();
                    })
                    self.$emit("upDate", "rowId");
                    self.$message({
                        type: "success",
                        message: "信息删除成功",
                    });
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除",
                    });
                });
        },
        /**现在需要对日期选择框进行一些时间选择的大小限制 提示框 */
        changeDate() {
            if (common.timeEfficiency(this.startDate, this.endDate)) {
                this.$message.error(this.alertDateError);
                this.btnDisabled = true;
            } else {
                this.btnDisabled = false;
            }
        },
        getDatePeriod() {
            /** */
        },
        disposeCallBackData(dataObject) {
            dataObject &&
                dataObject.then(
                    (res) => {
                        // 处理完数据
                        this.$emit("upDate", res.data);
                    },
                    (err) => {
                        console.log(err);
                    }
                );
        },
        parentGetDataBasedOnTime(rowData, whichParent) {
            if (whichParent == 1) this.parentFunc[1](rowData.in_id);
            if (whichParent == 2) this.parentFunc[2](rowData.send_id, rowData.recive_name, rowData.context, rowData.title);
            if (whichParent == 3) this.parentFunc[3](rowData.id);
        }
    },
    mounted(){
        this.userId = sessionStorage.getItem('userId');
    }
};
</script>

<style scoped>
#dateContainer {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-bottom: 20px;
}
</style>