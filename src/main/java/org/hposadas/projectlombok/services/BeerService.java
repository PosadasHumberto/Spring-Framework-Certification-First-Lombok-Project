package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.BeerDTO;
import org.hposadas.projectlombok.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    //firmas de métodos
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> ubdateBeerById(UUID id, BeerDTO beer);
    Boolean deleteById(UUID id);
    Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beer);
}
