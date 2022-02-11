package com.ing.brokage.persistance.repository;

import com.ing.brokage.persistance.entity.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    Customer findByUsername(String username);

}
