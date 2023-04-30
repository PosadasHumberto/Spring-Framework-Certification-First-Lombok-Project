package org.hposadas.projectlombok.bootstrap;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.entities.Customer;
import org.hposadas.projectlombok.entities.model.BeerCSVRecord;
import org.hposadas.projectlombok.entities.model.BeerStyle;
import org.hposadas.projectlombok.repositories.BeerRepository;
import org.hposadas.projectlombok.repositories.CustomerRepository;
import org.hposadas.projectlombok.services.BeerCsvService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/*
 * Esta clase implementa la interfaz CommandLineRunner de Spring.
 * CommandLineRunner es una interfaz Spring Boot simple con un método run().
 * Spring Boot llamará automáticamente al método run() de todos los
 * beans que implementan esta interfaz después de que se haya cargado el
 * contexto de la aplicación.
 * */
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    //atributos
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;

    //métodos
    @Override
    @Transactional
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
        loadCsvData();
    }

    private void loadBeerData(){

        if (beerRepository.count() == 0){

            Beer beer1 = Beer.builder()
                    .beerName("Victoria")
                    .beerStyle(BeerStyle.WHEAT)
                    .upc("161817")
                    .price(BigDecimal.valueOf(15.35))
                    .quantityOnHand(250)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer2 = Beer.builder()
                    .beerName("Estrella")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("989794")
                    .price(BigDecimal.valueOf(19.99))
                    .quantityOnHand(330)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            Beer beer3 = Beer.builder()
                    .beerName("Bud Ligth")
                    .beerStyle(BeerStyle.PILSNER)
                    .upc("868676")
                    .price(BigDecimal.valueOf(12.50))
                    .quantityOnHand(250)
                    .createdDate(LocalDateTime.now())
                    .updateDate(LocalDateTime.now())
                    .build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }

    }

    private void loadCustomerData(){

        if (customerRepository.count() == 0){


            Customer customer1 = Customer.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .name("Jaime Duende")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .name("Leticia Aguila")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .name("Carolina Silis")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);

        }
    }
    private void loadCsvData() throws FileNotFoundException {
        if (beerRepository.count() < 10) {
            File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
            List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

            //mapeando el BeerStyle de cada Beer
            recs.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                //guardando el objeto Beer en curso de iteracion en la BDD
                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(),50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });

        }
    }
}
