$(function () {
    var shopId=getQueryString('shopId');
    var shopInfoUrl='/myo2o/shopadmin/getshopmanagementinfo?shopId='+shopId;
    $.getJSON(shopInfoUrl,function (data) {
        if(data.redirect){
            window.location.href=data.url;
        }else {
            if(data.shopId!=undefined && data.shopId!=null){
                shopId=data.shopId;
            }//获取该shopId下的信息
            $('#shopInfo').attr('href','/myo2o/shopadmin/shopoperation?shopId='+shopId);
        }
    });
});