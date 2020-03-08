/*
 *获取商铺分类和区域分类列表
 */
$(function () {
    var shopId=getQueryString('shopId');
    var isEdit=shopId?true:false;//如果shopid为空则执行创建店铺的方法，如果shopid不为空则执行更新店铺的方法
    var initUrl = '/myo2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/myo2o/shopadmin/registershop';
    var modifyShopUrl="/myo2o/shopadmin/modifyshop";
    var shopInfoUrl="/myo2o/shopadmin/getshopbyid?shopId="+shopId;
    var ShopUrl=isEdit?modifyShopUrl:registerShopUrl;

    //1，由id获取店铺信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function(data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-addr').val(shop.shopAddr);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                var shopCategory = '<option data-id="'
                    + shop.shopCategory.shopCategoryId + '" selected>'
                    + shop.shopCategory.shopCategoryName + '</option>';
                var tempAreaHtml = '';
                data.areaList.map(function(item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled','disabled');//店铺类别设置为disabled，不能被用户选择
                $('#area').html(tempAreaHtml);
                $("#area option[data-id='"+shop.area.areaId+"']").attr("selected","selected");//区域下拉列表默认选择该ID区域的信息
            }
        });
    }
    //2，获取两个列表信息
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data) {
            console.log(data)
            if (data.success) {
                var tempHtml = "";
                var tempAreaHtml = "";
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id=' + item.shopCategoryId + '>'
                        + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id=' + item.areaId + '>'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
        //获取提交的内容

    }
    //3，判断是只获取列表信息，还是由id获得店铺信息
    if (isEdit) {
        getShopInfo(shopId);
    } else {
        getShopInitInfo();
    }
    //4，点击事件，提交表单中的信息
    $('#submit').click(function () {
        var shop = {};
        if(isEdit){
            shop.shopId=shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        if(ShopUrl==registerShopUrl){//是注册的化，从html获取shopCategory
        shop.shopCategory = {
            shopCategoryId: $('#shop-category').find('option').not(function () {
                return !this.selected;//双重否定等肯定
            }).data('id')
        };
        }else {//否则发送url获取
            $.getJSON(shopInfoUrl, function(data) {
                if (data.success) {
                    var shop = data.shop;
                    shop.shopCategory =shop.shopCategory;
                    }
            });
        }
        shop.area={
            areaId:$('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };

        //获取图片的文件流
        var shopImg=$("#shop-img")[0].files[0];

        var formData=new FormData();
        formData.append('shopImg',shopImg);
        formData.append('shopStr',JSON.stringify(shop));
        //将验证码传入后台
        var verifyCodeActual=$('#j_captcha').val();
        if(!verifyCodeActual){//为空提示，不为空则加入formdata
            $.toast('请输入验证码！');
            return ;
        }
        formData.append('verifyCodeActual',verifyCodeActual);
        //ajax提交到后台
        $.ajax({
            url:ShopUrl,
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function (data) {
                if (data.success){
                    $.toast('提交成功！');
                }else{
                    $.toast('提交失败！'+data.errMsg);
                }
                $('#captcha_img').click();//点击提交后不管提交成功与否都触发点击方法更验证码
            }
        })

    });
})




