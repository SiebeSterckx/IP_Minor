package be.ucll.ip.minor.reeks1210.Boat;

import be.ucll.ip.minor.reeks1210.Reeks1210Application;
import be.ucll.ip.minor.reeks1210.boat.controller.BoatDto;
import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.boat.domain.BoatService;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Reeks1210Application.class)
@AutoConfigureMockMvc

public class BoatRestControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    // We use @MockBean to create and inject a mock for the service
    @MockBean
    private BoatService service;

    @Autowired
    private WebApplicationContext context;

    // To not start the server at all but to test only the layer below that,
    // where Spring handles the incoming HTTP request and hands it off to your controller.
    // That way, almost of the full stack is used, and your code will be called in exactly the
    // same way as if it were processing a real HTTP request but without the cost of starting the server.
    // To do that, use Spring’s MockMvc and ask for that to be injected for
    // you by using the @WebMvcTest annotation on the test case.
    @Autowired
    MockMvc boatRestController;

    private Boat boat1, boat2, boat3;
    private BoatDto boatDto1, boatDto2;


    @BeforeEach
    public void setUp(){
        boat1= BoatBuilder.aBoatBanana().build();
        boat2= BoatBuilder.aBoatApple().build();
        boat3= BoatBuilder.aBoatGrape().build();


        boatDto1= BoatDtoBuilder.aBoatBanana().build();
        boatDto2= BoatDtoBuilder.anInvalidBoatWithNoName().build();
    }

    //overview happy
    @Test
    public void givenBoats_whenGetRequestToAllBoats_thenJSONWithAllBoatsReturned() throws Exception {
        //given
        List<Boat> allBoats = Arrays.asList(boat1, boat2, boat3);
        //mocking
        given(service.getAllBoats()).willReturn(allBoats);
        //when
        boatRestController.perform(get("/api/boat/overview")
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(boat1.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(boat2.getName())))
                .andExpect(jsonPath("$[2].name", Is.is(boat3.getName())));
    }

    //delete happy
    @Test
    public void givenABoatId_whenDeleteRequest_thenBoatIsDeleted() throws Exception {
        //given
        Long id = boat1.getId();
        //when
        boatRestController.perform(delete("/api/boat/delete/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk());
        verify(service, times(1)).deleteBoat(id);
    }


    //add happy
//    @Test
//    public void givenNoBoats_whenPostRequestToAddAValidBoat_thenJSONIsReturned() throws Exception {
//        //given
//        List<Boat> boats = Arrays.asList(boat1);
//        //mocking
//        when(service.createBoat(boatDto1)).thenReturn(boat1);
//        when(service.getAllBoats()).thenReturn(boats);
//        //when
//        boatRestController.perform(post("/api/boat/add")
//                        .content(mapper.writeValueAsString(boatDto1))
//                        .contentType(MediaType.APPLICATION_JSON))
//                //then
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].name", Is.is(boatDto1.getName())));
//    }

    @Test
    public void givenNoBoats_whenPostRequestToAddAValidBoat_thenJSONIsReturned() throws Exception {
        //given
        List<Boat> boats = Arrays.asList(boat1);
        String nom1 = boat1.getName();
        String nom2 = boatDto1.getName();
        //mocking
        when(service.createBoat(boatDto1)).thenReturn(boat1);
        when(service.searchBoatunique(boat1.getName(), boat1.getEmail(), boat1.getInsuranceNumber())).thenReturn(boats);

        //when
        boatRestController.perform(post("/api/boat/add")
                        .content(mapper.writeValueAsString(boatDto1))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(boatDto1.getName())));
    }


    //update happy
    @Test
    public void givenABoatIdAndValidBoat_whenPutRequestToUpdate_thenBoatIsUpdated() throws Exception {
        //given
        Long id = boat2.getId();
        BoatDto updatedBoatDto = BoatDtoBuilder.aBoatGrape().build();
        Boat updatedBoat = BoatBuilder.aBoatBanana().withId(id).build();
        //mocking
        given(service.updateBoat(id, updatedBoatDto)).willReturn(updatedBoat);
        given(service.getAllBoats()).willReturn(Arrays.asList(updatedBoat));
        //when
        boatRestController.perform(put("/api/boat/update/{id}", id)
                        .content(mapper.writeValueAsString(updatedBoatDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", Is.is(updatedBoat.getName())));
    }
    

    //update unhappy
    @Test
    public void givenABoatIdAndInvalidBoat_whenPutRequestToUpdate_thenBadRequestIsReturned() throws Exception {
        //given
        Long id = boat2.getId();
        BoatDto updatedBoatDto = boatDto2;
        //when
        boatRestController.perform(put("/api/boat/update/{id}", id)
                        .content(mapper.writeValueAsString(updatedBoatDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidInsuranceNumber_whenGetRequestToSearchBoatByInsuranceNumber_thenBoatsAreReturned() throws Exception {
        // given
        String insurance = "123456789C";
        List<Boat> boats = Arrays.asList(boat3);
        given(service.searchBoatByInsuranceNumber(insurance)).willReturn(boats);

        // when
        boatRestController.perform(get("/api/boat/search")
                        .param("insurance", insurance))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].insuranceNumber", Is.is(insurance)));

    }


}
