package com.wheel.service;

import com.wheel.common.exception.ResultCode;
import com.wheel.common.exception.ServiceException;
import com.wheel.controller.response.CustomerShowVO;
import com.wheel.controller.response.ProductVO;
import com.wheel.dao.CustomerShowDao;
import com.wheel.dao.dataObject.CustomerShowDO;
import com.wheel.dao.dataObject.ProductDO;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerShowService {

    @Resource
    CategoryService categoryService;

    @Resource
    CustomerShowDao customerShowDao;

    @Resource
    Mapper beanMapper;


    public Set<String> findAllBrand() {
        Example<CustomerShowDO> example = Example.of(new CustomerShowDO());
        example.getProbe().setIsRemoved(0);
        return customerShowDao.findAll(example)
                .parallelStream()
                .map(CustomerShowDO::getBrandName)
                .collect(Collectors.toSet());

    }

    public CustomerShowDO addOne(CustomerShowDO data) {
        data.setProductId("Show_Id_" + UUID.randomUUID().toString().replace("-", ""));
        return customerShowDao.save(data);
    }

    public Boolean removeOne(String productId) {
        CustomerShowDO record = this.get(productId);
        record.setIsRemoved(1);
        customerShowDao.save(record);
        return Boolean.TRUE;
    }

    public CustomerShowDO editOne(CustomerShowDO update) {
        CustomerShowDO record = this.get(update.getProductId());
        record.update(update);
        record.setIsRemoved(0);
        return customerShowDao.save(record);
    }

    private CustomerShowDO get(String productId) {
        CustomerShowVO param = new CustomerShowVO();
        param.setProductId(productId);
        Example<CustomerShowDO> example = Example.of(beanMapper.map(param, CustomerShowDO.class));
        example.getProbe().setIsRemoved(0);
        CustomerShowDO product = customerShowDao.findOne(example).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST.getCode(), "current product is invalid！"));
        return product;
    }


    /**
     * 模糊搜索
     *
     * @param searchKey
     * @return
     */
    public List<CustomerShowVO> search(String searchKey) {
        Example<CustomerShowDO> example = Example.of(new CustomerShowDO());
        example.getProbe().setIsRemoved(0);
        return customerShowDao.findAll(example)
                .parallelStream()
//                .filter(item -> Objects.item.getIsRemoved())
                .map(CustomerShowVO::read4)
                .collect(Collectors.toList());
    }

    private List<CustomerShowDO> findById(String productId) {
        CustomerShowDO param = new CustomerShowDO();
        param.setProductId(productId);
        Example<CustomerShowDO> example = Example.of(param);
        example.getProbe().setIsRemoved(0);
        return customerShowDao.findAll(example);
    }


    public CustomerShowVO findOneById(String productId) {
        CustomerShowVO param = new CustomerShowVO();
        param.setProductId(productId);
        Example<CustomerShowDO> example = Example.of(beanMapper.map(param, CustomerShowDO.class));
        example.getProbe().setIsRemoved(0);
        return customerShowDao
                .findOne(example)
                .map(CustomerShowVO::read4)
                .orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST.getCode(), "current product is invalid！"));
    }

}