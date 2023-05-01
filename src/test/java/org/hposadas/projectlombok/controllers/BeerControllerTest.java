package org.hposadas.projectlombok.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hposadas.projectlombok.config.SpringSecConfig;
import org.hposadas.projectlombok.entities.model.BeerDTO;
import org.hposadas.projectlombok.entities.model.BeerStyle;
import org.hposadas.projectlombok.services.BeerService;
import org.hposadas.projectlombok.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//@SpringBootTest
@WebMvcTest(BeerController.class)   //debemos especificar cual es el controlador que queremos testear. Si no lo especificamos entonces va a traer todos los controladores que tengamos.
@Import(SpringSecConfig.class)
class BeerControllerTest {

    //atributos
    public final static String BEER_PATH = "/api/v1/beer";
    public final static String BEER_PATH_ID = BEER_PATH + "/";
    public final static String USER = "user1";
    public final static String PASSWORD = "password";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean           //indica a mockito brindar un mock de BeerService al contexto Spring
    BeerService beerService;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<BeerDTO> beerDTOArgumentCaptor;

    BeerServiceImpl beerServiceImpl;

    //métodos

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtRequestPostProcessors =
            jwt().jwt(jwt-> {
                jwt
                        .claims(claims -> {
                            claims.put("scope", "message-read");
                            claims.put("scope", "message-write");
                        })
                        .subject("messaging-client")
                        .notBefore(Instant.now().minusSeconds(5L));
            });

    @Test
    void listBeers() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.listBeers(null, null, false, null, null));

        mockMvc.perform(get(BEER_PATH)
                        .with(jwtRequestPostProcessors)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {

        BeerDTO testBeer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_PATH_ID + testBeer.getId())
                        .with(jwtRequestPostProcessors)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {

        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BEER_PATH_ID + UUID.randomUUID())
                .with(jwtRequestPostProcessors))
                .andExpect(status().isNotFound());


    }

    @Test
    void handlePost() throws Exception {

        BeerDTO beer = BeerDTO.builder()
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

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beer);

        mockMvc.perform(post(BEER_PATH)
                        .with(jwtRequestPostProcessors)
                .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().is(201))
                .andExpect(header().exists("Location"));

    }

    @Test
    void updateById() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("Ultra");
        beer.setUpdateDate(LocalDateTime.now());

        given(beerService.ubdateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BEER_PATH_ID + beer.getId())
                        .with(jwtRequestPostProcessors)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer))
                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

        verify(beerService).ubdateBeerById(any(UUID.class),any(BeerDTO.class));

    }

    @Test
    void deleteById() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);

        given(beerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(BEER_PATH_ID + beer.getId())
                        .with(jwtRequestPostProcessors)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteById(uuidArgumentCaptor.capture());

        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void patchBeerById() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("Tecate");
        beer.setUpdateDate(LocalDateTime.now());

        mockMvc.perform(patch(BEER_PATH_ID + beer.getId())
                        .with(jwtRequestPostProcessors)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
        verify(beerService).patchBeerById(any(UUID.class), any(BeerDTO.class));
    }

    /*                          Validation tests                                */
    @Test
    void testCreatedBeerNotValidated() throws Exception {

        BeerDTO beerDTO = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(BEER_PATH)
                        .with(jwtRequestPostProcessors)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(jsonPath("$.length()", is(6)))
                .andExpect(status().isBadRequest())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updateByIdNonValidTest() throws Exception {

        BeerDTO beer = beerServiceImpl.listBeers(null, null, false, 1, 25).getContent().get(0);
        beer.setBeerName("");
        beer.setUpdateDate(LocalDateTime.now());

        given(beerService.ubdateBeerById(any(), any())).willReturn(Optional.of(beer));

        mockMvc.perform(put(BEER_PATH_ID + beer.getId())
                        .with(jwtRequestPostProcessors)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidauthentication() throws Exception {

        given(beerService.listBeers(any(), any(), any(), any(), any()))
                .willReturn(beerServiceImpl.listBeers(null, null, false, null, null));

        mockMvc.perform(get(BEER_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}