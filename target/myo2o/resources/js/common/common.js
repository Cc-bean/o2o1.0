/**
 *函数参数为图片本身
 *
 **/
function  changeVerifyCode(img) {
    //提交给servlet生成验证码,传入四位随机数生成验证码
        //Java中random()代表返回一个[0,1)的浮点数。 所以 math.random()*100代表返回一个[0,100)的浮点数。
    img.src ="../Kaptcha?" + Math.floor(Math.random() * 100);

}

//从url的key中获取value
function getQueryString(name) {
    //用正则表达式匹配出符合的参数名
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);//返回给参数名的值
    }
    return '';
}
//日期格式处理
Date.prototype.Format = function(fmt) {
    var o = {
        "M+" : this.getMonth() + 1, // 月份
        "d+" : this.getDate(), // 日
        "h+" : this.getHours(), // 小时
        "m+" : this.getMinutes(), // 分
        "s+" : this.getSeconds(), // 秒
        "q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
        "S" : this.getMilliseconds()
        // 毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    for ( var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
                : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
