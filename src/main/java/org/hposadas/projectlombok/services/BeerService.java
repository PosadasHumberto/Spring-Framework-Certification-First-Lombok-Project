package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    //firmas de métodos
    List<Beer> listBeers();
    Optional<Beer> getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void ubdateBeerById(UUID id, Beer beer);
    void deleteById(UUID id);
    void patchBeerById(UUID id, Beer beer);
}
