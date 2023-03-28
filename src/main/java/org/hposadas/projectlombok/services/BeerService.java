package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.Beer;

import java.util.UUID;

public interface BeerService {

    //firmas de métodos
    Beer getBeerById(UUID id);

}
