package org.hposadas.projectlombok.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.projectlombok.entities.Customer;
import org.hposadas.projectlombok.mappers.CustomerMapper;
import org.hposadas.projectlombok.model.CustomerDTO;
import org.hposadas.projectlombok.repositories.CustomerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
        return customerMapper.customerToCustomerDto(customerRepository.save(customerMapper.customerDtoToCustomer(customer)));
    }

    @Override
    public Optional<CustomerDTO> updateCustomerById(UUID id, CustomerDTO customer) {
        if (customerRepository.existsById(id)){
            Customer customerRecovered = this.customerRepository.findById(id).get();
            customerRecovered.setCustomerName("Modified Customer Name");
            customerRecovered.setLastModifiedDate(LocalDateTime.now());

            customerRepository.save(customerRecovered);
            return Optional.of(customerMapper.customerToCustomerDto(customerRecovered));
        }
        return Optional.empty();
    }

    @Override
    public Boolean deleteCustomerById(UUID id) {
        if(customerRepository.existsById(id)){
            customerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchCustomerById(UUID customerId, CustomerDTO customer) {
        AtomicReference<Optional<CustomerDTO>> atomicReference = new AtomicReference<>();

        customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            if (StringUtils.hasText(customer.getCustomerName())){
                foundCustomer.setCustomerName(customer.getCustomerName());
            }
            atomicReference.set(Optional.of(customerMapper
                    .customerToCustomerDto(customerRepository.save(foundCustomer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
