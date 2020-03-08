$(function () {
    $("#log-out").click(function () {
        //清除
        $.ajax({
            url:"/myo2o/local/logout",
            type:"post",
            async:false,
            cache:false,
            dataType:'json',
            success:function (data) {
                if(data.success){
                    var usertype=$("#log-out").attr("usertype");
                    //清除后退出登录界面
                    window.location.href="/myo2o/local/login?usertype="+usertype;
                    return false;
                }
            },
            error:function (data,error) {
                alert(error);
            }
        });
    });

});