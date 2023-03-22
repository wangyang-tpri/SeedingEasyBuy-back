/**
    发件箱和回收站 使用的表格组件
 */
<template>
    <div>
        <el-table
            ref="multipleTable"
            :data="tableData.slice((currentPage-1)*pageSize, pageSize*currentPage)"
            tooltip-effect="dark"
            style="width: 100%"
            border
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" width="55"> </el-table-column>
            <el-table-column prop="recive_name" label="姓名" width="320">
                <div v-for="item in recive_name"
                :key="item">

                <el-tag >
                    {{ item }}
                </el-tag>
                </div>
            </el-table-column>
            <el-table-column
                prop="title"
                label="主题"
                show-overflow-tooltip
                width="300"
            >
            </el-table-column>
            <el-table-column prop="box_date" label="日期" sortable>
                <template slot-scope="scope">
                    {{ scope.row.box_date | dateTimeFormat }}
                </template>
            </el-table-column>
            <el-table-column prop="mode" label="模式" width="100">
                <template slot-scope="scope">
                    {{ scope.row.mode | transMode}}
                </template>

            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            delectRow(scope.$index, scope.row, tableData)
                        "
                        type="text"
                        size="small"
                    >
                        删除
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        @click.native.prevent="
                            showBoxDetail(scope.$index, scope.row, tableData)
                        "
                    >
                        详情
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog
            :visible.sync="showDialog"
            title="信息详情"
            width="40%"
            class="fontB"
        >
            <div style="text-align: left">
                <ul>
                    <li>
                        <span class="fontB">发件人：</span>
                        <span>{{ loginName }}</span>
                        <hr />
                    </li>
                    <li>
                        <span style="font-weight: bold">收件人：</span>
                        <span>{{ name }}</span>
                        <hr />
                    </li>
                    <li>
                        <span style="font-weight: bold">主题：</span>
                        <span>{{ title }}</span>
                        <hr />
                    </li>
                    <li>
                        <span class="fontB">摘要：</span>
                        <span>{{digest}}</span>
                        <hr />
                    </li>
                    <li>
                        <span class="fontB">日期：</span>
                        <span>{{ date | dateTimeFormat}}</span>
                        <hr />
                    </li>
                    <li>
                        <span class="fontB">信息内容：</span>
                        <span>{{ content }}</span>
                    </li>
                </ul>
            </div>

            <span slot="footer" class="dialog-footer">
                <el-button @click="showDialog = false" type="primary">
                    确认
                </el-button>
            </span>
        </el-dialog>
        <el-pagination
            style="margin-top: 20px"
            background
            small
            layout="prev, pager,next"
            :total="total"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
        >

        </el-pagination>
    </div>
</template>

<script>
/**
 * ref 访问子组件实例或子元素
 *  ref在子组件中添加一个标识id
 *      在js中使用this.$refs.id来访问当前的元素
 *
 *
 */
import selfRequest from "../utils/api";
import dialogBox from "./dialogBox.vue";
export default {
    name: "box",
    components: {
        "dialog-box": dialogBox,
    },
    props: ["tableData", "distinguishBox", "total"],
    data() {
        return {
            multipleSelection: [],
            selectRow: [],
            distinguish: this.distinguishBox,
            showDialog: false,
            name: "",
            title: "",
            content: "",
            currentPage: 1,
            pageSize: 10,
            parentFunc:['', selfRequest.deleteRowSendBox, selfRequest.deleteRowDeleteBox],
            target_url: '',
            digest: '',
            date: '',
            loginName: '',
        };
    },
    methods: {
        toggleSelection(rows) {
            if (rows) {
                rows.forEach((row) => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
            /** 将box子组件中选择的数据传递给父组件 inbox， inbox将值传递给子组件 dateSearch */
            this.$emit("selectDateChild", this.multipleSelection);
        },
        /**删除单行的 事件 */
        delectRow(index, row,rows) {
            let distin = this.distinguish;
            this.$confirm("确认要删除这条信息吗？", "提示", {
                confirmButtonText: "确认",
                cancelButtonText: "取消",
            })
                .then(() => {
                    let rowData = row;
                    this.deleteRowBasedParent(rowData, distin);
                    rows.forEach(function(ele, index){
                        if (ele.id){
                            (ele.id == row.id) && rows.splice(index, 1)
                        } else {
                            (ele.send_id == row.send_id) && rows.splice(index, 1)
                        }
                    })
                    this.$message({
                        type: 'success',
                        message: '信息删除成功'
                    })
                })
        },
        showBoxDetail(index, row, rows) {
            let currentRow = row;
            this.name = currentRow.recive_name;
            this.title = currentRow.title;
            this.content = currentRow.context;
            this.digest = currentRow.digest;
            this.date = currentRow.box_date
            this.showDialog = true;
        },
        handleCurrentChange(val){
            this.currentPage = val
        },
        deleteRowBasedParent(val, whichParent){
            if (whichParent == 'send') this.parentFunc[1](val.send_id, val.recive_name, val.context, val.title);
            if (whichParent == 'delete') this.parentFunc[2](val.id);
        }
    },
    computed: {
        disposeData: function () {
            for (let i = 0; i < this.tableData.length; i++) {
                this.tableData[0].title = "使用计算属性来改变父组件中的值";
            }
            return this.tableData;
        },
    },
    updated() {
        // this.toggleRowSelection(rows)
    },
    mounted(){
        this.loginName = sessionStorage.getItem('loginName');
    }
};
</script>

<style scoped>
.fontB {
    font-weight: bold;
}
ul li {
    list-style: none;
}
</style>