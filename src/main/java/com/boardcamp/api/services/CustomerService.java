package com.boardcamp.api.services;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> listCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public Customer createCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (customer.getCpf() == null || !customer.getCpf().matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF must be 11 numeric characters.");
        }

        if (customer.getPhone() == null || !customer.getPhone().matches("\\d{10,11}")) {
            throw new IllegalArgumentException("Phone must be 10 or 11 numeric characters.");
        }

        if (customerRepository.findByCpf(customer.getCpf()).isPresent()) {
            throw new RuntimeException("CPF already exists.");
        }

        return customerRepository.save(customer);
    }
}
