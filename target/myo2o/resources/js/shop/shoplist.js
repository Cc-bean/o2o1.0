/**
 *
 */
$(function () {
    getlist();
    function getlist() {
        $.ajax({
            url:"/myo2o/shopadmin/getshoplist",
            type:"get",
            dataType:"json",
            success:function (data) {
                if (data.success){
                    handleList(data.shopList);//执行追加列表信息方法
                    handleUser(data.user);//执行追加用户信息方法
                }
            }
        });//ajax结尾加;
    }
    function handleUser(data) {
        $('#user-name').text(data.name);
    }

    function handleList(data) {
        var html='';
        data.map(function (item, index) {
            html += '<div class="row row-shop"><div class="col-40">'
                + item.shopName +'</div><div class="col-40">'
                + shopStatus(item.enableStatus) +'</div><div class="col-20">'
                + goShop(item.enableStatus, item.shopId) +'</div></div>';
        });
        $('.shop-wrap').html(html);
    }

    function shopStatus(status) {
        if (status == 0) {
            return '审核中';
        } else if (status == -1) {
            return '店铺非法';
        } else if(status==1){
            return '审核通过';
        }
    }
    //进入店铺的链接
    function goShop(status, id) {
        if (status != 0 && status != -1) {
            return '<a href="/myo2o/shopadmin/shopmanagement?shopId='+ id +'">进入</a>';
        } else {
            return '';
        }
    }





});//总方法结尾
