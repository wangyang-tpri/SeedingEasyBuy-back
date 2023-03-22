<template>
    <div>
        <span>{{ msg }}</span>
    </div>
</template>

<script>
export default {
    data() {
        return {
            msg: '信息详情页',
            presentItem: ''
        }
    },
    methods: {
        
    },
    mounted() {
    },    
}
</script>

<style scoped>
.active{
    color: #fff;
    background: #f67d28;
}
</style>