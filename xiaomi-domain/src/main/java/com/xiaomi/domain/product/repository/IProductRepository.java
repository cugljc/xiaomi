package com.xiaomi.domain.product.repository;

import com.xiaomi.domain.product.model.entity.ProductEntity;

/**
 * ClassName: IProductRepository
 * Package: com.xiaomi.domain.product.repository
 */
public interface IProductRepository {
    ProductEntity queryProductId(Long id);

    // 新增商品
    boolean insertProduct(ProductEntity productEntity);

    //加锁
    int updateProduct(Long id, Integer amount);
}
