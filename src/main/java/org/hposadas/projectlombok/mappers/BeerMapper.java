package org.hposadas.projectlombok.mappers;

import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.entities.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO dto);

    BeerDTO beerToBeerDto(Beer beer);
}
