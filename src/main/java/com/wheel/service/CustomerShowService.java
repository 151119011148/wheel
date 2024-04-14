package com.wheel.service;

import com.google.common.collect.Lists;
import com.wheel.common.exception.ResultCode;
import com.wheel.common.exception.ServiceException;
import com.wheel.controller.request.ShowQueryParam;
import com.wheel.controller.response.CustomerShowVO;
import com.wheel.controller.response.ProductVO;
import com.wheel.dao.CustomerShowDao;
import com.wheel.dao.dataObject.CustomerShowDO;
import com.wheel.dao.dataObject.ProductDO;
import org.apache.commons.lang3.StringUtils;
import org.dozer.Mapper;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    CustomerShowDao customerShowDao;



    public Set<String> findAllBrand() {
        Example<CustomerShowDO> example = Example.of(new CustomerShowDO());
        example.getProbe().setIsRemoved(0);
        return customerShowDao.findAll(example)
                .parallelStream()
                .map(CustomerShowDO::getBrandName)
                .collect(Collectors.toSet());

    }

    public CustomerShowDO addOne(CustomerShowDO data) {
        data.setCustomerShowId("Show_Id_" + UUID.randomUUID().toString().replace("-", ""));
        return customerShowDao.save(data);
    }

    public Boolean removeOne(String showId) {
        CustomerShowDO record = this.get(showId);
        record.setIsRemoved(1);
        customerShowDao.save(record);
        return Boolean.TRUE;
    }

    public void remove(List<String> showIds) {
        List<CustomerShowDO> records = Lists.newArrayList();
        showIds.parallelStream().forEach(id -> {
            records.addAll(this.findById(id));
        });
        records.parallelStream()
                .peek(record -> record.setIsRemoved(1))
                .forEach(this::editOne);
    }

    public CustomerShowDO editOne(CustomerShowDO update) {
        CustomerShowDO record = this.get(update.getCustomerShowId());
        record.update(update);
        record.setIsRemoved(0);
        return customerShowDao.save(record);
    }

    private CustomerShowDO get(String showId) {
        CustomerShowDO param = new CustomerShowDO();
        param.setCustomerShowId(showId);
        Example<CustomerShowDO> example = Example.of(param);
        example.getProbe().setIsRemoved(0);
        CustomerShowDO product = customerShowDao.findOne(example).orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST.getCode(), "current product is invalid！"));
        return product;
    }


    public Page<CustomerShowVO> page(ShowQueryParam param) {
        PageRequest pageRequest = new PageRequest(param.getPageIndex(), param.getPageSize());
        Example<CustomerShowDO> example = Example.of(new CustomerShowDO());
        example.getProbe().setIsRemoved(0);
        if (StringUtils.isEmpty(param.getQueryValue())){
            Page<CustomerShowDO> pageRecord = customerShowDao.findAll(example, pageRequest);
            Page<CustomerShowVO> pageResult = new PageImpl(pageRecord.getContent().parallelStream()
                    .map(CustomerShowVO::read4)
                    .collect(Collectors.toList()), pageRecord.getPageable(), pageRecord.getTotalElements());
            return pageResult;
        }
        Page<CustomerShowDO> pageRecord = customerShowDao.pageFuzzy(param.getQueryValue(), pageRequest);
        Page<CustomerShowVO> pageResult = new PageImpl(pageRecord.getContent().parallelStream()
                .map(CustomerShowVO::read4)
                .collect(Collectors.toList()), pageRecord.getPageable(), pageRecord.getTotalElements());
        return pageResult;
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
                .map(CustomerShowVO::read4)
                .collect(Collectors.toList());
    }

    private List<CustomerShowDO> findById(String showId) {
        CustomerShowDO param = new CustomerShowDO();
        param.setCustomerShowId(showId);
        Example<CustomerShowDO> example = Example.of(param);
        example.getProbe().setIsRemoved(0);
        return customerShowDao.findAll(example);
    }


    public CustomerShowVO findOneById(String showId) {
        CustomerShowDO param = new CustomerShowDO();
        param.setCustomerShowId(showId);
        Example<CustomerShowDO> example = Example.of(param);
        example.getProbe().setIsRemoved(0);
        return customerShowDao
                .findOne(example)
                .map(CustomerShowVO::read4)
                .orElseThrow(() -> new ServiceException(ResultCode.USER_NOT_EXIST.getCode(), "current product is invalid！"));
    }



}
