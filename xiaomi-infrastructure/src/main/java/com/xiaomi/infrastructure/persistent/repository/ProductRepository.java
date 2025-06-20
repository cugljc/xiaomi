package com.xiaomi.infrastructure.persistent.repository;

import com.xiaomi.domain.product.model.entity.ProductEntity;
import com.xiaomi.domain.product.repository.IProductRepository;
import com.xiaomi.infrastructure.persistent.dao.ProductDao;
import com.xiaomi.infrastructure.persistent.po.Employee;
import com.xiaomi.infrastructure.persistent.po.Product;
import com.xiaomi.infrastructure.persistent.redis.IRedisService;
import org.redisson.api.RLock;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import com.xiaomi.types.common.Constants;

/**
 * ClassName: EmployeeResipostory
 * Package: com.xiaomi.infrastructure.persistent.repository
 */
@Slf4j
@Repository
public class ProductRepository  implements IProductRepository {
    @Resource
    private IRedisService redisService;
    @Resource
    private ProductDao productDao;

    @Override
    public ProductEntity queryProductId(Long id){
        String cacheKey = Constants.RedisKey.PRODUCT_KEY+id.toString();
        ProductEntity productEntity = redisService.getValue(cacheKey);
        if (null != productEntity) {
            return productEntity;
        }
        Product product = productDao.selectById(id);
        productEntity = ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
        if (product == null) {
            redisService.setValue(cacheKey, new Employee());//缓存空对象
            return null;
        }
        redisService.setValue(cacheKey, product);
        return productEntity;
    }
    @Override
    public boolean insertProduct(ProductEntity productEntity) {
        productDao.insert(Product.builder()
                .name(productEntity.getName())
                .stock(productEntity.getStock())
                .build());
        return true;
    }
    @Override
    public int updateProduct(Long id, Integer amount) {
        String cacheKey = Constants.RedisKey.PRODUCT_KEY + id.toString();
        String lockid = Constants.RedisKey.PRODUCT_UPDATE_KEY + id.toString();
        RLock lock = redisService.getLock(lockid);
        boolean acquired = false;
        try {
            int maxRetry = 3;
            long waitTime = 1000;
            for (int i = 0; i < maxRetry; i++) {
                acquired = lock.tryLock(3, 10, TimeUnit.SECONDS);
                if (acquired) break;
                Thread.sleep(waitTime);
            }
            if (!acquired) {
                return 0;
            }
            int rows = productDao.decreaseStock(id, amount);
            if (rows > 0) {
                redisService.remove(cacheKey);
            }
            return rows;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0;
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }


    }
}
