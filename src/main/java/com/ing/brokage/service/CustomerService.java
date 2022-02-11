package com.ing.brokage.service;

import com.ing.brokage.persistance.entity.Customer;

public interface CustomerService {

    Customer findByUsername(String username);
    Customer getCustomer(long customerId);
}
