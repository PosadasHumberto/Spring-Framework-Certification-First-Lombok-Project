package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    //firmas de métodos
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beer);

    Optional<BeerDTO> ubdateBeerById(UUID id, BeerDTO beer);
    void deleteById(UUID id);
    void patchBeerById(UUID id, BeerDTO beer);
}
