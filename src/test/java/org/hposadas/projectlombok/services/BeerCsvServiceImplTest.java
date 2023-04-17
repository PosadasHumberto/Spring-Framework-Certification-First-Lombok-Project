package org.hposadas.projectlombok.services;

import org.hposadas.projectlombok.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerCsvServiceImplTest {

    //atributos
    @Autowired
    BeerCsvService beerCsvService;

    //métodos
    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> lista = beerCsvService.convertCSV(file);

        System.out.println(lista.size());

        assertThat(lista.size()).isGreaterThan(0);
    }

}