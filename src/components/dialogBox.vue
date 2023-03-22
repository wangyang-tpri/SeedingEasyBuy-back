/** 
    查看信息详情的组件
*/
<template>
    <div>
        <el-dialog
            :visible.sync="showDialog"
            title="信息详情展示"
            width="40%"
        >
            
        </el-dialog>
    </div>
</template>

<script>
export default {
    props: ['name', 'title', 'content', 'showDialog'],
    name: 'dialogBox',
    data() {
        return {
            msg: '信息详情的组件',
            showDialog: false
        }
    },
}
</script>

<style>

</style> 