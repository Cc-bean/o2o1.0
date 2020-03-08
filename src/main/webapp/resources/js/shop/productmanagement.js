$(function() {
	var listUrl = '/myo2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
	//商品下架Irl
	var statusUrl = '/myo2o/shopadmin/modifyproduct';

	function getList() {
		//从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				//遍历并拼接商品信息productList（map类型）
				productList.map(function(item, index) {
					var textOp = "下架";//默认下架
					var contraryStatus = 0;//默认下架
					if (item.enableStatus == 0) {
						//如状况值0，表明已下架的商品，点击后变为上架（点击上架按钮上架相关商品）
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-30">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.point
							+ '</div>'
							+ '<div class="col-50">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus//保存相反状态
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	getList();
	//下架商品的方法
	function deleteItem(id, enableStatus) {
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true//下架修改商品状态信息跳过验证码验证
				},//data中参数都是可以直接从request中获取的
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();//刷新列表
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}

	//将class为product-wrap里面的a标签绑定点击事件
	$('.product-wrap')
			.on('click','a',function(e) {
						var target = $(e.currentTarget);//获取当前点击按钮
						if (target.hasClass('edit')) {//如果是编辑则触发编辑功能带productId :::data-id="'+ item.productId
							window.location.href = '/myo2o/shopadmin/productoperation?productId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('status')) {
							deleteItem(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							window.location.href = '/myo2o/frontend/productdetail?productId='
									+ e.currentTarget.dataset.id;
						}
					});

	$('#new').click(function() {
		window.location.href = '/myo2o/shop/productedit';
	});
});