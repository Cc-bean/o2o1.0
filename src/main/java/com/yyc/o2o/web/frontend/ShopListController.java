package com.yyc.o2o.web.frontend;

import com.yyc.o2o.dto.ShopExecution;
import com.yyc.o2o.entity.Area;
import com.yyc.o2o.entity.Shop;
import com.yyc.o2o.entity.ShopCategory;
import com.yyc.o2o.service.AreaService;
import com.yyc.o2o.service.ShopCategoryService;
import com.yyc.o2o.service.ShopService;
import com.yyc.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ShopListController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;

	@RequestMapping(value = "/listshopspageinfo", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShopsPageInfo(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//从前端获取parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if (parentId != -1) {
			//若parentId存在，则取出该一级shopCategory下的二级shopcategory列表
			try {
				ShopCategory shopCategoryCondition=new ShopCategory();
				ShopCategory parent=new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			try {
				//如果传入空，则去除所有一级ShopCategory
				shopCategoryList = shopCategoryService
						.getShopCategoryList(null);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
			}
		}
		modelMap.put("shopCategoryList", shopCategoryList);
		List<Area> areaList = null;
		try {
			areaList = areaService.getAreaList();
			modelMap.put("areaList", areaList);
			modelMap.put("success", true);
			return modelMap;
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());

		}
		return modelMap;
	}
	/**
	 * 获取指定查询条件下的店铺列表
	 *@params:
	 * @return
	 */
	@RequestMapping(value = "/listshops", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listShops(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取那一夜需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		if ((pageIndex > -1) && (pageSize > -1)) {
			//获取父类id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			//获取子类id
			long shopCategoryId = HttpServletRequestUtil.getLong(request,
					"shopCategoryId");
			//获取地域id
			int areaId = HttpServletRequestUtil.getInt(request, "areaId");
			//模糊查询名字
			String shopName = HttpServletRequestUtil.getString(request,
					"shopName");
			//获取组合后的查询条件
			Shop shopCondition = compactShopCondition4Search(parentId, shopCategoryId, areaId, shopName);
			//查询
			ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
			modelMap.put("shopList", se.getShopList());
			modelMap.put("count", se.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}

		return modelMap;
	}
	//组合参数
	private Shop compactShopCondition4Search(long parentId,
											 long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if (parentId != -1L) {
			ShopCategory parentCategory = new ShopCategory();
			ShopCategory parent=new ShopCategory();
			parent.setShopCategoryId(parentId);
			parentCategory.setParent(parent);
			shopCondition.setShopCategory(parentCategory);
		}
		if (shopCategoryId != -1L) {
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if (areaId != -1L) {
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}

		if (shopName != null) {
			shopCondition.setShopName(shopName);
		}
		//将符合条件的店铺都设置成审核成功的店铺
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}
}
