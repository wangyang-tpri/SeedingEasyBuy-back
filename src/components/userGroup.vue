<template>
    <div>
        <div id="u-header">
            <el-button type="primary" @click="createUserGroup">{{
                createTitle
            }}</el-button>
            <div>
                <el-input
                    placeholder="请输入用户组名称"
                    v-model="searchVal"
                    style="width: 400px"
                >
                    <el-button
                        slot="append"
                        icon="el-icon-search"
                        @click.prevent.native="startSearch"
                    >
                    </el-button>
                </el-input>
            </div>
        </div>
        <i class="el-icon-close close" @click="clearInput"></i>
        <div id="u-center">
            <el-table
                ref="groupTable"
                border
                style="width: 100%"
                :data="groupData"
            >
                <el-table-column
                    prop="group_name"
                    label="用户组名称"
                    width="200"
                ></el-table-column>
                <el-table-column
                    prop="group_desc"
                    label="用户组的描述"
                    width="700"
                ></el-table-column>
                <el-table-column prop="options" :label="optionsTitle">
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            size="big"
                            @click.prevent.native="
                                deleteRow(scope.$index, groupData)
                            "
                        >
                            {{ deleteBtn }}
                        </el-button>
                        <el-button
                            type="text"
                            size="big"
                            @click.prevent.native="
                                lookGroup(scope.$index, groupData)
                            "
                        >
                            {{ lookBtn }}
                        </el-button>

                        <!-- 新建用户 -->
                        <el-button
                            type="text"
                            size="big"
                            @click.prevent.native="createUser(scope.$index, groupData)"
                        >
                            {{ userBtn }}

                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-dialog
                :visible.sync="showDialog"
                title="创建用户组"
                width="40%"
            >
                <el-form :model="form" :label-position="labelPosition">
                    <el-form-item label="用户组名称:" :label-width="labelWidth">
                        <el-input
                            v-model="form.groupName"
                            :placeholder="groupsNameHolder"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="用户组描述:" :label-width="labelWidth">
                        <el-input
                            v-model="form.groupDesc"
                            placeholder="请输入描述信息"
                        >
                        </el-input>
                    </el-form-item>
                    <!-- <el-form-item label="选择用户:" :label-width="labelWidth">
                        <el-select
                            v-model="form.groupUser"
                            placeholder="请选择用户"
                            style="width: 100%"
                            multiple
                        >
                            <el-option
                                v-for="item in userOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item> -->
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="showDialog = false">{{
                        cancelBtn
                    }}</el-button>
                    <el-button @click="confirmDialog" type="primary">{{
                        checkBtn
                    }}</el-button>
                </span>
            </el-dialog>

            <el-dialog
                :visible.sync="showGroupDetails"
                :title="detailsTitle"
                width="40%"
            >
                <span
                    v-show="groupDetails.length > 0"
                    v-for="item in groupDetails"
                    :key="item"
                    style="margin-left: 20px"
                    ><el-tag
                        v-show="item"
                        closable
                        @close="handleClose(item)"
                    >{{ item }}</el-tag></span
                >
                <span slot="footer" class="dialog-footer">
                    <el-button
                        @click="showGroupDetails = false"
                        type="primary"
                        >{{ checkBtn }}</el-button
                    >
                </span>
            </el-dialog>

            <el-dialog
                :visible.sync="userDialog"
                :title="创建用户"
                width="40%"
            >
                <el-form :model="userForm" :label-position="labelPosition">
                    <el-form-item label="用户名:" :label-width="labelWidth">
                        <el-input
                            v-model="userForm.userName"
                            placeholder="请输入用户名"
                        ></el-input>
                    </el-form-item>
                    <el-form-item label="密码:" :label-width="labelWidth">
                        <el-input
                            v-model="userForm.password"
                            placeholder="请输入密码"
                        >
                        </el-input>
                    </el-form-item>
                    <el-form-item label="权限:" :label-width="labelWidth">
                        <el-select
                            v-model="userForm.userControl"
                            placeholder="请选择权限"
                            style="width: 100%"
                            multiple
                        >
                            <el-option
                                v-for="item in userControl"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                            >
                            </el-option>
                        </el-select>
                    </el-form-item>
                </el-form>
                <span slot="footer" class="dialog-footer">
                    <el-button @click="userDialog = false">{{
                        cancelBtn
                    }}</el-button>
                    <el-button @click="confirmUser" type="primary">{{
                        checkBtn
                    }}</el-button>
                </span>


            </el-dialog>
        </div>
    </div>
</template>

<script>
import selfRequest from "../utils/api";
export default {
    name: "userGroup",
    data() {
        return {
            createTitle: this.common.userGroup.createUserTitle,
            searchVal: "",
            deleteBtn: this.common.deleteBtn,
            checkBtn: this.common.checkBtn,
            cancelBtn: this.common.cancleBtn,
            lookBtn: this.common.lookBtn,
            groupsNameHolder: this.common.userGroup.inputGroupsName,
            optionsTitle: this.common.optionsTitle,
            groupData: [],
            showDialog: false,
            isUpdate: false,
            updateIndex: 0,
            form: {
                groupName: "",
                groupUser: "",
                groupDesc: "",
                delivery: true,
            },
            value: "",
            labelWidth: "120px",
            labelPosition: "right",
            showGroupDetails: false,
            groupDetails: [],
            detailsTitle: "",

            userBtn: "创建用户",
            userDialog: false,
            userControl: [
                {value: "admin"},
                {value: "user"},
                {value: "guest"}
            ],
            userForm: {
                userName: "",
                password: "",
                userControl: "",
                delivery: true,
            },            
        };
    },
    // TODO 在正式的使用场景中 都会使用到 userId 也就是需要来对用户进行区分的
    // 现在就是对功能进行正常的测试
    methods: {
        createUserGroup() {
            this.showDialog = true;
        },
        deleteRow(index, data) {
            this.$confirm("确认要删除这条信息吗", "提示", {
                cancelButtonText: "取消",
                confirmButtonText: "确认",
            }).then(() => {
                let gName = data[index].group_name;
                selfRequest.deleteUserGroup(gName).then((result) => {});
                selfRequest.deleteAllGroupUsers(gName).then((result) => {});
                data.splice(index, 1);
            }).catch( () => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                })
            });
        },
        lookGroup(index, data) {  
            this.updateIndex = index;
            selfRequest.getUserInfo(data[index].group_name).then((result) => {
                let userData = [];
                for (let i =0; i < result.data.length; i++){
                    userData.push(result.data[i].user_name)
                }
                this.detailsTitle =
                data[index].group_name + this.common.userGroup.groupDetails;
            this.groupDetails = userData;
            this.showGroupDetails = true;
            })
        },
        confirmDialog() {
            let form = this.form;
            let userId = sessionStorage.getItem('userId');
            selfRequest
                .createUserGroup(form.groupName, form.groupDesc, form.groupUser, userId)
                .then(
                    (result) => {
                        result &&
                            this.$message({
                                type: "success",
                                message:
                                    this.common.userGroup.createUserSuccess +
                                    this.form.groupName,
                            });
                    },
                    (err) => {}
                );
            this.showDialog = false;
            setTimeout(() => {
                this.getGroupData();
                form.groupName = "";
                form.groupDesc = "";
                form.groupUser = "";
            }, 1000);
        },
        startSearch() {
            /**
             * 搜索框中对表格中的数据进行搜索
             */
            for (let i = 0; i < this.groupData.length; i++) {
                if (this.groupData[i].group_name == this.searchVal) {
                    this.groupData = [
                        {
                            group_name: this.searchVal,
                            group_desc: this.groupData[i].group_desc,
                        },
                    ];
                } else {
                    this.$refs.groupTable.data = [];
                }
            }
        },
        clearInput() {
            this.searchVal = "";
            this.getGroupData();
            /**对表格中的数据进行重新的请求 然后展示 */
        },
        getGroupData() {
            let userId = sessionStorage.getItem('userId');
            selfRequest.getUserGroup(userId).then((result) => {
                this.groupData = result.data;
            });
        },

        /** 现在的话 就是必须要获取到最原始的用户组的数据*/
        handleClose(tag){
            let index = this.groupDetails.indexOf(tag);
            this.groupDetails.splice(index, 1)
            // let createUserData = this.disposeUsers(this.groupData,"", tag)
            this.isUpdate = true
            let selfData = this.groupData[this.updateIndex];
            let gUsers = selfData.group_users;
            let gUsersSplit = gUsers.split(',');
            let gName = selfData.group_name;
            gUsersSplit.splice(gUsersSplit.indexOf(tag), 1)
            this.groupData[this.updateIndex].group_users = gUsers = gUsersSplit.join(',')
            // todo 在此处将已经更改过的用户组的数据 重新在数据库中进行更新
            selfRequest.updateUserGroup(gName,gUsers)
            selfRequest.deleteUser(gName,tag)
        },
        /**
         * 创建用户
         * 1 打开一个dialog 在此创建用户
         * 2 创建成功后， 将数据发送 到 register_user这个表中
         * 
         */
        createUser(index, data){
            this.userDialog = true;
            console.log(index)
            this.updateIndex = index;
        },
        confirmUser(index, data){
            let form = this.userForm;
            // let createUserData = this.disposeUsers(this.groupData,form.userName, "");
            this.isUpdate = true
            let selfData = this.groupData[this.updateIndex];
            let gUsers = selfData.group_users;
            let gUsersSplit = gUsers.split(',');
            let gName = selfData.group_name;
            selfRequest.createUser(form.userName, form.password, form.userControl, gUsers,gName).then((res) => {
                if (res.data.code == 2) {
                    this.$message.warning("此用户已存在，请重新注册");
                }else {
                    this.userDialog = false;
                    gUsersSplit.push(form.userName);
                    this.groupData[this.updateIndex].group_users = gUsers = gUsersSplit.join(',');
                }
                this.userForm.userName = "";
                this.userForm.password = "";
                this.userForm.userControl = "";     
            })
        },
        disposeUsers: (groupData, addUser, tag) => {
            this.isUpdate = true;
            let selfData = groupData[this.updateIndex];
            let gUsers = selfData.group_users;
            let gUsersSplit = gUsers.split(',');
            addUser && gUsersSplit.push(addUser);
            tag && gUsersSplit.splice(gUsersSplit.indexOf(tag), 1)
            groupData[this.updateIndex].group_users = gUsers = gUsersSplit.join(',');
            let gName = selfData.group_name;
            return {
                gUsers: gUsers,
                gName: gName
            }
        }

    },
    mounted() {
        this.getGroupData();
    },
};
</script>

<style scoped>
#u-header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-bottom: 20px;
}
.close {
    cursor: pointer;
    position: absolute;
    top: 105px;
    right: 90px;
}
</style>