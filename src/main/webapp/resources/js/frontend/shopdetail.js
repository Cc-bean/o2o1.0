$(function() {
    //加载符定义
    var loading = false;
    //分页允许返回的最大条数，超过次数则禁止访问后台
    var maxItems =30;//改数字会被修改为商品总数
    //默认一页返回的商品数
    var pageSize = 10;
    //列出商品列表的url
    var listUrl = '/myo2o/frontend/listproductsbyshop';
    var pageNum = 1;
    var shopId = getQueryString('shopId');
    var productCategoryId = '';
    var productName = '';
    //商品详情信息
    var searchDivUrl = '/myo2o/frontend/listshopdetailpageinfo?shopId='
        + shopId;
    //渲染出店铺基本信息及商品类别列表以方便搜索
    getSearchDivData();
    //预先加载10条商品信息
    addItems(pageSize, pageNum);
    //获取本店铺信息以及商品类别信息
    function getSearchDivData() {
        var url = searchDivUrl;
        $.getJSON(url,function(data) {
                    if (data.success) {
                        var shop = data.shop;
                        $('#shop-cover-pic').attr('src', shop.shopImg);
                        $('#shop-update-time').html(
                            new Date(shop.lastEditTime)
                                .Format("yyyy-MM-dd"));
                        $('#shop-name').html(shop.shopName);
                        $('#shop-desc').html(shop.shopDesc);
                        $('#shop-addr').html(shop.shopAddr);
                        $('#shop-phone').html(shop.phone);
                        //获取后台返回的店铺商品类别列表
                        var productCategoryList = data.productCategoryList;
                        var html = '';
                        productCategoryList
                            .map(function(item, index) {
                                html += '<a href="#" class="button" data-product-search-id='
                                    + item.productCategoryId
                                    + '>'
                                    + item.productCategoryName
                                    + '</a>';
                            });
                        $('#shopdetail-button-div').html(html);
                    }
                });
    }


    //获取分页展示的商品信息列表
    function addItems(pageSize, pageIndex) {
        // 生成新条目的HTML
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&productCategoryId=' + productCategoryId
            + '&productName=' + productName + '&shopId=' + shopId;
        //设定加载符，若还在后台取数据则不能再次访问后台，避免重复加载
        loading = true;
        //访问后台获取相应查询条件下的商品列表
        $.getJSON(url, function(data) {
            if (data.success) {
                //获取总数赋给最大加载数
                maxItems = data.count;
                var html = '';
                //遍历商品列表，拼接商品卡片
                data.productList.map(function(item, index) {
                    html += '' + '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.productName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.imgAddr + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.productDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
                //计算目前已加载商品总数
                var total = $('.list-div .card').length;
                if (total >= maxItems) {
                    // 隐藏加载提示符
                    $('.infinite-scroll-preloader').remove();
                }else{
                    $('.infinite-scroll-preloader').show();
                }
                pageNum += 1;
                loading = false;
                $.refreshScroller();
            }
        });
    }


    //下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });
    //选择新的商品类别之后，重置页码，清空原先列表，按照新的查询
    $('#shopdetail-button-div').on('click', '.button',
        function(e) {
            productCategoryId = e.target.dataset.productSearchId;
            if (productCategoryId) {
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    productCategoryId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                $('.list-div').empty();
                pageNum = 1;
                addItems(pageSize, pageNum);
            }
        });
//点击商品的卡片计入该商品的详情页
    $('.list-div').on('click','.card',
            function(e) {
                var productId = e.currentTarget.dataset.productId;
                window.location.href = '/myo2o/frontend/productdetail?productId='
                    + productId;
            });
//需要查询的商品名字发生变化后，重置页码，清空原来商品列表，按照新名字查询
    $('#search').on('input', function(e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });
//点击打开侧边栏
    $('#me').click(function() {
        $.openPanel('#panel-left-demo');
    });
    $.init();
});
