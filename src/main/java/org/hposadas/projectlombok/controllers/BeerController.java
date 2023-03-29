package org.hposadas.projectlombok.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.services.BeerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController     //integra las anotaciones @Controller y @ResponseBody
@Slf4j
public class BeerController {

    //atributos
    private final BeerService beerService;

    //métodos
    @RequestMapping("/api/v1/beer")
    public List<Beer> listBeers(){
        return this.beerService.listBeers();
    }

    public Beer getBeerById(UUID id) {

        log.debug("get Beer by ID - in controller");

        return beerService.getBeerById(id);
    }
}
