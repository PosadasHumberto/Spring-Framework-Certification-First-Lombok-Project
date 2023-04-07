package org.hposadas.projectlombok.services;

import lombok.extern.slf4j.Slf4j;
import org.hposadas.projectlombok.model.BeerDTO;
import org.hposadas.projectlombok.model.BeerStyle;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    //atributos
    private Map<UUID, BeerDTO> beerMap;

    //constructor
    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
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

        BeerDTO beer2 = BeerDTO.builder()
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

        BeerDTO beer3 = BeerDTO.builder()
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
    public List<BeerDTO> listBeers() {
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

      log.debug("Get Beer by Id - in service. Id: " + id.toString());

        return Optional.of(beerMap.get(id)); //devuelve el valor al cual esta asociada la llave id que recibe el metodo como argumento. Si no exuste devolvera null
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        BeerDTO tempBeer = BeerDTO.builder()
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
    public Optional<BeerDTO> ubdateBeerById(UUID id, BeerDTO beer) {

        BeerDTO foundBeer = this.beerMap.get(id);
        foundBeer.setBeerName(beer.getBeerName());
        foundBeer.setBeerStyle(beer.getBeerStyle());
        foundBeer.setUpc(beer.getUpc());
        foundBeer.setPrice(beer.getPrice());
        foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
        foundBeer.setUpdateDate(LocalDateTime.now());

        this.beerMap.put(id, foundBeer);
        return Optional.of(foundBeer);


    }

    @Override
    public Boolean deleteById(UUID id) {

        this.beerMap.remove(id);
        return true;
    }

    @Override
    public void patchBeerById(UUID id, BeerDTO beer) {

        BeerDTO existing = this.beerMap.get(id);
        if (StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }

        if (beer.getBeerStyle() != null) {
            existing.setBeerStyle(beer.getBeerStyle());
        }

        if (beer.getPrice() != null) {
            existing.setPrice(beer.getPrice());
        }

        if (beer.getQuantityOnHand() != null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }

        if (StringUtils.hasText(beer.getUpc())) {
            existing.setUpc(beer.getUpc());
        }

        this.beerMap.put(id, existing);
    }
}
