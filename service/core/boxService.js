/**
 * @description 业务逻辑 API 模块
 * 在对发件箱 发送成功 发送失败 页面中的数据进行查找的话 需要使用user_id这个字段 对用户进行区分 然后才能进行查找
 */
const queryStr = require('querystring')
const sqlQueryResult = require('../utils/connecSql');
const constant = require('../utils/constant');
const jsonwebtoken = require('jsonwebtoken');
const weChart = require('../utils/weChart')
const nodeFs = require('node-fs'); // 文件操作库
const formstream = require('formstream');
const request = require('request')
const path = require('path')
const fs = require('fs')
let getData = (req, res, sql) => {
    sqlQueryResult(sql).then((result) => {
        res.send(result)
    }, (err) => {
        console.log(err)
    })
}
let deleteData = (req, res, sql) => {
    sqlQueryResult(sql).then(() => {
        res.send('数据删除成功')
    }, () => {
        res.send('数据删除失败')
    })
}
const allInterfaces = {
    search: ( req, res ) => {

    },
    uploadTreeImage: ( req, res ) => {},
    storedInfo: (req, res ) => {
        const { nurName, nurPri, nurSize, nurPic, nurPhone, nurLocation } = req.body; 
        let sql = `INSERT INTO tree_detail (name, address, phone, diameter, age ) VALUES ( '${nurName}', '${nurLocation}', '${nurPhone}', '${nurSize}', '${nurPri}')`;
        sqlQueryResult(sql).then((result) => {
            res.send('数据插入成功');
        })
    },
    userInfo: (req, res ) => {
        console.log( req.body )
        res.send(('数据发送成功重新启动'))
    },
    getTree:(req, res ) => {
        /**检索最新的10条记录 */
        let sql = 'SELECT * FROM tree_detail ORDER BY detail_id DESC LIMIT 10';
        getData( req, res, sql)
    },

    getFailBox: (req, res) => {
        let query = req.query, sql;
        if (Object.keys(query).length > 1){
            sql = `SELECT * FROM fail_box WHERE box_date > '${query.sTime}' AND box_date < '${query.eTime}'`;
        } else {
            sql = "select * from fail_box";
        }
        getData(req, res, sql)
    },
    getSendBox: (req, res) => {
        let sql, query;
        query = req.query;
        let userId = query.userId;
        if (Object.keys(query).length > 1) {
            sql = `SELECT * FROM send_box WHERE box_date > '${query.sTime}' AND box_date < '${query.eTime}' AND user_id = ${userId}`;
        } else {           
            sql = `SELECT * FROM send_box WHERE user_id = ${userId}`;
        }
        getData(req, res, sql)
    },
    deleteSendBox: (req, res) => {
        let {rowId, reciveName, context, title} = req.body;

        let sql = "DELETE FROM send_box WHERE send_id = " + rowId + " LIMIT 1";
        let sqlDel = `INSERT INTO delete_box (recive_name, context, title) VALUES ('${reciveName}', '${context}', '${title}')`;
        sqlQueryResult(sqlDel).then((result) => {

        })
        deleteData(req, res, sql);
    },
    getInBox: (req, res) => {
        let sql, query;
        query = req.query;
        if (Object.keys(query).length != 0) {
            sql = `SELECT * FROM in_box WHERE box_date > '${query.sTime}' AND box_date < '${query.eTime}'`;
        } else {
            sql = "select * from chartbox.in_box";
        }
        getData(req, res, sql)
    },
    deleteInBox: (req, res) => {
        let inId = req.query.rowId;
        let sql = "delete from chartbox.in_box where in_id = " + inId + " limit 1";
        deleteData(req, res, sql)
    },
    getSuccessBox: (req, res) => {
        let sql, query;
        query = req.query;
        let userId = query.userId;
        if (Object.keys(query).length > 1) {
            sql = `SELECT * FROM send_box WHERE box_date > '${query.sTime}' AND box_date < '${query.eTime}' AND user_id = ${userId}`;
        } else {
            sql = `select * from chartbox.send_box where status = 1 AND user_id = ${userId}`
        }
        getData(req, res, sql)
    },
    deleteSuccess: (req, res) => {
        let {rowId, reciveName, context, title} = req.body;
        /**
         * 将删除的数据插入到delete_box表中
         */

        let sql = `DELETE FROM send_box WHERE send_id = ${rowId} limit 1`;
        let sqlDel = `INSERT INTO delete_box (recive_name, context, title) VALUES ('${reciveName}', '${context}', '${title}')`;
        sqlQueryResult(sqlDel).then((result) => {

        })
        deleteData(req, res, sql)
    },
    deleteFail: (req, res) => {
        let rowId = req.query.rowId;
        let sql = `DELETE FROM fail_box WHERE fail_id = ${rowId} limit 1`;
        deleteData(req, res, sql)
    },
    /**插入数据 */
    insertBox: (req, res) => {
        let query = req.body;
        /**
         * TODO 这里的userid以后是要动态获取的 不能写死的
         */
        let userId = query.userId;
        let title = query.title;
        let context = query.context;
        let reciveName = query.reviceN;
        let imagePath = "";
        let touser = query.touser;
        let digest = query.digest;
        let toparty = query.toparty;
        let totag = query.totag;
        let externalLinks = query.externalLinks;

        // let imageInfo = req.file;
        // let imageName = imageInfo.originalname;
        // let imagePath = `d:/image/${imageName}`;
        /**
         * status = 1 代表的是消息发送成功
         * status = 0 代表的是消息发送失败
         */
        // fs.mkdir('d:/image', { recursive: true }, (err) => {
        //     err && console.log(err)
        // })

        // fs.writeFile(imagePath, Buffer.from(imageInfo.buffer), (err) => {
        //     err && console.log(err)
        // })
        let sql = `INSERT INTO send_box (recive_name, context, title, status, user_id, image_path, touser, toparty, totag, digest, target_url) values ('${reciveName}', '${context}', '${title}', ${userId}, '${userId}', '${imagePath}', '${touser}','${toparty}','${totag}','${digest}','${externalLinks}')`;
        sqlQueryResult(sql).then((result) => {
            res.send('数据插入成功');
        })
    },
    /**用户登录 */
    loginUser: (req, res) => {
        let query = req.body;
        let userName = query.userName;
        let passW = query.password;
        let sql = `SELECT password, user_id, right_control FROM register_user WHERE user_name = '${userName}'`;
        sqlQueryResult(sql).then((result) => {
            if (result[0].password == passW) {
                let primary_key = constant.PRIMARY_KEY = 'helloKitty';
                let token = jsonwebtoken.sign({ name: `${userName}` }, primary_key, { expiresIn: constant.JWT_EXPIRED });
                res.status(200).send({
                    code: 1,
                    msg: '登录成功',
                    token: token,
                    userId: result[0].user_id,
                    role: result[0].right_control
                })
            } else {
                res.status(200).send({
                    code: 2,
                    msg: '用户名或密码错误'
                })
            }
        })
    },
    /**
     * @desc 创建用户组
     * @param {Object} req 
     * @param {Object} res 
     */
    createUserGroup: (req, res) => {
        let body = req.body;
        console.log(body)
        let gName = body.groupName;
        let gDesc = body.groupDesc;
        let gUser = body.groupUsers;
        let userId = body.userId
        let sql = `INSERT INTO user_group (group_name, group_desc, user_id, group_users) VALUES ('${gName}','${gDesc}',${userId},'${gUser}')`;
        sqlQueryResult(sql).then((result) => {
            res.status(200).send({
                msg: '数据插入成功'
            })
        })
    },

    getGroupInfo: (req, res) => {
        let query = req.query;
        let userId = query.userId;
        let sql = `SELECT * FROM user_group WHERE user_id = ${userId} `;
        getData(req, res, sql);
    },
    deleteUserGroup: (req, res) => {

        let query = req.query;
        let gName = query.groupName;
        let sql = `DELETE FROM user_group WHERE group_name = '${gName}' LIMIT 1`;
        deleteData(req, res, sql);
    },
    updateUserGroup: (req, res) => {
        let query = req.body;
        let gName = query.gName;
        let gUsers = query.gUsers;
        let sql = `UPDATE user_group SET group_users = '${gUsers}' WHERE group_name = '${gName}'`;
        sqlQueryResult(sql).then((result) => {
            res.status(200).send({
                msg: '数据更新成功'
            })
        })
    },
    getWeChartInfo: (req, res) => {
        weChart.getWeChartAccessToken(req, res)

        /**这是一个单独提炼的方法 后面再用 */
        // weChart.insertDepartmentList(req, res)
    },
    getList: (req, res) => {
        weChart.getDepartmentList(req, res)
    },
    getEmployeeList: (req, res) => {
        weChart.getEmployeeFromDatabase(req, res)
    },
    getAddressTokenForClient: (req, res) => {
        let sql = "SELECT * FROM chart_token WHERE name = 'addressBook' ORDER BY id DESC LIMIT 0, 1 ";
        sqlQueryResult(sql).then((result) => {
            res.status(200).send({
                token: result[0]
            })
        })
    },
    sendChartMsg: (req, res) => {
        weChart.setChartMsg(req, res);
    },
    getTagList: (req, res) => {
        weChart.getTagList(req, res)
    },
    getTagMem: (req, res) => {
        weChart.getTagMembers(req, res)
    },
    /**
     * 上传图片
     */
    uploadImage: (req, res) => {
        let sql = "SELECT * FROM chart_token WHERE name = 'global' ORDER BY id DESC LIMIT 0, 1";
        let uploadFile = req.files.file;
        let fileName = uploadFile.name;
        sqlQueryResult(sql).then((result) => {
            let token = result[0].token;
            var boundaryKey = Math.random().toString(16).split('.')[1];
            var options = {
                host: 'qyapi.weixin.qq.com',
                path: '/cgi-bin/media/upload?access_token=' + token + '&type=image',
                method: 'POST'
            };
            var reqHttps = https.request(options, function (resHttps) {
                resHttps.on('data', function (body1) {
                    let resData = JSON.parse(body1)
                    let mediaId = resData.media_id;
                    let sql = `INSERT INTO thumb_image (image_name, thumb_media_id) VALUES ("${fileName}", "${mediaId}")`;
                    sqlQueryResult(sql).then((result) =>{
                        res.send({
                            code: 1,
                            msg: '图片上传成功'
                        })
                    })
                });
            });
            let enddata = `\r\n--${boundaryKey}--\r\n`;
            let payload = `--${boundaryKey}\r\n` +
                `Content-Dispostion:form-data; name="media"; filename="${uploadFile.originalFilename}";filelength=${uploadFile.size}\r\n` +
                `Content-Type:${uploadFile.type}\r\n\r\n`;
            reqHttps.setHeader('Content-Type', 'multipart/form-data; boundary=' + boundaryKey);
            reqHttps.setHeader('Content-Length', Buffer.byteLength(payload) + Buffer.byteLength(enddata) + uploadFile.size);
            reqHttps.write(payload);
            let fileStream = fs.createReadStream(uploadFile.path, { bufferSize: 4 * 1024 });
            fileStream.pipe(reqHttps, { end: false });
            /**
             * 当将文件中的数据读取完成后 才会触发 'end' 事件
             */
            fileStream.on('end', function () {
                reqHttps.end(enddata);
            });
            reqHttps.on('error', function (e) {
                console.error("error:" + e);
            });
        })
    },
    /**
     * 创建用户
     */
    createUser: (req, res) => {
        let query = req.body;
        let userName = query.userName;
        let password = query.password;
        let userControl = query.userControl;

        let gUsers = query.gUsers;
        let gName = query.gName;
        let updateSql = `UPDATE user_group SET group_users = '${gUsers}' WHERE group_name = '${gName}'`;
        let insertSql = `INSERT INTO register_user (user_name, password, right_control, group_name) VALUES ('${userName}', '${password}', '${userControl}', '${gName}')`;
        let selectSql = `SELECT password FROM register_user WHERE user_name = '${userName}'`;
        sqlQueryResult(selectSql).then((result) => {
            if (result.length > 0) {
                res.send({
                    code: 2,
                    msg: '此用户已存在'
                })
            } else {
                sqlQueryResult(insertSql).then((result) => {
                    sqlQueryResult(updateSql).then((result) => {})
                    res.send({
                        code: 1,
                        msg: '用户创建成功'
                    })
                })
            }
        })
    },
    /**
     * @description 删除单个用户
     * @param {object} req 
     * @param {object} res 
     */
    deleteUser: (req, res) => {
        let query = req.body;
        let gName = query.gName;
        let user = query.user;
        let sql = `DELETE FROM register_user WHERE group_name = '${gName}'
        AND user_name = '${user}'`;
        deleteData(req, res, sql);
    },
    /**
     * @description 删除整个用户组下面的用户
     * @param {object} req 
     * @param {object} res 
     */
    deleteAllGroupUsers: (req, res) => {
        let {groupName} = req.body;
        let sql = `DELETE FROM register_user WHERE group_name = '${groupName}'`;
        deleteData(req, res, sql);
    },
    getUserInfo: (req, res) => {
        let {groupName} = req.body;
        let sql = `SELECT user_name FROM register_user WHERE group_name = '${groupName}'`;
        getData(req, res, sql);
    },
    addTemplate: (req, res) => {
        let {name, user, title, digest, url, content, userId, employee, departy, totag} = req.body;
        let sql = `INSERT INTO msg_template (tem_name, tem_user, tem_title, tem_digest, tem_url, tem_content, user_id, touser, toparty, totag) VALUES ('${name}', '${user}', '${title}','${digest}','${url}', '${content}',${userId}, '${employee}', '${departy}','${totag}')`;
        getData(req, res, sql);
    },
    getAllTem: (req, res) => {
        let {userId} = req.query;
        let sql = `SELECT tem_name FROM msg_template WHERE user_id = ${userId}`;
        getData(req, res, sql);
    },
    getSelfTem: (req, res) => {
        let {userId, tem_name} = req.query;
        let sql = `SELECT * FROM msg_template WHERE user_id = ${userId} AND tem_name = '${tem_name}'`;
        getData(req, res, sql);
    },
    getAllDeleteData: (req, res) => {

    
        let sql = `SELECT * FROM delete_box`;
        getData(req, res, sql);
    },
    deleteDataFromDeleteBox: (req, res) => {
        let {id} = req.query;
        let sql = `DELETE FROM delete_box WHERE id = ${id}`;
        deleteData(req, res, sql);
    },
    saveKeyWords: (req, res) => {
        let {keyWords} = req.body;
        let sql = `INSERT INTO key_word (words) VALUE ('${keyWords}')`;
        sqlQueryResult(sql).then((result) => {
            res.send({
                msg: '关键词插入成功'
            })
        })
    },
    getKeyWords: (req, res) => {
        let sql = `SELECT * FROM key_word`;
        getData(req, res, sql)
    },
    deleteKeyWord: (req, res) => {
        let {word} = req.body;
        let sql =  `DELETE FROM key_word WHERE words = '${word}'`;
        deleteData(req, res, sql);
    },
    saveFailBox: (req, res) => {
        let {reciveName, content, title, employee, departmentData, tag, digest} = req.body;
        let sql = `INSERT INTO fail_box (recive_name, context, title, touser, toparty, totag, digest) VALUES ('${reciveName}', '${content}', '${title}','${employee}','${departmentData}','${tag}','${digest}')`;
        sqlQueryResult(sql).then((result) => {
            res.send({
                msg: '消息插入成功'
            })
        })

    }

}
module.exports = allInterfaces;




