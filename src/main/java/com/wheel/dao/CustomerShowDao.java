package com.wheel.dao;

import com.wheel.dao.dataObject.CustomerShowDO;
import com.wheel.dao.dataObject.ProductDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author gaofeng
 * @Date 2023/9/3 8:34 PM
 **/
public interface CustomerShowDao extends JpaRepository<CustomerShowDO, Long> {



    @Override
    default <S extends CustomerShowDO> Optional<S> findOne(Example<S> example) {
        List<S> result = this.findAll(example);
        if (CollectionUtils.isEmpty(result)){
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Query("select show from CustomerShowDO show where show.productName like %:fuzzy% " +
            "or show.categoryName like %:fuzzy% " +
            "or show.brandName like %:fuzzy% " +
            "or show.carModel like %:fuzzy% ")
    <S extends CustomerShowDO> Page<S> pageFuzzy(@Param("fuzzy") String fuzzy, Pageable pageable);





}
