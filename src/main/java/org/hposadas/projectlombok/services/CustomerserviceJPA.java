package org.hposadas.projectlombok.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.projectlombok.mappers.CustomerMapper;
import org.hposadas.projectlombok.model.CustomerDTO;
import org.hposadas.projectlombok.repositories.CustomerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerserviceJPA implements Customerservice {

    //atributos
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    //métodos
    @Override
    public List<CustomerDTO> getCustomers() {
        /**
         * El método CustomererRepository.findall() devuelve un List<Customer> pero como
         * debemos devolver una List<CustomerDTO> entonces convertimos esta lista a un
         * Stream y mapeamos el elemento Customer en curso usando el método
         * CustomerMapper.customerToCustomerDTO. Despues reduce el flujo y lo deja en forma de
         * lista por lo que ahora tendremos una List<CustomerDTO< que ha sido devuelta.
         */
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(
                customerMapper.customerToCustomerDto(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        return null;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDTO customer) {

    }

    @Override
    public void deleteCustomerById(UUID id) {

    }
}
