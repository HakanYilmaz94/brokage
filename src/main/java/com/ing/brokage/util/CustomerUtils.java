package com.ing.brokage.util;

import com.ing.brokage.exception.CustomException;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.persistance.enums.UserRoleEnum;
import lombok.experimental.UtilityClass;

import static com.ing.brokage.constant.ExceptionConstants.CUSTOMER_ID_REQUIRED_ERROR_CODE;
import static com.ing.brokage.constant.ExceptionConstants.CUSTOMER_ID_REQUIRED_ERROR_MSG;

@UtilityClass
public class CustomerUtils {

    public static Long resolveCustomerId(Customer authenticatedCustomer, Long customerId) {
        if (UserRoleEnum.ADMIN.equals(authenticatedCustomer.getRole())) {
            if (customerId == null)
                throw new CustomException(CUSTOMER_ID_REQUIRED_ERROR_MSG, CUSTOMER_ID_REQUIRED_ERROR_CODE);
            return customerId;
        }
        return authenticatedCustomer.getId();
    }
}
