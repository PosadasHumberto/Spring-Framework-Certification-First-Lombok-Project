package org.hposadas.projectlombok.controllers;

import org.hposadas.projectlombok.entities.Customer;
import org.hposadas.projectlombok.model.CustomerDTO;
import org.hposadas.projectlombok.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIntegrationTest {

    //atributos
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    //métodos
    @Test
    void testCustomersList(){
        List<CustomerDTO> dtos = customerController.getCustomerList();
        assertThat(dtos).isNotNull();
    }

    @Test
    @Transactional
    @Rollback
    void testEmptyList(){
        customerRepository.deleteAll();
        List<CustomerDTO> dtos = customerController.getCustomerList();
        assertThat(dtos).isEmpty();
    }

    @Test
    void testGetCustomerById(){
        Customer customer = customerRepository.findAll().get(0);
        assertThat(customerController.getCustomerById(customer.getId())).isNotNull();
    }

    @Test
    void testCustomerIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
           customerController.getCustomerById(UUID.randomUUID());
        });
    }


}