/*
 * @Author: wangyang-tpri
 * @Date: 2021-08-17 11:15:02
 * @LastEditTime: 2021-12-16 10:48:49
 * @FilePath: \IAPlatform\service\utils\weChart.js
 * @email: angularyang@163.com (c) 2020-2021 yiTong
 * @Description: 处理微信公共接口
 * 
 */

const https = require('https')
const axios = require('axios');
const querystring = require('querystring')
const sqlQueryResult = require('./connecSql')
const chartConfig = require('./chartConfig');
const request = require('request');
const { Querystring } = require('request/lib/querystring');
let joinData = (data) => {
    if (data.length > 1) {
        data = data.join('|')
    } else {
        data = data[0]
    }
    return data;
}
let insertEmployeeData = (chunk, listId) => {
    let res = JSON.parse(chunk)
    let userlist = res.userlist;
    let len = userlist.length;
    if (len > 0) {
        for (let i = 0; i < len; i++) {
            let sql = `INSERT INTO employee (name, list_id) VALUES ('${userlist[i].name}', '${listId}') `;
            sqlQueryResult(sql).then((result) => { })
        }
    }
}
let getEmployee = (token, listId) => {
    let data = {
        access_token: token,
        department_id: listId,
        fetch_child: 0
        /**0 代表不适用递归 1 表示使用递归 */
    }
    let option = chartConfig.employeeOptions(data)
    let res = https.request(option, (res) => {
        res.on('data', (chunk) => {
            setTimeout(() => {
                insertEmployeeData(chunk, listId)
            }, 200);
        })
        res.on('end', () => { })
    })
    res.end()

}
let getTagMembers = (tagId, token) => {
    let sql = "DELETE FROM tag_member";
    let data = {
        access_token: token,
        tagid: tagId
    }
    let opt = chartConfig.tagMemOption(data);
    let memReq = https.request(opt, (res) => {
        res.on('data', (chunk) => {
            chunk = JSON.parse(chunk);
            let userList = chunk.userlist;
            let partylist = chunk.partylist;
            for (let i = 0; i < userList.length; i++) {
                let userSql = `INSERT INTO tag_member (user_id, user_name, tag_id, isEmployee) VALUES ('${userList[i].userid}', '${userList[i].name}', ${tagId}, 1 )`;
                sqlQueryResult(userSql).then((result) => { });
            }
            for (let j = 0; j < partylist.length; j++) {
                /**
                 * 在这里对 部门列表进行一个更新
                 */
                let queryDepartSql = `SELECT list_name FROM chart_department_list WHERE list_id = ${partylist[j]}`;
                sqlQueryResult(queryDepartSql).then((result) => {
                    let user_name = result[0].list_name;
                    let partySql = `INSERT INTO tag_member (party_id, user_name,tag_id, isEmployee) VALUES (${partylist[j]}, '${user_name}',${tagId}, 0)`;
                    sqlQueryResult(partySql).then((result) => { })
                })
            }
            /**
             * 获取到每个标签的具体数据后 
             * 存入到 tag_member 表中
             */

        })
        res.on('end', () => { })
    })
    memReq.end();
}
let getTagList = (token) => {
    let sql = "DELETE FROM tag_list";
    sqlQueryResult(sql).then((result) => {
        let data = {
            access_token: token
        }
        let tagOption = chartConfig.tagOption(data);
        let tagRes = https.request(tagOption, (res) => {
            res.on('data', (chunk) => {
                chunk = JSON.parse(chunk);
                let tagList = chunk.taglist;
                for (let i = 0; i < tagList.length; i++) {
                    getTagMembers(tagList[i].tagid, token)
                    /**
                     * 在这里根据tagId来获取
                     * 标签下面的具体部门和员工
                     */
                    let insertTagList = `INSERT INTO tag_list VALUES (${tagList[i].tagid}, '${tagList[i].tagname}')`;
                    sqlQueryResult(insertTagList).then((result) => {

                    })
                }
                /**
                 * 将列表数据保存到对应的数据库中 tag_list 表中
                 */
            })
            res.on('end', () => {
                console.log('标签数据获取成功')
            })
        })
        tagRes.end()
    })
}

/**
 * 单独定义一个方法  用来获取全局access_token
 */

let getAccessToken = () => {
    let options = chartConfig.tokenOptions;
    let token;
    let req2 = https.request(options, (res) => {
        res.on('data', (chunk) => {
            token = JSON.parse(chunk).access_token;
            let sql = `INSERT INTO chart_token (token, name) VALUES ('${token}', 'global') ON DUPLICATE KEY UPDATE id = 1`;
            sqlQueryResult(sql).then((result) => {

            })
        })
        res.on('end', () => {
        })
    })
    req2.end()
}
/**
 * 用来获取通讯录 应用的token
 */

module.exports = {
    /**
     * 
     * @param {Object} req 
     * @param {Object} res 
     * @description 在服务启动时  需要调用一次  
     *  在客户端进行手动数据同步时  还需要再调用   
     *  access_token 是需要保存到数据库中的 
     */
    getWeChartAccessToken: (req, res) => {
        let sql = "SELECT * FROM chart_token WHERE name = 'global' ORDER BY id DESC LIMIT 0, 1";
        sqlQueryResult(sql).then((result) => {
            let token = result[0].token;
            let list;
            let data2 = {
                access_token: token,
                id: ''
            }
            let options2 = chartConfig.departOptions(data2);
            getTagList(token)
            let reqList = https.request(options2, (res) => {
                res.on('data', (chunk) => {
                    list = JSON.parse(chunk)
                    if (list.department.length > 0) {
                        let sql = "DELETE FROM employee";
                        let sql2 = "DELETE FROM chart_department_list";
                        sqlQueryResult(sql2).then((result) => {
                            sqlQueryResult(sql).then((result) => { })
                            for (let i = 0; i < list.department.length; i++) {
                                let key = list.department[i]
                                /**在此将数据去查询部门列表中的员工数据 */
                                getEmployee(token, key.id)
                                let sql = `INSERT INTO chart_department_list (list_id, list_name, list_parentid, list_order) VALUES ('${key.id}', '${key.name}', '${key.parentid}', '${key.order}') ON DUPLICATE KEY UPDATE list_id = ${key.id}`;
                                sqlQueryResult(sql).then((result) => {
                                })
                            }
                        })
                    }

                })
                res.on('end', () => {
                    console.log('部门列表数据获取成功')
                })
            })
            reqList.end()
        })
        req && res.status(200).send({
            msg: '数据更新成功'
        })
    },
    /**
     * 
     * @param {Object} req 
     * @param {Object} res 
     * @description 全量获取部门列表的数据
     */
    getDepartmentList: (req, res) => {
        let sql = "SELECT * FROM chart_department_list"
        sqlQueryResult(sql).then((result) => {
            try {
                res.status(200).send({
                    data: result
                })
            } catch (error) {
                reject(error)
            }
        })

    },
    /**
     * @description 从数据库中获取部门中的用户详情
     */
    getEmployeeFromDatabase: (req, res) => {
        let sql = "SELECT * FROM employee";
        sqlQueryResult(sql).then((result) => {
            res.send(result)
        }, (err) => {
            console.log(err)
        })
    },
    getAccessToken: () => {
        try {
            getAccessToken();
        } catch (error) {
            getAccessToken()
        } finally {
        }
    },
    setChartMsg: (req, res) => {
        let clientRes = res;
        let rBody = req.body;
        let touser;
        let employee = rBody.employee;
        let department = rBody.department;
        let content = rBody.content;
        let tag = rBody.tag;
        let digest = rBody.digest;
        let title = rBody.title;
        let url = rBody.url;
        tag = joinData(tag);
        touser = joinData(employee);
        department = (department && joinData(department));
        let data = querystring.stringify({
            access_token: rBody.access_token.token,

        })
        let sql = "SELECT thumb_media_id FROM thumb_image  ORDER BY id DESC LIMIT 0, 1";
        sqlQueryResult(sql).then((result) => {
            let mediaId = result[0].thumb_media_id;
            const optionss = JSON.stringify({
                "touser": touser,
                "toparty": department,
                "totag": tag,
                "msgtype": "text",
                "agentid": 3,
                "text": {
                    "content": content
                },
                "safe": 0,
                "enable_id_trans": 0,
                "enable_duplicate_check": 0
            }
            )

            /**
             * 使用axios来发送post请求
             */
            axios({
                url: "https://qyapi.weixin.qq.com/cgi-bin/message/send?" + data,
                method: "post",
                data: {
                    "touser": touser,
                    "toparty": department,
                    "totag": tag,
                    "msgtype": "mpnews",
                    "agentid": 3,
                    "mpnews": {
                        "articles": [
                            {
                                "title": title,
                                "thumb_media_id": mediaId,
                                "author": "Author",
                                "content_source_url": url ,
                                "content": content,
                                "digest": digest
                            }
                        ]
                    },
                    "safe": 0,
                    "enable_id_trans": 0,
                    "enable_duplicate_check": 0,
                    "duplicate_check_interval": 1800
                },
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then((res) => {
                clientRes.send({
                    errcode: 1,
                    msg: 'ok'
                })
            }, (res) => {
                clientRes.send({
                    errcode: 0,
                    msg: res.data.errmsg
                })
            })
        })
    },
    getAddressToken: () => {
        try {
            let options = chartConfig.addressOption;
            let token;
            let req2 = https.request(options, (res) => {
                res.on('data', (chunk) => {
                    token = JSON.parse(chunk).access_token;
                    let sql = `INSERT INTO chart_token (token, name) VALUES ('${token}', 'addressBook') `;
                    sqlQueryResult(sql).then((result) => {

                    })
                })
                res.on('end', () => {
                })
            })
            req2.end()
        } catch (error) {
            this.getAddressToken()
        } finally {
        }
    },
    getTagList: (req, res) => {
        let sql = "SELECT * FROM tag_list";
        sqlQueryResult(sql).then((result) => {
            res.send(result)
        })
    },
    getTagMembers: (req, res) => {
        let sql = "SELECT * FROM tag_member";
        sqlQueryResult(sql).then((result) => {
            res.send(result)
        })
    },
    /**
     * 上传图片
     * 将图片上传到企业微信服务器
     * 然后将返回的mediaiD存储到 数据库中
     * 
     */
    postImage: (form, uploadFile, stat) => {
        let data = {
            access_token: 'kJCf6rYYITpOBjEtHl3At2bey4-gRNwkTXjqdl9z05A5B4x78bPo8zHorpd9-r-LThk56Hd7Dl_ZsQSl2g1YqR9OWnSV6Co9oXTsaqjTEa7M3AAnp0s9FaOVxRNQzhVpVquzvvqcEpKdp33XBCAKMDBZiArslqlGcw03JhAPm7GRf0Pmr7iIIfePhxjQgsg8zEnWVFrgHLx7WD1-Y_ngXA',
            type: 'image'
        }
        let opt = chartConfig.uploadOption(data);
        form.file('media', uploadFile.file.path, uploadFile.originalFilename, stat.size);
        let upload = https.request(opt, { headers: form.headers() }, (err, res, body) => {
            if (err) {

            }
            console.log('上传成功', body)
        })
        form.pipe(upload)
    }
}

