package org.hposadas.projectlombok.mappers;

import org.hposadas.projectlombok.entities.Customer;
import org.hposadas.projectlombok.entities.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDTO dto);
    CustomerDTO customerToCustomerDto(Customer customer);
}
