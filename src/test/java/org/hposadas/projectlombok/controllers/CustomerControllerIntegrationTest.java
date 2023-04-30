package org.hposadas.projectlombok.controllers;

import org.hposadas.projectlombok.entities.Customer;
import org.hposadas.projectlombok.mappers.CustomerMapper;
import org.hposadas.projectlombok.entities.model.CustomerDTO;
import org.hposadas.projectlombok.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Autowired
    CustomerMapper customerMapper;

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

    @Test
    @Transactional
    @Rollback
    void saveNewCustomer(){
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("New Customer")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        ResponseEntity response = customerController.handlerPost(customerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = response.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        assertThat(customerRepository.findById(savedUUID).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void updateExistingCustomerTest(){
        Customer customer = customerRepository.findAll().get(0);
        CustomerDTO customerDTO = customerMapper.customerToCustomerDto(customer);
        customerDTO.setName("Modified Customer Name");
        customerDTO.setLastModifiedDate(LocalDateTime.now());
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        ResponseEntity response = customerController.updateCustomerById(
                customer.getId(), customerDTO
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(
                customerRepository.findById(customer.getId()).get().getName()
        ).isEqualTo(customerDTO.getName());
    }

    @Test
    @Transactional
    @Rollback
    void updateNonExistingBeerTest() {
        assertThrows(
                NotFoundException.class,
                () -> {
                    customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
                }
                );
    }

    @Test
    @Transactional
    @Rollback
    void deleteByIdFound(){
        Customer customer = customerRepository.findAll().get(0);
        ResponseEntity response = customerController.deleteCustomerByid(customer.getId());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customer).isNotNull();
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void deleteByIdNotFound(){
        assertThrows(NotFoundException.class, ()->{
            customerController.deleteCustomerByid(UUID.randomUUID());
        });
    }

}