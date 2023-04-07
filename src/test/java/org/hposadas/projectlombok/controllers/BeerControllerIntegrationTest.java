package org.hposadas.projectlombok.controllers;

import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.mappers.BeerMapper;
import org.hposadas.projectlombok.model.BeerDTO;
import org.hposadas.projectlombok.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest     //Para el Integration Test necesitamos todo el contexto de SpringBootTest
class BeerControllerIntegrationTest {

    //atributos
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    //métodos
    @Test
    void testListBeers(){
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Test
    @Rollback
    @Transactional
    void testEmptyList(){
        beerRepository.deleteAll();

        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos).isEmpty();
    }

    @Test
    void testGetById(){
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO dto = beerController.getBeerById(beer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testBeerIdNotFound(){
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    @Transactional
    @Rollback
    void saveNewBeerTest(){
        BeerDTO beer = BeerDTO.builder()
                .beerName("BeerTest")
                .build();

        ResponseEntity response = beerController.handlePost(beer);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = response.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beerTemp = beerRepository.findById(savedUUID).get();
        assertThat(beerTemp).isNotNull();

    }

    @Test
    @Transactional
    @Rollback
    void updateExistingBeerTest() {
        Beer beerToUpdate = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beerToUpdate);
        beerDTO.setBeerName("Modified Beer Name");
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        ResponseEntity response = beerController.updateById(
                beerToUpdate.getId(), beerDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        Beer beerToCompare = beerRepository.findById(beerToUpdate.getId()).get();
        assertThat(beerToCompare.getBeerName()).isEqualTo(beerDTO.getBeerName());
    }

    @Test
    @Transactional
    @Rollback
    void updateNonExistingBeerTest() {
        assertThrows(
                NotFoundException.class,
                () -> {
                    beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
                }
                );
    }

    @Test
    @Transactional
    @Rollback
    void deleteByIdFound() {

        Beer beer = beerRepository.findAll().get(0);

        ResponseEntity response = beerController.deleteById(beer.getId());

        assertThat(beer).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    void deleteNonExistingBeer() {
        assertThrows(NotFoundException.class, () -> beerController.deleteById(UUID.randomUUID()));
    }
}