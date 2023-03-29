package org.hposadas.projectlombok.services;

import lombok.extern.slf4j.Slf4j;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.model.BeerStyle;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    //atributos
    private Map<UUID, Beer> beerMap;

    //constructor
    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Corona")
                .beerStyle(BeerStyle.LAGER)
                .upc("123456")
                .price(new BigDecimal(12.99))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Indio")
                .beerStyle(BeerStyle.WHEAT)
                .upc("457835")
                .price(new BigDecimal(10.99))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Pacifico")
                .beerStyle(BeerStyle.PILSNER)
                .upc("784125")
                .price(new BigDecimal(15))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.beerMap.put(beer1.getId(), beer1);
        this.beerMap.put(beer2.getId(), beer2);
        this.beerMap.put(beer3.getId(), beer3);
    }

    //métodos
    @Override
    public List<Beer> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {

      log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return beerMap.get(id); //devuelve el valor al cual esta asociada la llave id que recibe el metodo como argumento. Si no exuste devolvera null
    }

    @Override
    public Beer saveNewBeer(Beer beer) {

        Beer tempBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(beer.getVersion())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        this.beerMap.put(tempBeer.getId(), tempBeer);

        return tempBeer;

    }

    @Override
    public void ubdateBeerById(UUID id, Beer beer) {

        //Beer foundBeer = this.beerMap.get(id);
        Beer foundBeer = this.getBeerById(UUID.fromString(id.toString()));
        foundBeer.setBeerName(beer.getBeerName() != null? beer.getBeerName() : foundBeer.getBeerName());
        foundBeer.setVersion(beer.getVersion() != null? beer.getVersion() : foundBeer.getVersion());
        foundBeer.setBeerStyle(beer.getBeerStyle() != null? beer.getBeerStyle() : foundBeer.getBeerStyle());
        foundBeer.setUpc(beer.getUpc() != null? beer.getUpc() : foundBeer.getUpc());
        foundBeer.setPrice(beer.getPrice() != null? beer.getPrice() : foundBeer.getPrice());
        foundBeer.setQuantityOnHand(beer.getQuantityOnHand() != null? beer.getQuantityOnHand() : foundBeer.getQuantityOnHand());
        foundBeer.setUpdateDate(LocalDateTime.now());

        this.beerMap.put(id, foundBeer);


    }
}
