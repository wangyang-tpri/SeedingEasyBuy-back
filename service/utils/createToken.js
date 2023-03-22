/**
 * 新建token的模块
 * token 由三部分组成 中间用.隔开
 * 
 * 1.header
 * 2.payload(载荷)
 * 3.signature(署名)
 */
const crypto = require('crypto');
const jsonwebtoken = require('jsonwebtoken');
module.exports = {
    createToken: () => {
       jsonwebtoken 
    }
}
const hmac = crypto.createHmac('sha256', 'secret-key');
hmac.update('hello world')
console.log(hmac.digest('hex'))