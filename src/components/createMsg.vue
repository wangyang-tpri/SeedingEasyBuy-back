<template>
    <div style="margin-top: 20px">
        <div style="text-align: left">{{ temTitle }}
            <span
                v-for="item in allPresentTem"
                :key="item.tem_name"
                style="margin-left: 20px"
            >
                <el-tag 
                    effect="dark"   
                    @click="getTem(item)" 
                    style="cursor: pointer"
                >
                    {{ item.tem_name }}
                </el-tag>
            </span>
            <el-dialog
                :visible.sync="showTem"
                width="40%"
                height="50%"
                :title="saveTemTitle"
            >
                <el-input
                    placeholder="请输入模板名称"
                    v-model="temName"
                    clearable
                >

                </el-input>
                <span slot="footer" class="dialog-footer">
                <el-button
                    @click="postTemToDatabase"
                    type="primary"
                >
                    {{ checkBtn }}</el-button
                >
            </span>
            </el-dialog>
        </div>
        <hr/>
        <el-button 
            type="primary" 
            size="small"
            id="syncEle"
            @click="updateDeparmentAndUserList"
            v-if="isShowSyncDataEle"
        > 同步数据</el-button>
        <div style="text-align: left">
            {{ addressUser }}

            <el-button type="success" size="small" @click="getData"
                >选择用户</el-button
            >
            <span
                v-for="item in selectVal"
                :key="item.list_name"
                style="margin-left: 20px"
            >
                <el-tag closable @close="handleClose(item)">
                    {{ item.list_name }}
                </el-tag>
            </span>
            <hr :class="[{ lineColor: showLine1 }]" />
            {{ boxTitle }}
            <input
                type="text"
                @focus="showLine2 = true"
                @blur="showLine2 = false"
                v-model="inputTitle"
            />
            <hr :class="{ lineColor: showLine2 }" />
            
            {{digTitle}}
            <input type="text"
                v-model = "digest"
            >
            <hr/>
            {{linkTitle}}
            <input type="text"
                v-model="externalLinks"
            >
            <hr/>
            <span>
                <el-upload
                    class="upload-demo"
                    multiple
                    action="/upload/image"
                    ref="imageUpload"
                    :before-upload="beforeAvatarUpload"
                    :on-success="imageUploadSuccess"
                    :headers="uploadFileHeaders"
                >
                    <el-button size="small" type="success">
                        上传封面照片</el-button
                    >
                </el-upload>
            </span>
            <hr />
        </div>
        <div>
            <el-input
                type="textarea"
                placeholder="请输入内容"
                v-model="textarea2"
                :rows="10"
            >
            </el-input>
        </div>
        <div id="footer">
            <el-button type="primary" size="small" @click="saveTem">将以上数据保存成模板</el-button>
            <el-button type="primary" size="small" 
            @click="sendChartMsg">发送</el-button>
        </div>

        <el-dialog
            :visible.sync="showDepartment"
            width="50%"
            height="50%"
            :title="showTitle"
        >
            <el-tabs v-model="activeName">

                <el-tab-pane label="部门" name="first">

                    
            <template>
                <!-- <el-transfer
                    v-model="value"
                    :data="departmentData"
                    disabled=""
                    @change="checkDepart"
                ></el-transfer> -->
                <div>
                    <el-tree
                        :data="treeData"
                        show-checkbox = true
                        :props="defaultProps"
                        check-strictly="true"
                        ref="tree"
                    ></el-tree>
                </div>
            </template>
                </el-tab-pane>
                <el-tab-pane label="标签" name="second" v-if="!isGuestRole">
                    <el-tree
                        :data="tagTreeData"
                        show-checkbox = true
                        :props="defaultTagProps"
                        check-strictly="true"
                        ref="tagTree"
                    ></el-tree>
                    
                </el-tab-pane>
            </el-tabs>
            <span slot="footer" class="dialog-footer">
                <el-button
                    @click="createDepartTag"
                    type="primary"
                    :disabled="isCheckBtn"
                >
                    {{ checkBtn }}</el-button
                >
            </span>
        </el-dialog>
    </div>
</template>
<script>
import selfRequest from "../utils/api";
export default {
    name: "createMsg",
    data() {
        return {
            addressUser: "收件人 :",
            boxTitle: "主题：",
            inputUser: "",
            inputTitle: "",
            textarea2: "",
            uploadFileHeaders: {},
            showLine1: false,
            showLine2: false,
            showDepartment: false,
            showTitle: "选择需要发送的用户",
            departmentData: [],
            selectDepartData: [],
            value: [],
            checkBtn: "确认",
            isCheckBtn: false,
            cbData: [],
            selectVal: [],
            treeData: [],
            defaultProps: {
                children: "children",
                label: "list_name",
            },
            employeeData: [],
            flag: false,
            addressToken: "",
            activeName: 'first',
            defaultTagProps: {
                children: "children",
                label: "list_name"
            },
            tagListData: [],
            tagMemData: [],
            tagTreeData: [],

            digest: "",
            digTitle: '摘要:',
            isImageUpload: false,
            linkTitle: '外部链接:',
            externalLinks: '',
            temTitle: '用户已有模板:',
            userId: '',
            showTem: false,
            saveTemTitle: '请输入需要保存的模板的名称',
            temName: '',
            allPresentTem: '',
            selfTemData: '',
            employee: [],
            department: [],
            tag: [],
            isChooseTem: false,
            isShowSyncDataEle: false,
            isIncludeKeyWord: false,
            keyWords: [],
            allContent: '',
            isGuestRole: false
        };
    },
    methods: {
        insertBoxToTable() {
            this.value = [];
            let xhr = new XMLHttpRequest();
            let formData = this.getUploadInfo();
            xhr.open("post", "/insertBox", true);
            xhr.onreadystatechange = (res) => {
                if (xhr.status == 200 && xhr.readyState == 4) {
                    this.$message.success("信息发送成功");
                    setTimeout(() => {
                        location.reload();
                    }, 2000);
                }
            };
            xhr.setRequestHeader(
                "Authorization",
                sessionStorage.getItem("token")
            );

            xhr.send(formData);
        },
        getUploadInfo() {
            let uploadEle =
                document.getElementsByClassName("el-upload__input")[0];
            let formData = new FormData();
            let uploadFile = uploadEle.files[0];
            formData.append("file", uploadFile);
            /**
             * 就是我这只让上传图片
             * 通过 服务端的接口调用企业微信的接口 将图片上传到企业微信中
             * 然后返回一个 mediaid  存储到我的数据库中   
             */
            // formData.append("title", this.inputTitle);
            // formData.append("context", this.textarea2);
            // formData.append("reviceN", this.selectVal.join(''));
            return formData;
        },
        callWeChartInter() {
            selfRequest.callWeChartAccessToken().then((result) => {});
        },
        getDepartmentList() {
            selfRequest.getDeparmentList().then((result) => {
                this.cbData = result.data;
            });
        },
        getData() {
            let data = this.judgeUserRole(this.cbData.data);
            let empData = this.employeeData;
            let count = data.length;
            if (!this.flag) {
                for (let i = 0; i < empData.length; i++) {
                    if (empData[i].name != "undefined") {
                        for (let j = 0; j < data.length; j++) {
                            if (empData[i].list_id == data[j].list_id) {
                                count++;
                                data.push({
                                    list_name: empData[i].name,
                                    list_parentid: empData[i].list_id,
                                    list_id: empData[i].emp_id,
                                    isEmployee: 1
                                });
                            }
                        }
                    }
                    this.flag = true;
                }
            }
            this.createTreeData(data);
            this.showDepartment = true;
        },
        judgeUserRole(data){
            this.isGuestRole && data.forEach(ele => {
                if (ele.list_name.indexOf('guest') != -1) {
                    data = [].concat(ele);
                }
            })
            return data;
        },
        checkDepart() {
            this.isCheckBtn = false;
        },
        createDepartTag() {
            this.selectVal = [];
            this.showDepartment = false;
            let allData = this.$refs.tree.getCheckedNodes();
            let a = this.$refs.tagTree.getCheckedNodes();
            if (allData.length >= 1 || a.length >= 1){
                this.isChooseTem = false;
            }
            if(a.length >= 1) {
                for (let i = 0; i < a.length; i++){
                    allData.push(a[i]);
                }
            }
            this.selectVal = allData;
        },
        handleClose(tag) {
            let index = this.selectVal.indexOf(tag);
            this.selectVal.splice(index, 1);
        },
        createTreeData(originalData) {
            this.createTagData()
            let len = originalData.length;
            let tData = [];
            /**如果用户的角色是guest的话 就只展示guest部门 其他的都不展示 */
            this.treeData = this.treeUtils(originalData);
        },
        sonsTree(obj, arr) {
            let count = 0;
            var children = new Array();
            for (var i = 0; i < arr.length; i++) {
                count ++;
                arr[i].id = count;
                if (obj.list_id == arr[i].list_parentid) {
                    this.sonsTree(arr[i], arr);
                    children.push(arr[i]);
                }
            }
            if (children.length > 0) {
                obj.children = children;
            }
            return obj;
        },
        treeUtils(data) {
            let ptree = [];
            for (var i = 0; i < data.length; i++) {
               if (!this.isGuestRole){
                   if (data[i].list_parentid == 0) {
                        let o = this.sonsTree(data[i], data);
                        ptree.push(o);
                    }
                } else {
                    if (data[i].list_name.indexOf('guest') != -1){
                        let o = this.sonsTree(data[i], data);
                        ptree.push(o);
                    }
                }
            }
            return ptree;
        },
        getEmployee(listId) {
            selfRequest.getEmployee(listId).then((result) => {
                this.employeeData = result.data;
            });
        },
        updateDeparmentAndUserList() {
            this.flag = false;
            this.treeData = [];
            selfRequest.updateDepartmentData().then((res) => {
                this.$message.success("同步数据成功");
                setTimeout(() => {
                    location.reload();
                }, 2000)
            });
        },
        
        /**
         * 获取企业微信的标签数据
         */
        getTagList(){
            selfRequest.tagList().then((res) => {
                this.tagListData = res.data;
            })
        },
        getTagMemData(tagId){
            selfRequest.tagMem(tagId).then((res) => {
                this.tagMemData = res.data
            })
        },
        createTagData(){
            this.tagTreeData = [];
            for (let i = 0; i < this.tagListData.length; i++){
                this.tagTreeData.push({
                    list_name: this.tagListData[i].tag_name,
                    tag_id: this.tagListData[i].tag_id,
                    children: []
                })
            }
            let tagMem = this.tagMemData;
            for (let i = 0; i < this.tagTreeData.length; i++){
                for (let j = 0; j < tagMem.length; j ++){
                    if (this.tagTreeData[i].tag_id == tagMem[j].tag_id){
                        this.tagTreeData[i].children.push({
                            list_name: tagMem[j].user_name,
                            partyId: tagMem[j].party_id,
                            isEmployee: tagMem[j].isEmployee
                        })
                    }
                }
            }
        },
        beforeAvatarUpload(file){
            const islt2M = file.size / 1024 /1024 < 2;
            const isIamgeFormat = (file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg');
            if (!islt2M) {
                this.$message.error('上传的图片大小不能超过2MB!')
            }
            if (!isIamgeFormat) {
                this.$message.error('上传的图片的格式只能是 png | jpg | jpeg')
            }
            return islt2M && isIamgeFormat;
        },
        imageUploadSuccess(res, file, fileList){
            if (res.code === 1) {
                this.isImageUpload = true;
            }
        },
        init(){
            this.cbData = [];
            this.employeeData = [];
            this.tagListData = [];
            this.tagMemData = [];
            this.getDepartmentList();
            this.getEmployee();
            this.getTagList();
            this.getTagMemData();
            this.isGuestRole = sessionStorage.getItem('userControl') == 'guest';
        } ,
        sendChartMsg(){
            if (!this.isImageUpload) {
                this.$message.warning("请上传图片")
                return;
            }
            this.disposeToData();
            let content = this.textarea2;
            let title = this.inputTitle;
            let digest = this.digest;
            let userId = this.userId;
            let mediaId = '';
            let reviceN = [];
            for (let i =0; i < this.selectVal.length; i++){
                reviceN.push(this.selectVal[i].list_name)
            }
            this.allContent = content + title + digest + '';
            // 判断发送的消息中是否包含关键词
            if (this.judgeHasKeyWord(this.allContent)){
                this.saveFailBox(reviceN.join('|'), content, title, this.employee, this.departmentData, this.tag, digest);
                location.reload();
                return;
            }
            selfRequest.chartMsg(this.addressToken, content, title, this.employee, this.department, this.tag, digest, mediaId, this.externalLinks).then((result) => {
                if (result.data.errcode == 1){
                    this.$message.success("信息发送成功");
                    selfRequest
                .insertBox(reviceN.join(' | '), this.inputTitle, this.textarea2, this.employee, this.department, this.tag, mediaId, digest, this.externalLinks, userId)
                .then(
                    (result) => {
                        setTimeout(() => {
                            location.reload();
                        }, 2000);
                    },
                    (err) => {}
                );
                } else {
                    // 消息发送失败时给出提示
                    let msg = result.data.msg.split(',')[0];
                    this.$message({
                        duration: 5000,
                        type: 'error',
                        message: msg
                    })
                }
            })
            
        },
        getToken(){
            selfRequest.getAddressToken().then((result) => {
                this.addressToken = result.data.token;
            }, (err) => {

            })
        },
        getGlobalToken(){
            /**
             * 获取全局token
             */
        },
        saveTem(){
            this.showTem = true;
        },
        getTem(item){
            this.isChooseTem = true;
            this.selectVal = [];
            selfRequest.getSelfTem(this.userId, item.tem_name).then((result) => {
                let data = this.selfTemData = result.data;
                let listItem = data[0].tem_user.split('|');
                listItem.forEach(ele => {
                    this.selectVal.push({
                        list_name: ele
                    })
                });
                for (let i = 0; i < data.length; i++) {
                    this.inputTitle = data[i].tem_title;
                    this.digest = data[i].tem_digest;
                    this.externalLinks = data[i].tem_url;
                    this.textarea2 = data[i].tem_content;
                    this.employee.push(data[i].touser);
                    this.department.push(data[i].toparty);
                    this.tag.push(data[i].totag);
                }
            })
            /**
             * 需要将信息的id存放到  数组中 
             */
        },
        postTemToDatabase(){
            let reviceN = [];
            for (let i =0; i < this.selectVal.length; i++){
                reviceN.push(this.selectVal[i].list_name)
            }
            this.disposeToData();
            this.showTem = false;
            selfRequest.addTemplate(this.temName,reviceN.join('|'),this.inputTitle, this.digest, this.externalLinks, this.textarea2, this.userId, this.employee,this.department, this.tag).then((result) => {
                this.$message.success("模板保存成功，请刷新后使用")
            })
        },
        /**
         * 获取当前用户下的所有拥有的模板
         */
        getAllTem() {
            selfRequest.getAllTem(this.userId).then((result) => {
                this.allPresentTem = result.data
            })
        },
        disposeToData(){
            if (!this.isChooseTem) {
                this.employee = [];
                this.tag = [];
                this.department = [];
                for (var i = 0; i < this.selectVal.length; i++){
                    let currentVal = this.selectVal[i];
                    if (currentVal.isEmployee){
                        this.employee.push(currentVal.list_name)
                    } else if (currentVal.tag_id){
                        this.tag.push(currentVal.tag_id);
                    } else {
                        this.department.push(currentVal.list_id)
                    }
                }
            }

        },
        judgeHasKeyWord(str){
            for(let i = 0; i < this.keyWords.length; i++){
                let strReg = new RegExp(this.keyWords[i], 'ig');
                this.isIncludeKeyWord = strReg.test(str);
                if (this.isIncludeKeyWord) {
                    break;
                }
            }
            return this.isIncludeKeyWord;
        },
        async getKeyWord(){
            let res = await selfRequest.getKeyWords();
            res.data.forEach(ele => {
                this.keyWords.push(ele.words)
            })
        },
        async saveFailBox(reciveName, content, title, employee, departmentData, tag, digest){
           let res = await selfRequest.saveFailBox(reciveName, content, title, employee, departmentData, tag, digest);
        }

    },
    mounted() {
        this.init();
        this.uploadFileHeaders = {
            Authorization: sessionStorage.getItem("token"),
        };
        this.getToken();
        this.userId = sessionStorage.getItem('userId');
        this.isShowSyncDataEle = sessionStorage.getItem('loginName') == "tpri";
        this.getAllTem();
        this.getKeyWord();
    },
};
</script>

<style scoped>
input {
    border: none;
    outline: none;
    width: 80%;
}
#footer {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}
.lineColor {
    height: 0.1px;
    background: #409eff;
}
#syncEle {
    position: absolute;
    right: 130px;
    top: 21px;
}
</style>