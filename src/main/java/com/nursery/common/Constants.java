package com.nursery.common;

public class Constants {
    
    // ==================== HTTP 响应状态码 ====================
    public static final int CODE_SUCCESS = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_SERVER_ERROR = 500;
    
    // ==================== 业务错误码 (1xxx - 用户相关) ====================
    public static final int ERROR_USER_NOT_LOGIN = 1001;
    public static final int ERROR_USER_NOT_EXIST = 1002;
    public static final int ERROR_USER_PASSWORD_WRONG = 1003;
    public static final int ERROR_USER_PHONE_INVALID = 1004;
    public static final int ERROR_USER_TOKEN_INVALID = 1005;
    public static final int ERROR_USER_TOKEN_EXPIRED = 1006;
    
    // ==================== 业务错误码 (2xxx - 商品相关) ====================
    public static final int ERROR_PRODUCT_NOT_EXIST = 2001;
    public static final int ERROR_PRODUCT_OFF_SHELF = 2002;
    public static final int ERROR_PRODUCT_STOCK_INSUFFICIENT = 2003;
    public static final int ERROR_PRODUCT_AUDITING = 2004;
    
    // ==================== 业务错误码 (3xxx - 订单相关) ====================
    public static final int ERROR_ORDER_NOT_EXIST = 3001;
    public static final int ERROR_ORDER_STATUS_INVALID = 3002;
    public static final int ERROR_ORDER_CANNOT_CANCEL = 3003;
    public static final int ERROR_ORDER_CANNOT_PAY = 3004;
    public static final int ERROR_ORDER_CANNOT_RECEIVE = 3005;
    public static final int ERROR_ORDER_CANNOT_REVIEW = 3006;
    
    // ==================== 业务错误码 (4xxx - 购物车相关) ====================
    public static final int ERROR_CART_ITEM_NOT_EXIST = 4001;
    public static final int ERROR_CART_QUANTITY_INVALID = 4002;
    
    // ==================== 业务错误码 (5xxx - 地址相关) ====================
    public static final int ERROR_ADDRESS_NOT_EXIST = 5001;
    public static final int ERROR_ADDRESS_LIMIT_EXCEEDED = 5002;
    
    // ==================== 业务错误码 (6xxx - 收藏相关) ====================
    public static final int ERROR_FAVORITE_ALREADY_EXISTS = 6001;
    public static final int ERROR_FAVORITE_NOT_EXIST = 6002;
    
    // ==================== 业务错误码 (7xxx - 优惠券相关) ====================
    public static final int ERROR_COUPON_NOT_EXIST = 7001;
    public static final int ERROR_COUPON_EXPIRED = 7002;
    public static final int ERROR_COUPON_USED = 7003;
    public static final int ERROR_COUPON_CONDITION_NOT_MET = 7004;
    
    // ==================== 业务错误码 (8xxx - 评价相关) ====================
    public static final int ERROR_REVIEW_ALREADY_EXISTS = 8001;
    public static final int ERROR_REVIEW_NOT_ALLOWED = 8002;
    
    // ==================== 业务错误码 (9xxx - 通用业务错误) ====================
    public static final int ERROR_PARAM_INVALID = 9001;
    public static final int ERROR_OPERATION_FAILED = 9002;
    public static final int ERROR_DATA_NOT_FOUND = 9003;
    public static final int ERROR_PERMISSION_DENIED = 9004;
    
    // ==================== 订单状态 ====================
    /** 待付款 */
    public static final int ORDER_PENDING_PAY = 0;
    /** 待发货 */
    public static final int ORDER_PENDING_DELIVERY = 1;
    /** 待收货 */
    public static final int ORDER_PENDING_RECEIVE = 2;
    /** 待评价 */
    public static final int ORDER_PENDING_REVIEW = 3;
    /** 已完成 */
    public static final int ORDER_COMPLETED = 4;
    /** 已取消 */
    public static final int ORDER_CANCELLED = 5;
    /** 售后中 */
    public static final int ORDER_AFTER_SALE = 6;
    
    // ==================== 支付方式 ====================
    /** 微信支付 */
    public static final int PAY_TYPE_WECHAT = 1;
    /** 支付宝支付 */
    public static final int PAY_TYPE_ALIPAY = 2;
    /** 余额支付 */
    public static final int PAY_TYPE_BALANCE = 3;
    
    // ==================== 用户类型 ====================
    /** 普通用户 */
    public static final int USER_TYPE_NORMAL = 0;
    /** 商家用户 */
    public static final int USER_TYPE_MERCHANT = 1;
    /** 管理员 */
    public static final int USER_TYPE_ADMIN = 2;
    
    // ==================== 商品状态 ====================
    /** 已下架 */
    public static final int PRODUCT_OFF_SHELF = 0;
    /** 已上架 */
    public static final int PRODUCT_ON_SHELF = 1;
    /** 审核中 */
    public static final int PRODUCT_AUDITING = 2;
    
    // ==================== SKU 规格类型 ====================
    /** 无规格 */
    public static final int SKU_TYPE_NONE = 0;
    /** 有规格 */
    public static final int SKU_TYPE_MULTI = 1;
    
    // ==================== 地址默认标识 ====================
    /** 非默认地址 */
    public static final int ADDRESS_NOT_DEFAULT = 0;
    /** 默认地址 */
    public static final int ADDRESS_IS_DEFAULT = 1;
    
    // ==================== 收藏状态 ====================
    /** 未收藏 */
    public static final int FAVORITE_STATUS_NO = 0;
    /** 已收藏 */
    public static final int FAVORITE_STATUS_YES = 1;
    
    // ==================== 优惠券类型 ====================
    /** 满减券 */
    public static final int COUPON_TYPE_FULL_REDUCTION = 1;
    /** 折扣券 */
    public static final int COUPON_TYPE_DISCOUNT = 2;
    /** 免运费券 */
    public static final int COUPON_TYPE_FREE_SHIPPING = 3;
    
    // ==================== 优惠券状态 ====================
    /** 未使用 */
    public static final int COUPON_STATUS_UNUSED = 0;
    /** 已使用 */
    public static final int COUPON_STATUS_USED = 1;
    /** 已过期 */
    public static final int COUPON_STATUS_EXPIRED = 2;
    
    // ==================== 评价状态 ====================
    /** 待审核 */
    public static final int REVIEW_STATUS_PENDING = 0;
    /** 已通过 */
    public static final int REVIEW_STATUS_APPROVED = 1;
    /** 已拒绝 */
    public static final int REVIEW_STATUS_REJECTED = 2;
    
    // ==================== 评价匿名标识 ====================
    /** 实名评价 */
    public static final int REVIEW_ANONYMOUS_NO = 0;
    /** 匿名评价 */
    public static final int REVIEW_ANONYMOUS_YES = 1;
    
    // ==================== 购物车选中状态 ====================
    /** 未选中 */
    public static final int CART_SELECTED_NO = 0;
    /** 已选中 */
    public static final int CART_SELECTED_YES = 1;
    
    // ==================== 排序方式 ====================
    /** 综合排序 */
    public static final String SORT_BY_COMPREHENSIVE = "comprehensive";
    /** 销量排序 */
    public static final String SORT_BY_SALES = "sales";
    /** 价格升序 */
    public static final String SORT_BY_PRICE_ASC = "price_asc";
    /** 价格降序 */
    public static final String SORT_BY_PRICE_DESC = "price_desc";
    /** 最新上架 */
    public static final String SORT_BY_NEWEST = "newest";
    
    // ==================== 分页默认值 ====================
    public static final int PAGE_DEFAULT_CURRENT = 1;
    public static final int PAGE_DEFAULT_SIZE = 10;
    public static final int PAGE_MAX_SIZE = 100;
    
    // ==================== JWT Token 相关 ====================
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final long TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L; // 7天
    
    // ==================== 文件上传相关 ====================
    public static final long FILE_MAX_SIZE = 5 * 1024 * 1024L; // 5MB
    public static final String[] IMAGE_ALLOW_TYPES = {"jpg", "jpeg", "png", "gif", "webp"};
    
    // ==================== 缓存 Key 前缀 ====================
    public static final String CACHE_KEY_PRODUCT = "product:";
    public static final String CACHE_KEY_CATEGORY = "category:";
    public static final String CACHE_KEY_BANNER = "banner:";
    public static final String CACHE_KEY_USER = "user:";
}
