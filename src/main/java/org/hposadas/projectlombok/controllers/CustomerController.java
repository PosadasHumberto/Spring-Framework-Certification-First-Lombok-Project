package org.hposadas.projectlombok.controllers;


import lombok.RequiredArgsConstructor;
import org.hposadas.projectlombok.model.CustomerDTO;
import org.hposadas.projectlombok.services.CustomerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor        //to generate constructor injecting service
@RequestMapping("/api/customer/v1")
public class CustomerController {

    //atributos
    private final CustomerService customerservice;

    //métodos
    @GetMapping()
    public List<CustomerDTO> getCustomerList(){

        return this.customerservice.getCustomers();
    }

    @GetMapping("/{customerId}")
    public CustomerDTO getCustomerById(@PathVariable("customerId")UUID id){

        return this.customerservice.getCustomerById(id).orElseThrow(NotFoundException::new);

    }

    @PostMapping
    public ResponseEntity handlerPost (@RequestBody CustomerDTO customer) {

        CustomerDTO savedCustomer = this.customerservice.saveNewCustomer(customer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/customer/v1/" + savedCustomer.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer){

        if (this.customerservice.updateCustomerById(id, customer).isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new NotFoundException();
        }

    }


    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomerByid(@PathVariable("customerId") UUID id){

        if (this.customerservice.deleteCustomerById(id)){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else {
            throw new NotFoundException();
        }

    }

}
