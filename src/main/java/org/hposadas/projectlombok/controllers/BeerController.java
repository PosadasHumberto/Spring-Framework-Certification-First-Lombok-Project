package org.hposadas.projectlombok.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.services.BeerService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
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

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateById(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {

        this.beerService.ubdateBeerById(id, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{beerId}")
    public ResponseEntity patchBeerById(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {

        this.beerService.patchBeerById(id, beer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{beerId}")
    public ResponseEntity deleteById(@PathVariable("beerId") UUID id){

        this.beerService.deleteById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
