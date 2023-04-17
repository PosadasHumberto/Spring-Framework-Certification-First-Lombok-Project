package org.hposadas.projectlombok.services;

import lombok.RequiredArgsConstructor;
import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.mappers.BeerMapper;
import org.hposadas.projectlombok.model.BeerDTO;
import org.hposadas.projectlombok.repositories.BeerRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
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
    public Boolean deleteById(UUID id) {

        if (beerRepository.existsById(id)){
            beerRepository.deleteById(id);
            return true;
        }
            return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())){
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null){
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())){
                foundBeer.setUpc(beer.getUpc());
            }
            if (beer.getPrice() != null){
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null){
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(beerMapper
                    .beerToBeerDto(beerRepository.save(foundBeer))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }
}
