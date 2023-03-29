package org.hposadas.projectlombok.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.services.BeerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController     //integra las anotaciones @Controller y @ResponseBody
@Slf4j
@RequestMapping("/api/v1/beer")
public class BeerController {

    //atributos
    private final BeerService beerService;

    //métodos
    @GetMapping
    public List<Beer> listBeers(){
        return this.beerService.listBeers();
    }

    @GetMapping("/{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID id) {

        log.debug("get Beer by ID - in controller");

        return beerService.getBeerById(id);
    }

    @PostMapping
    public ResponseEntity handlePost(@RequestBody Beer beer){

        Beer savedBeer = beerService.saveNewBeer(beer);

        return new ResponseEntity(HttpStatus.CREATED);

    }
}
