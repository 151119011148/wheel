package com.wheel.dao;

import com.wheel.dao.dataObject.CategoryDO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @Description: TODO
 * @Author gaofeng
 * @Date 2023/9/3 8:34 PM
 **/
public interface CategoryDao extends JpaRepository<CategoryDO, Long> {



    @Override
    default <S extends CategoryDO> Optional<S> findOne(Example<S> example) {
        List<S> result = this.findAll(example);
        if (CollectionUtils.isEmpty(result)){
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    List<CategoryDO> findByCategoryIdIn(List<String> categoryIds);




}
