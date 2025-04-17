package com.boardcamp.api.integration;

import com.boardcamp.api.models.Customer;
import com.boardcamp.api.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void cleanDatabase() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        String json = """
            {
              "name": "Maria",
              "phone": "21999999999",
              "cpf": "12345678901"
            }
        """;

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Maria"));
    }

    @Test
    void shouldReturnCustomerById() throws Exception {
        Customer customer = new Customer();
        customer.setName("Joana");
        customer.setPhone("21999999999");
        customer.setCpf("98765432100");
        customer = customerRepository.save(customer);

        mockMvc.perform(get("/customers/" + customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Joana"));
    }

    @Test
    void shouldReturn404IfCustomerDoesNotExist() throws Exception {
        mockMvc.perform(get("/customers/999"))
                .andExpect(status().isNotFound());
    }
}
