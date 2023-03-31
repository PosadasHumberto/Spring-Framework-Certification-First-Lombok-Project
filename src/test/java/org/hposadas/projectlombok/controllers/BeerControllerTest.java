package org.hposadas.projectlombok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.model.BeerStyle;
import org.hposadas.projectlombok.services.BeerService;
import org.hposadas.projectlombok.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.core.Is.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest
@WebMvcTest(BeerController.class)   //debemos especificar cual es el controlador que queremos testear. Si no lo especificamos entonces va a traer todos los controladores que tengamos.
class BeerControllerTest {

    //atributos
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean           //indica a mockito brindar un mock de BeerService al contexto Spring
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    //métodos

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beerServiceImpl.listBeers().size())));
    }

    @Test
    void getBeerById() throws Exception {

        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);

        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void handlePost() throws Exception {

        Beer beer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Test Beer")
                .beerStyle(BeerStyle.PILSNER)
                .upc("898989")
                .price(new BigDecimal(11))
                .quantityOnHand(300)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beer);

        mockMvc.perform(post("/api/v1/beer")
                .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().is(201))
                .andExpect(header().exists("Location"));

    }

    @Test
    void updateById() throws Exception {

        Beer beer = beerServiceImpl.listBeers().get(0);
        beer.setBeerName("Ultra");
        beer.setUpdateDate(LocalDateTime.now());

        mockMvc.perform(put("/api/v1/beer/" + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(beerService).ubdateBeerById(any(UUID.class),any(Beer.class));

    }
}