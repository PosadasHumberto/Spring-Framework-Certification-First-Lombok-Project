package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.CustomerDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerserviceImpl implements Customerservice {

    //atributos
    private Map<UUID, CustomerDTO> customerMap;

    //constructor

    public CustomerserviceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Humberto Posadas")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Jaimes Hernandez")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("Helena Cruz")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        this.customerMap.put(customer1.getId(), customer1);
        this.customerMap.put(customer2.getId(), customer2);
        this.customerMap.put(customer3.getId(), customer3);
    }

    //métodos
    @Override
    public List<CustomerDTO> getCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public CustomerDTO getCustomerById(UUID id) {
        return this.customerMap.get(id);
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {

        CustomerDTO tempCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        this.customerMap.put(tempCustomer.getId(), tempCustomer);
        return tempCustomer;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDTO customer) {

        CustomerDTO tempCustomer = this.getCustomerById(UUID.fromString(id.toString()));
        tempCustomer.setCustomerName(customer.getCustomerName() != null? customer.getCustomerName() : tempCustomer.getCustomerName());
        tempCustomer.setVersion(customer.getVersion() != null? customer.getVersion() : tempCustomer.getVersion());
        tempCustomer.setLastModifiedDate(LocalDateTime.now());

        this.customerMap.put(id, tempCustomer);

    }

    @Override
    public void deleteCustomerById(UUID id) {
        this.customerMap.remove(id);
    }
}
