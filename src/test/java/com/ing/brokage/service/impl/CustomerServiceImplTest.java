package com.ing.brokage.service.impl;

import com.ing.brokage.constant.ExceptionConstants;
import com.ing.brokage.exception.CustomException;
import com.ing.brokage.persistance.entity.Customer;
import com.ing.brokage.persistance.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    private CustomerRepository customerRepository;
    private CustomerServiceImpl customerService;

    private final Customer customer = new Customer();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerServiceImpl(customerRepository);

        customer.setId(1L);
        customer.setUsername("testuser");
        customer.setName("Test");
        customer.setSurname("User");
    }

    @Test
    void testFindByUsername_found() {
        when(customerRepository.findByUsername("testuser")).thenReturn(customer);

        Customer result = customerService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(customerRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_notFound() {
        when(customerRepository.findByUsername("unknown")).thenReturn(null);

        Customer result = customerService.findByUsername("unknown");

        assertNull(result);
        verify(customerRepository, times(1)).findByUsername("unknown");
    }

    @Test
    void testGetCustomer_found() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomer_notFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> customerService.getCustomer(2L));

        assertEquals(ExceptionConstants.CUSTOMER_NOT_FOUND_ERROR_CODE, exception.getStatusCode());
        assertEquals(ExceptionConstants.CUSTOMER_NOT_FOUND_ERROR_MSG, exception.getMessage());
        verify(customerRepository, times(1)).findById(2L);
    }
}
