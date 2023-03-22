/**
 * @description 定义公共方法 和 公共变量 模块
 */
let variableObject = {
    notFound: '接口没有找到',
    checkDateMsg: '开始时间不能大于结束时间',
    timeEfficiency: (sDate, eDate) => {
        return sDate > eDate ? true : false;
    }
}
export default {
    dateHolder: '选择日期',
    notFound: '接口没有找到',
    serverErr: '内部服务器错误',
    sTitle: '开始时间',
    eTitle: '结束时间',
    deleteBtn: '删除',
    searchBtn: '查询',
    lookBtn: '查看',
    deleteMsg: '你确认删除这条数据吗',
    userGroup: {
        createUserSuccess: '成功创建了用户组:',
        groupDetails: ' 用户组所包含的用户',
        inputGroupsName: '请输入用户组名称',
        createUserTitle: '新建用户组',
    },
    checkBtn: '确认',
    cancleBtn: '取消',
    optionsTitle: '操作',
    interface: {
        notFoundErrorMsg: {
            message: variableObject.notFound,
            showClose: true
        }
    },
    timeEfficiency: variableObject.timeEfficiency,
    comCheckDate: (sDate, eDate, self) => {
        let that = this;
        if (this.timeEfficiency(sDate, eDate)) {
            that.$message(that.alertDateError)
            that.btnDisabled = true;
        } else {
            that.btnDisabled = false;
        }
    },
    confirmDelete: (self, data, httpRequest) => {
        self.$confirm('确认要删除这条信息吗？', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消'
        }).then(() => {
            for (let i = 0; i < data.length; i++) {
                let selfData = data[i];
                selfData.fail_id ? httpRequest(selfData.fail_id) : httpRequest(selfData.send_id, selfData.recive_name, selfData.context, selfData.title);
            }
            setTimeout(() => {
                location.reload()
            })
            self.refreshNum++;
            self.$message({
                type: 'success',
                message: '信息删除成功'
            }).catch(() => {
                self.$message({
                    type: 'info',
                    message: '已取消删除'
                })
            })
        })

    }
}