package org.hposadas.projectlombok.repositories;

import org.hposadas.projectlombok.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
