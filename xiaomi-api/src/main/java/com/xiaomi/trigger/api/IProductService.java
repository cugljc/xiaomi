package com.xiaomi.trigger.api;

import com.xiaomi.trigger.api.dto.ProductDTO;

/**
 * ClassName: IProcutService
 * Package: com.xiaomi.trigger.api
 */
public interface IProductService {
    public ProductDTO queryProductId(Long id);
    public int updateProduct(Long id, Integer amount);
    public boolean insertProduct(ProductDTO product);

}
