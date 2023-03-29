package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    //firmas de métodos
    List<Beer> listBeers();
    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);
}
