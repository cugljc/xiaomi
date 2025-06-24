package com.xiaomi.domain.Order.model;

/**
 * ClassName: Order
 * Package: com.xiaomi.domain.Order.entity
 */



import com.xiaomi.domain.Order.dto.CreateRequestDTO;
import com.xiaomi.domain.Order.dto.CreateRequestItemDTO;

import com.xiaomi.types.common.OrderException;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class Order {
    private String id;
    private String userID;
    private String couponID;
    private int totalAmount;
    private int discountAmount;
    private int payAmount;
    private String status;
    private String merchantID;
    private String parentOrderID;
    private AddressVO address;

    private List<Order> subOrders;
    private List<OrderItem> items;

    public static Order create(CreateRequestDTO requestDTO, List<Sku> skuList) throws OrderException {
        Order order =  Order.builder().userID(requestDTO.getUserID())
                .couponID(requestDTO.getCouponID())
                .address(AddressVO.create(requestDTO.getAddress())).build();
        Map<String, Sku> skuMap =
                skuList.stream().collect(Collectors.toMap(Sku::getSku, sku->sku));
        order.subOrders = new ArrayList<>();
        Map<String,Order> merchantOrderMap = new HashMap<>();
        for (CreateRequestItemDTO itemDTO : requestDTO.getItems()){
            Sku sku = skuMap.get(itemDTO.getSku());
            if (sku == null){
                throw new OrderException("BE-PARAM-010","sku is not exist");
            }
            Order subOrder = merchantOrderMap.get(sku.getMerchantID());
            if (subOrder == null){
                subOrder = Order.builder().userID(requestDTO.getUserID())
                        .couponID(requestDTO.getCouponID())
                        .address(AddressVO.create(requestDTO.getAddress()))
                        .merchantID(sku.getMerchantID()).build();
                merchantOrderMap.put(sku.getMerchantID(),subOrder);
                order.subOrders.add(subOrder);
            }
            subOrder.addOrderItem(itemDTO,sku);
        }
        order.calculateTotalAmount();
        return order;
    }

    //根据订单行信息计算订单金额，优惠券金额，最终金额
    public void calculateTotalAmount(){
        int totalAmount = 0;
        int discountAmount = 0;
        int payAmount = 0;
        for (Order subOrder : subOrders) {
            subOrder.calculateSubOrderTotalAmount();
            totalAmount = totalAmount + subOrder.getTotalAmount();
            discountAmount = discountAmount + subOrder.getDiscountAmount();
            payAmount = payAmount + subOrder.getPayAmount();
        }
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.payAmount = payAmount;
    }

    private void calculateSubOrderTotalAmount(){
        int totalAmount = 0;
        int discountAmount = 0;
        int payAmount = 0;
        for (OrderItem item : this.getItems()){
            totalAmount = totalAmount + item.getTotalAmount();
            discountAmount = discountAmount + item.getDiscountAmount();
            payAmount = payAmount + item.getTotalAmount() - item.getDiscountAmount();
        }
        this.setTotalAmount(totalAmount);
        this.setDiscountAmount(discountAmount);
        this.setPayAmount(payAmount);
    }

    public void addOrderItem(CreateRequestItemDTO itemDTO,Sku sku){
        OrderItem orderItem = OrderItem.create(itemDTO,sku);
        if (this.items == null){
            this.items = new ArrayList<>();
        }
        items.add(orderItem);
    }

    public void validate() throws OrderException {
        if (userID == null || userID.isEmpty()) {
            throw new OrderException("BE-PARAM-001","userID is required");
        }
        if (items == null || items.isEmpty()) {
            throw new OrderException("BE-PARAM-002","couponID is required");
        }
        if (address == null) {
            throw new OrderException("BE-PARAM-003","address is required");
        }
        this.address.validate();
        for (OrderItem item : items) {
            item.validate();
        }
    }

    public void useCoupon(Coupon coupon) throws OrderException {
        if (coupon == null){
            return;
        }
        if (coupon.getX() > this.getTotalAmount()){
            throw new OrderException("BE-COUPON-001","coupon discount amount is greater than total amount");
        }
        this.discountAmount = coupon.getDiscountAmount(this.getTotalAmount());
        this.splitDiscountAmount();
    }

    private void splitDiscountAmount(){
        int leftAmount = this.getDiscountAmount();
        OrderItem lastItem = null;
        for (Order subOrder : subOrders) {
            for(OrderItem item : subOrder.getItems()){
                item.setDiscountAmount(this.getDiscountAmount()*item.getTotalAmount() / this.getTotalAmount());
                lastItem = item;
                leftAmount = leftAmount - item.getDiscountAmount();
            }
        }
        lastItem.setDiscountAmount(lastItem.getDiscountAmount() + leftAmount);
        this.calculateTotalAmount();
    }


}