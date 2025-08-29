package com.ing.brokage.constant;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionConstants {

    public static final int INSUFFICIENT_STOCK_SIZE_CODE = 501;
    public static final String INSUFFICIENT_STOCK_SIZE_MSG = "Insufficient stock size";

    public static final int INSUFFICIENT_TRY_BALANCE_CODE = 502;
    public static final String INSUFFICIENT_TRY_BALANCE_MSG = "Insufficient TRY balance";

    public static final int CUSTOMER_NOT_FOUND_ERROR_CODE = 503;
    public static final String CUSTOMER_NOT_FOUND_ERROR_MSG = "Customer not found.";

    public static final int ORDER_NOT_FOUND_ERROR_CODE = 504;
    public static final String ORDER_NOT_FOUND_ERROR_MSG = "Order not found.";

    public static final int ONLY_PENDING_ORDERS_CANCELED_CODE = 505;
    public static final String ONLY_PENDING_ORDERS_CANCELED_MSG = "Only pending orders can be canceled";

    public static final int ONLY_PENDING_ORDERS_MATCHED_CODE = 506;
    public static final String ONLY_PENDING_ORDERS_MATCHED_MSG = "Only pending orders can be matched";

    public static final int ASSET_NOT_FOUND_ERROR_CODE = 507;
    public static final String ASSET_NOT_FOUND_ERROR_MSG = "Asset not found.";

    public static final int CUSTOMER_ID_REQUIRED_ERROR_CODE = 508;
    public static final String CUSTOMER_ID_REQUIRED_ERROR_MSG = "Customer id required.";
}
