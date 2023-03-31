package org.hposadas.projectlombok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hposadas.projectlombok.model.Beer;
import org.hposadas.projectlombok.model.BeerStyle;
import org.hposadas.projectlombok.services.BeerService;
import org.hposadas.projectlombok.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest
@WebMvcTest(BeerController.class)   //debemos especificar cual es el controlador que queremos testear. Si no lo especificamos entonces va a traer todos los controladores que tengamos.
class BeerControllerTest {

    //atributos
    public final static String BEER_PATH = "/api/v1/beer";
    public final static String BEER_PATH_ID = BEER_PATH + "/";
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

        mockMvc.perform(get(BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(beerServiceImpl.listBeers().size())));
    }

    @Test
    void getBeerById() throws Exception {

        Beer testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_PATH_ID + testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BEER_PATH_ID + UUID.randomUUID()))
                .andExpect(status().isNotFound());


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

        mockMvc.perform(post(BEER_PATH)
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

        mockMvc.perform(put(BEER_PATH_ID + beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(beerService).ubdateBeerById(any(UUID.class),any(Beer.class));

    }

    @Test
    void deleteById() throws Exception {

        Beer beer = beerServiceImpl.listBeers().get(0);

        mockMvc.perform(delete(BEER_PATH_ID + beer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void patchBeerById() throws Exception {

        Beer beer = beerServiceImpl.listBeers().get(0);
        beer.setBeerName("Tecate");
        beer.setUpdateDate(LocalDateTime.now());

        mockMvc.perform(patch(BEER_PATH_ID + beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
        verify(beerService).patchBeerById(any(UUID.class), any(Beer.class));
    }
}