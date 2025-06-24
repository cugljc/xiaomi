package com.xiaomi.domain.Order.model;

/**
 * ClassName: OrderItem
 * Package: com.xiaomi.domain.Order.model.entity
 */


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItem {
    private String orderID;
    private String merchantID;
    private String sku;
    private int quantity;
    private int price;
    private int totalAmount;
    private int discountAmount;
    private int payAmount;
    private String status;
    private String couponID;

    public static OrderItem create(CreateRequestItemDTO orderItemDTO,Sku sku) {
        OrderItem item =  OrderItem.builder()
                .sku(orderItemDTO.getSku())
                .quantity(orderItemDTO.getQuantity())
                .price(sku.getPrice())
                .build();
        item.calculateAmount();
        return item;
    }

    public void calculateAmount(){
        this.totalAmount = this.quantity * this.price;
        this.payAmount = this.totalAmount - this.discountAmount;
    }

    public void setDiscountAmount(int discountAmount){
        this.discountAmount = discountAmount;
        this.calculateAmount();
    }


    public void validate(){
        if (sku == null || sku.isEmpty()) {
            throw new OrderException("BE-PARAM-003","sku is required");
        }
        if (quantity <= 0) {
            throw new OrderException("BE-PARAM-009","quantity is required");
        }
    }

}
