package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.CustomerDTO;

import java.util.List;
import java.util.UUID;

public interface Customerservice {

    //firmas de métodos
    public List<CustomerDTO> getCustomers();
    public CustomerDTO getCustomerById(UUID id);
    public CustomerDTO saveNewCustomer(CustomerDTO customer);
    public void updateCustomerById(UUID id, CustomerDTO customer);
    public void deleteCustomerById(UUID id);
}
