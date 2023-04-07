package org.hposadas.projectlombok.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.mappers.BeerMapper;
import org.hposadas.projectlombok.model.BeerDTO;
import org.hposadas.projectlombok.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService{

    //atributos
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    //métodos
    @Override
    public List<BeerDTO> listBeers() {
        /**
         * El método BeerRepository.findall() devuelve un List<Beer> pero como
         * debemos devolver una List<BeerDTO> entonces convertimos esta lista a un
         * Stream y mapeamos el elemento Beer en curso usando el método
         * BeerMapper.beerToBeerDTO. Despues reduce el flujo y lo deja en forma de
         * lista por lo que ahora tendremos una List<BeerDTO< que ha sido devuelta.
         */
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        return Optional.ofNullable(
                beerMapper.beerToBeerDto(beerRepository.findById(id)
                        .orElse(null))
        );
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
    }

    @Override
    public Optional<BeerDTO> ubdateBeerById(UUID id, BeerDTO beer) {
        /*beerRepository.findById(id).ifPresent(foundBeer -> {
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setUpc(beer.getUpc());

            beerRepository.save(foundBeer);
        });*/

        if(beerRepository.findById(id).isPresent()){
            Beer foundBeer = beerRepository.findById(id).get();
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setPrice(beer.getPrice());
            foundBeer.setUpc(beer.getUpc());

            beerRepository.save(foundBeer);
             return Optional.of(beerMapper.beerToBeerDto(foundBeer));
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(UUID id) {

    }

    @Override
    public void patchBeerById(UUID id, BeerDTO beer) {

    }
}
