package org.hposadas.projectlombok.repositories;

import jakarta.validation.ConstraintViolationException;
import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class BeerRepositoryTest {

    //atributos
    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer(){
        Beer savedBeer = beerRepository
                .save(Beer.builder()
                        .beerName("Tecate Ligth")
                        .upc("ohrnrsv5f")
                        .price(BigDecimal.valueOf(14.99))
                        .beerStyle(BeerStyle.WHEAT)
                        .build());

        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerTooLong(){
       assertThrows(ConstraintViolationException.class, ()->{
           Beer savedBeer = beerRepository
                   .save(Beer.builder()
                           .beerName("elukyfqgheùômifhgbrs!imeojfhbvezkhjlkfejoi!fluhpfi!elgz:i!mfdhgyekhfjhifugoezljfhpiel")
                           .upc("ohrnrsv5f")
                           .price(BigDecimal.valueOf(14.99))
                           .beerStyle(BeerStyle.WHEAT)
                           .build());

           beerRepository.flush();
           assertThat(savedBeer).isNotNull();
           assertThat(savedBeer.getId()).isNotNull();
       });


    }
}