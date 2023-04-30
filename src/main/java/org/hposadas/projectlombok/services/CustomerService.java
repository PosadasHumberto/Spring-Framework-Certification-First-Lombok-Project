package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.entities.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    //firmas de métodos
    public List<CustomerDTO> getCustomers();
    public Optional<CustomerDTO> getCustomerById(UUID id);
    public CustomerDTO saveNewCustomer(CustomerDTO customer);
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customer);
    public Boolean deleteCustomerById(UUID id);
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer);
}
