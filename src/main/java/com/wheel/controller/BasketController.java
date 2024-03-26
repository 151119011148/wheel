package com.wheel.controller;

import com.alibaba.fastjson.JSON;
import com.wheel.controller.request.BasketParam;
import com.wheel.controller.response.BasketVO;
import com.wheel.controller.response.ProductVO;
import com.wheel.controller.response.Response;
import com.wheel.dao.dataObject.BasketDO;
import com.wheel.service.BasketService;
import com.wheel.service.ProductService;
import org.dozer.Mapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author gaofeng
 * @Date 2023/9/3 9:54 AM
 **/
@RestController
@RequestMapping("/service/basket")
public class BasketController extends BaseController {

    @Resource
    BasketService basketService;

    @Resource
    ProductService productService;

    @Resource
    Mapper beanMapper;

    /**
     * 加入购物车
     *
     * @param
     * @return
     */
    @PostMapping("/add")
    public Response<BasketVO> add(@RequestBody List<String> productIds) {
        String ip = getIp();
        BasketParam param = new BasketParam();
        param.setVisitId(ip);
        param.setProductIds(productIds);
        BasketDO data = basketService.add(param);
        BasketVO result = buildBasketVO(ip, JSON.parseObject(data.getProductIds(), List.class));
        return new Response<>()
                .withData(result)
                .withErrorMsg("加入购物车成功");
    }


    /**
     * 从购物车 移除
     *
     * @param
     * @return
     */
    @PostMapping("/remove")
    public Response<BasketVO> remove(@RequestBody List<String> productIds) {
        String ip = getIp();
        BasketParam param = new BasketParam();
        param.setVisitId(ip);
        param.setProductIds(productIds);
        BasketDO data = basketService.remove(param);
        BasketVO result = buildBasketVO(ip, JSON.parseObject(data.getProductIds(), List.class));
        return new Response<>()
                .withData(result)
                .withErrorMsg("商品移除成功");
    }

    private BasketVO buildBasketVO(String ip, List<String> productIds) {
        List<ProductVO> productData = productService.findByIds(productIds);


        BasketVO result = new BasketVO();
        result.setVisitId(ip);
        result.setProducts(productData);
        return result;
    }



}
