/**
    信息发送成功、信息发送失败的表格渲染组件
 */
<template>
    <div>
        <el-table
            ref="multipleTable"
            border
            style="width: 100%"
            :data="tableData.slice((currentPage -1 ) * pageSize, pageSize * currentPage)"
            @selection-change="handleSelectionChange"
        >
            <el-table-column width="55" type="selection"></el-table-column>
            <el-table-column
                width="320"
                prop="recive_name"
                label="姓名"
            ></el-table-column>
            <el-table-column
                width="300"
                prop="title"
                label="主题"
            ></el-table-column>
            <el-table-column width="200" prop="box_date" label="日期" sortable>
                <template slot-scope="scope">
                    {{ scope.row.box_date | dateTimeFormat }}
                </template>
            </el-table-column>
            <el-table-column width="100" prop="mode" label="模式">
                <template slot-scope="scope">
                    {{ scope.row.mode | transMode }}
                </template>

            </el-table-column>
            <el-table-column label="操作">
                <template slot-scope="scope">
                    <el-button
                        @click.native.prevent="
                            deleteRow(scope.$index, scope.row, tableData)
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
                            showBoxDetail(scope.$index, scope.row)
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
                        <span>{{loginName}}</span>
                        <hr />
                    </li>

                    <li>
                        <span class="fontB">收件人：</span>
                        <span>{{ name }}</span>
                        <hr />
                    </li>
                    <li>
                        <span class="fontB">主题：</span>
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
                        <span>{{date | dateTimeFormat}}</span>
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
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-size="pageSize"
            small
            layout="prev, pager,next"
            :total="total"
        >
        </el-pagination>
    </div>
</template>

<script>
import selfRequest from "../utils/api";
export default {
    name: "sBox",
    props: ["tableData", "boxStatus", "total"],
    data() {
        return {
            multipleSelection: [],
            showDialog: false,
            name: "",
            title: "",
            content: "",
            currentPage: 1,
            pageSize: 10,
            loginName: '',
            digest: '',
            date: '',
        };
    },
    methods: {
        handleSelectionChange(val) {
            this.$emit("selectChildData", val);
        },
        checkTableData() {},
        showBoxDetail(index, rows) {
            this.showDialog = true;
            let currentRow = rows;
            this.name = currentRow.recive_name;
            this.title = currentRow.title;
            this.content = currentRow.context;
            this.digest = currentRow.digest;
            this.date = currentRow.box_date;
        },
        deleteRow(index, row, rows) {
            let specificFunc = (index, row) => {
                if (row.status == 1) {
                    /**
                     * 表示删除成功的消息
                     *
                     */
                    let rowData = row;
                    selfRequest.deleteRowSuccessBox(rowData.send_id, rowData.recive_name, rowData.context, rowData.title);
                    rows.forEach((ele, index) => {
                        (ele.send_id == row.send_id) && rows.splice(index, 1)
                    });
                } else {
                    /**
                     * 表示删除失败的消息
                     */
                    selfRequest.deleteRowFailBox(row.fail_id);
                    rows.forEach((ele, index) => {
                        (ele.fail_id == row.fail_id) && rows.splice(index, 1)
                    });
                }
                this.$message({
                    type: 'success',
                    message: '信息删除成功'
                })
            };
            this.$confirm("确定要删除这条数据吗？", "提示", {
                cancelButtonText: "取消",
                confirmButtonText: "确认",
            })
                .then(() => {
                    specificFunc(index, row);
                })
                .catch(() => {
                    this.$message({
                        type: "info",
                        message: "已取消删除",
                    });
                });
        },
        handleCurrentChange(val){
            this.currentPage = val;
        }
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
</style>