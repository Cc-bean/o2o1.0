$(function () {
    //从Url中获取productId的值

    var productId=getQueryString('productId');
    //通过productId获取商品信息的url
    var infoUrl='/myo2o/shopadmin/getproductbyid?productId='+productId;
    console.log(productId);
    //获取当前店铺设定的商品类别列表的url
    var categoryUrl='/myo2o/shopadmin/getproductcategorylist';
    //更新商品信息的url
    var productPostUrl='/myo2o/shopadmin/modifyproduct'

    //判断是编辑还是添加
    var isEdit=false;//标志本页是添加还是编辑
    if(productId){
        getInfo(productId);
        isEdit=true;
    }else {
        getCategory();//获取列表信息
        productPostUrl='/myo2o/shopadmin/addproduct';
    }

    //获取需要编辑的商品信息，赋予表单
    function getInfo(id) {
        $.getJSON(infoUrl,function (data) {
            if(data.success){
                //console.log(data.product);
                var product = data.product;
                //从返回的json当中获取product对象的信息，并赋值给表单
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                //获取原本的商品类别以及该店铺的所有列表信息
                var optionHtml='';
                var optionArr=data.productCategoryList;
                var optionSelected=product.productCategory.productCategoryId;
                //生成前端的html商品类别列表，并默认选择编辑前的商品类别
                optionArr.map(function (item, index) {
                    var isSelect=optionSelected===item.productCategoryId?'selected':'';
                    optionHtml+='<option data-value="'
                        +item.productCategoryId
                        +'"'
                        +isSelect
                        +'>'
                        +item.productCategoryName
                        +'</option>';
                });
                $('#category').html(optionHtml);

            }else {
                //$.toast(data.errMsg);
                window.location.href = "/myo2o/shopadmin/productmanagement";
            }
        });
    }

    //为商品添加操作提供该店铺下的所有商品类别列表
    function getCategory() {
        $.getJSON(categoryUrl,function (data) {
            if(data.success){
                var productCategoryList=data.data;
                var optionHtml='';
                productCategoryList.map(function (item, index) {
                    optionHtml+='<option data-value="'
                    +item.productCategoryId+'">'
                    +item.productCategoryName+'</option>';
                });
                $('#category').html(optionHtml);
            }

        })
    }
    //若上传图片最后一个控件元素发生了变化，且空间数量未达到六个，则新生成一个文件上传控件
    $('.detail-img-div').on('change', '.detail-img:last-child', function() {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    });
    
    
    //点击提交按钮，对商品添加和编辑做出不同的相应
    $('#submit').click(
        function () {
            //创建商品json对象，并从表单里米娜获取对应的属性值
            var product={};
            product.productName=$('#product-name').val();
            product.productDesc=$('#product-desc').val();
            product.priority=$('#priority').val();
            product.point=$('#point').val();
            product.normalPrice=$('#normal-price').val();
            //获取选定的商品类别值
            product.productCategory={
                productCategoryId:$('#category').find('option').not(
                    function () {
                        return !this.selected;
                    }).data('value')
            };
            product.productId=productId;

            //获取缩略图文件流
            var thumbnail=$('#small-img')[0].files[0];
            //生成表单对象，用于接收参数并传递给后台
            var formData=new FormData();
            formData.append("thumbnail",thumbnail);
            //遍历商品详情图控件获取文件流
            $('.detail-img').map(
                function (index,item) {
                    //判断该控件是否已选择文件
                    if($('.detail-img')[index].files.length>0){
                        //将文件添加进入formdata
                        formData.append('productImg'+index,$('.detail-img')[index].files[0]);
                    }
                });
            //将product json对象转换为字符串流保存值表单对象key为productStr的键值对里
            formData.append('productStr',JSON.stringify(product))
            //获取验证码
            var verifyCodeActual=$('#j_captcha').val();
            if(!verifyCodeActual){
                $.toast("请输入验证码");
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
                url : productPostUrl,
                type : 'POST',
                data : formData,
                contentType : false,
                processData : false,
                cache : false,
                success : function(data) {
                    if (data.success) {
                        $.toast('提交成功！');
                        $('#captcha_img').click();
                    } else {
                        $.toast('提交失败!'+data.errMsg);
                        $('#captcha_img').click();
                    }
                }
            });
        });


});