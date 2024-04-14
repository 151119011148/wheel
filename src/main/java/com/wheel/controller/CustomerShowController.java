package com.wheel.controller;

import com.wheel.common.exception.ResultCode;
import com.wheel.controller.request.ShowQueryParam;
import com.wheel.controller.response.CustomerShowVO;
import com.wheel.controller.response.Pager;
import com.wheel.controller.response.ProductVO;
import com.wheel.controller.response.Response;
import com.wheel.dao.dataObject.CustomerShowDO;
import com.wheel.service.CustomerShowService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * @Description: 买家秀
 * @Author gaofeng
 * @Date 2023/9/3 9:54 AM
 **/
@RestController
@RequestMapping("/service/customer/show")
public class CustomerShowController extends BaseController {

    @Resource
    CustomerShowService customerShowService;


    /**
     * 查询品牌列表
     *
     * @return
     */
    @GetMapping("/brand")
    public Response<List<ProductVO>> findAllBrand() {
        Set<String> data = customerShowService.findAllBrand();
        return new Response<>().withData(data);
    }


    /**
     * 新增 show
     *
     * @param param
     * @return
     */
    @PostMapping("")
    public Response<CustomerShowVO> add(@RequestBody CustomerShowVO param) {
        CustomerShowDO data = customerShowService.addOne(CustomerShowVO.convert2(param));
        return new Response<>()
                .withData(CustomerShowVO.read4(data))
                .withErrorMsg("添加show成功");
    }

    /**
     * 通过 id 删除show
     *
     * @param showId
     * @return
     */
    @DeleteMapping("/{showId}")
    public Response<Boolean> removeOne(@PathVariable String showId) {
        customerShowService.removeOne(showId);
        return new Response<>()
                .withData(Boolean.TRUE)
                .withErrorMsg("删除show成功");
    }

    /**
     * 通过 id 批量删除show
     *
     * @param showIds
     * @return
     */
    @PostMapping("/remove")
    public Response<Boolean> remove(@RequestBody List<String> showIds) {
        customerShowService.remove(showIds);
        return new Response<>()
                .withData(Boolean.TRUE)
                .withErrorMsg("删除show成功");
    }


    /**
     *
     * 分页模糊查询
     * @return
     */
    @GetMapping("/page")
    public Response<List<CustomerShowVO>> search(ShowQueryParam param) {
        if (Objects.isNull(param.getPageIndex()) || Objects.isNull(param.getPageSize())){
            return new Response<>(ResultCode.PARAM_ERROR.getCode(), "page must not null!");
        }
        Integer pageIndex = param.getPageIndex();
        param.setPageIndex(pageIndex - 1);

        Page<CustomerShowVO> pageResult = customerShowService.page(param);
        Pager pager = new Pager(pageIndex, param.getPageSize(), pageResult.getTotalElements());
        return new Response<>()
                .withPager(pager)
                .withData(pageResult.getContent());
    }




    /**
     * 通过 productId 修改商品信息
     *
     * @return
     */
    @PutMapping("/{showId}")
    public Response<CustomerShowVO> edit(@PathVariable String showId, @RequestBody CustomerShowVO param) {
        CustomerShowDO customerShow = customerShowService.editOne(CustomerShowVO.convert2(param));
        return new Response<>()
                .withData(CustomerShowVO.read4(customerShow))
                .withErrorMsg("修改信息成功");
    }
}
