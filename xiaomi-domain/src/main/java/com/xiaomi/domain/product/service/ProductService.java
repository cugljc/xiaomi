package com.xiaomi.domain.product.service;

import com.xiaomi.domain.product.model.entity.ProductEntity;
import com.xiaomi.domain.product.repository.IProductRepository;
import com.xiaomi.trigger.api.IProductService;
import com.xiaomi.trigger.api.dto.ProductDTO;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ClassName: ProcutService
 * Package: com.xiaomi.domain.product.service
 */
@Service("ProductService")
@DubboService(version = "1.0")
public class ProductService implements IProductService {
    @Resource
    private IProductRepository productRepository;
    @Override

    public ProductDTO queryProductId(Long id) {
        ProductEntity productEntity=productRepository.queryProductId(id);
        return ProductDTO.builder()
                .id(productEntity.getId())
                .name(productEntity.getName())
                .stock(productEntity.getStock())
                .build();
    }
    @Override
    public int updateProduct(Long id, Integer amount) {
        return 1;
    }
    @Override
    public boolean insertProduct(ProductDTO product){
        return true;
    }
}
