package org.hposadas.projectlombok.bootstrap;

import lombok.RequiredArgsConstructor;
import org.hposadas.projectlombok.entities.Beer;
import org.hposadas.projectlombok.entities.Customer;
import org.hposadas.projectlombok.model.BeerStyle;
import org.hposadas.projectlombok.repositories.BeerRepository;
import org.hposadas.projectlombok.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    //métodos
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
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
                    .customerName("Jaime Duende")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer2 = Customer.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .customerName("Leticia Aguila")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            Customer customer3 = Customer.builder()
                    .id(UUID.randomUUID())
                    .version(1)
                    .customerName("Carolina Silis")
                    .createdDate(LocalDateTime.now())
                    .lastModifiedDate(LocalDateTime.now())
                    .build();

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);
        }
    }
}
