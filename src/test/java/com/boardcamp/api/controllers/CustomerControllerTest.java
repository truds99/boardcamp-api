package com.boardcamp.api.controllers;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("João Alfredo");
        customer.setCpf("01234567890");
        customer.setPhone("21998899222");
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customer);

        String requestBody = objectMapper.writeValueAsString(customer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.cpf").value(customer.getCpf()))
                .andExpect(jsonPath("$.phone").value(customer.getPhone()));
    }

    @Test
    void shouldGetAllCustomers() throws Exception {
        when(customerService.listCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(customer.getId()))
                .andExpect(jsonPath("$[0].name").value(customer.getName()))
                .andExpect(jsonPath("$[0].cpf").value(customer.getCpf()))
                .andExpect(jsonPath("$[0].phone").value(customer.getPhone()));
    }

    @Test
    void shouldReturn404IfCustomerNotFound() throws Exception {
        when(customerService.getCustomer(99L)).thenThrow(new RuntimeException("Customer not found"));

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());
    }
}
