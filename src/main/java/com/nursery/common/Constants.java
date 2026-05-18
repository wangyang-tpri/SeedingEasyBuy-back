package com.nursery.common;

public class Constants {
    // 订单状态
    public static final int ORDER_PENDING_PAY = 0;
    public static final int ORDER_PENDING_DELIVERY = 1;
    public static final int ORDER_PENDING_RECEIVE = 2;
    public static final int ORDER_PENDING_REVIEW = 3;
    public static final int ORDER_COMPLETED = 4;
    public static final int ORDER_CANCELLED = 5;
    public static final int ORDER_AFTER_SALE = 6;

    // 用户类型
    public static final int USER_TYPE_NORMAL = 0;
    public static final int USER_TYPE_MERCHANT = 1;
    public static final int USER_TYPE_ADMIN = 2;

    // 商品状态
    public static final int PRODUCT_OFF_SHELF = 0;
    public static final int PRODUCT_ON_SHELF = 1;
    public static final int PRODUCT_AUDITING = 2;
}
