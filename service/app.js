const bodyParse = require('body-parser')
const express = require('express');
const router = require('./selfRouter');
const app = express();
app.use(bodyParse.json())
app.use(bodyParse.urlencoded({ extend: false }))
app.use('/', router)
app.listen(8081, () => {
    console.log('服务已启动: localhost:8081')
})

