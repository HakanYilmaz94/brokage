package com.ing.brokage.service.impl;

import com.ing.brokage.constant.ExceptionConstants;
import com.ing.brokage.exception.CustomException;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.persistance.repository.CustomerRepository;
import com.ing.brokage.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public Customer getCustomer(long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(ExceptionConstants.CUSTOMER_NOT_FOUND_ERROR_MSG, ExceptionConstants.CUSTOMER_NOT_FOUND_ERROR_CODE));
    }

}
