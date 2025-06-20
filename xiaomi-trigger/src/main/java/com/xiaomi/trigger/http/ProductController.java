package com.xiaomi.trigger.http;

import com.alibaba.fastjson.JSON;
import com.xiaomi.domain.product.service.ProductService;
import com.xiaomi.infrastructure.persistent.po.Product;
import com.xiaomi.infrastructure.persistent.repository.ProductRepository;
import com.xiaomi.trigger.api.dto.ProductDTO;
import com.xiaomi.types.enums.ResponseCode;
import com.xiaomi.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: RaffleController
 * Package: com.xiaomi.trigger.http
 */
@Slf4j
@RestController()
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("select")
    public Response<ProductDTO> getById(@RequestParam Long id) {
        try {
            log.info("查询id开始 id：{}", id);
            ProductDTO productDTO = productService.queryProductId(id);
            Response<ProductDTO> response = Response.<ProductDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(productDTO)
                    .build();
            log.info("查询id完成 id：{} response: {}", id, JSON.toJSONString(response));
            return response;
        }
        catch (Exception e) {
            log.error("查询id失败 id：{}", id, e);
            return Response.<ProductDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Response<Boolean> insertProduct(@RequestBody ProductDTO productDTO) {
        try {
            log.info("插入员工开始 → product={}", JSON.toJSONString(productDTO));
            Boolean status=productService.insertProduct(productDTO);
            log.info("插入员工完成 → productId={}", productDTO.getId());
            return Response.<Boolean>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(status)
                    .build();
        } catch (Exception e) {
            log.error("插入员工失败（系统异常） → Product={}",
                    JSON.toJSONString(productDTO), e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @PostMapping("/update")
    public Response<Boolean> updateProductStock(
            @RequestParam Long id,
            @RequestParam Integer amount) {
        try {
            log.info("请求更新产品库存 → id={}, amount={}", id, amount);
            int affectedRows = productService.updateProduct(id, amount);
            if (affectedRows > 0) {
                log.info("产品库存更新成功 → id={}", id);
                return Response.<Boolean>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(true)
                        .build();
            } else {
                log.warn("产品库存更新失败（可能库存不足） → id={}", id);
                return Response.<Boolean>builder()
                        .code(ResponseCode.UN_ERROR.getCode())
                        .info("库存不足或商品不存在")
                        .data(false)
                        .build();
            }

        } catch (Exception e) {
            log.error("更新产品库存异常 → id={}", id, e);
            return Response.<Boolean>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info("系统繁忙，请重试")
                    .build();
        }
    }

}
