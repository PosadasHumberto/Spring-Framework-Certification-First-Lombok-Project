package org.hposadas.projectlombok.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.services.BeerService;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@AllArgsConstructor
@Controller
@Slf4j
public class BeerController {

    //atributos
    private final BeerService beerService;

    public Beer getBeerById(UUID id) {

        log.debug("get Beer by ID - in controller");

        return beerService.getBeerById(id);
    }
}
