package com.ing.brokage.util;

import com.ing.brokage.exception.CustomException;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.persistance.enums.UserRoleEnum;
import org.junit.jupiter.api.Test;

import static com.ing.brokage.constant.ExceptionConstants.CUSTOMER_ID_REQUIRED_ERROR_CODE;
import static com.ing.brokage.constant.ExceptionConstants.CUSTOMER_ID_REQUIRED_ERROR_MSG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerUtilsTest {

    @Test
    void resolveCustomerId_ShouldReturnGivenCustomerId_ForAdmin() {
        Customer admin = new Customer();
        admin.setRole(UserRoleEnum.ADMIN);

        Long givenCustomerId = 123L;

        Long result = CustomerUtils.resolveCustomerId(admin, givenCustomerId);

        assertEquals(givenCustomerId, result);
    }

    @Test
    void resolveCustomerId_ShouldThrowException_WhenAdminAndCustomerIdIsNull() {
        Customer admin = new Customer();
        admin.setRole(UserRoleEnum.ADMIN);

        CustomException ex = assertThrows(CustomException.class, () ->
                CustomerUtils.resolveCustomerId(admin, null)
        );

        assertEquals(CUSTOMER_ID_REQUIRED_ERROR_MSG, ex.getMessage());
        assertEquals(CUSTOMER_ID_REQUIRED_ERROR_CODE, ex.getStatusCode());
    }

    @Test
    void resolveCustomerId_ShouldReturnAuthenticatedCustomerId_ForNonAdmin() {
        Customer user = new Customer();
        user.setRole(UserRoleEnum.USER);
        user.setId(456L);

        Long result = CustomerUtils.resolveCustomerId(user, 999L);

        assertEquals(user.getId(), result);
    }
}
