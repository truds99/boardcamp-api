package com.boardcamp.api.services;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void shouldThrowIfCpfAlreadyExists() {
        Customer existing = new Customer();
        existing.setCpf("12345678901");

        when(customerRepository.findByCpf("12345678901")).thenReturn(Optional.of(existing));

        Customer customer = new Customer();
        customer.setName("Maria");
        customer.setCpf("12345678901");
        customer.setPhone("21999999999");

        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(customer));
    }

    @Test
    void shouldCreateCustomerIfDataIsValid() {
        Customer customer = new Customer();
        customer.setName("João");
        customer.setCpf("98765432100");
        customer.setPhone("21988888888");

        when(customerRepository.findByCpf("98765432100")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Customer created = customerService.createCustomer(customer);

        assertEquals("João", created.getName());
        assertEquals("98765432100", created.getCpf());
        assertEquals("21988888888", created.getPhone());
    }

    @Test
    void shouldThrowIfNameIsEmpty() {
        Customer customer = new Customer();
        customer.setName("");
        customer.setCpf("12312312312");
        customer.setPhone("21999999999");

        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(customer));
    }

}
