package org.hposadas.projectlombok.repositories;

import org.hposadas.projectlombok.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
