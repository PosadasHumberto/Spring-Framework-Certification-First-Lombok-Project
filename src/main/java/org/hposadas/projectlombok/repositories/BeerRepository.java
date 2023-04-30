package org.hposadas.projectlombok.repositories;

import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.entities.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

    //adding personalized queries
    Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, org.springframework.data.domain.Pageable pageable);
    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);
    Page<Beer> findAllByBeerNameIsLikeIgnoreCaseAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable);
}
