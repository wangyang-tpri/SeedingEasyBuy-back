/**
 * @description 连接mysql模块
 */

const createSelfPool = require('../db/dbconfig.js');
let sqlQueryResult = (sql) => {
    return new Promise((resolve, reject) => {
        try {
            createSelfPool().getConnection((err, conn) => {
                if (err) {
                    reject(err)
                } else {
                    conn.query(sql, (err, result) => {
                        err && reject(err)
                        result && resolve(result)
                        conn.destroy();
                    })
                }
            })
        } catch (error) {
            reject(error)
        } finally {
            createSelfPool().end();
        }
    })
}

module.exports = sqlQueryResult;