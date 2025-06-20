package com.xiaomi.infrastructure.persistent.dao;

import com.xiaomi.infrastructure.persistent.po.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: ProductDao
 * Package: com.xiaomi.infrastructure.persistent.dao
 */
@Mapper
public interface ProductDao {

    int insert(Product product);

    Product selectById(Long id);

    /**
     * 扣减库存
     * @param id 商品ID
     * @param amount 扣减数量
     * @return 影响行数
     */
    int decreaseStock(Long id, Integer amount);
}
