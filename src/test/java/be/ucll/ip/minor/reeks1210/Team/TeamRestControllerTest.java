package be.ucll.ip.minor.reeks1210.Team;

import be.ucll.ip.minor.reeks1210.Reeks1210Application;
import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import be.ucll.ip.minor.reeks1210.team.domain.TeamService;
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


public class TeamRestControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    // We use @MockBean to create and inject a mock for the service
    @MockBean
    private TeamService service;

    @Autowired
    private WebApplicationContext context;

    // To not start the server at all but to test only the layer below that,
    // where Spring handles the incoming HTTP request and hands it off to your controller.
    // That way, almost of the full stack is used, and your code will be called in exactly the
    // same way as if it were processing a real HTTP request but without the cost of starting the server.
    // To do that, use Spring’s MockMvc and ask for that to be injected for
    // you by using the @WebMvcTest annotation on the test case.
    @Autowired
    MockMvc teamRestController;

    private Team team1, team2, team3, team4;
    private TeamDto teamDto1, teamDto2;

    @BeforeEach
    public void setUp(){
        team1= TeamBuilder.aTeamRacers().build();
        team2= TeamBuilder.aTeamSpeeders().build();
        team3= TeamBuilder.aTeamSurfers().build();
        team4= TeamBuilder.aTeamSurfers2().build();

        teamDto1= TeamDtoBuilder.aTeamRacers().build();
        teamDto2= TeamDtoBuilder.anInvalidTeamWithNoName().build();
    }

    //overview happy
    @Test
    public void givenTeams_whenGetRequestToAllTeams_thenJSONWithAllTeamsReturned() throws Exception {
        //given
        List<Team> allTeams = Arrays.asList(team1, team2, team3);
        //mocking
        given(service.getAllTeams()).willReturn(allTeams);
        //when
        teamRestController.perform(get("/api/team/overview")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", Is.is(team1.getName())))
                .andExpect(jsonPath("$[1].name", Is.is(team2.getName())))
                .andExpect(jsonPath("$[2].name", Is.is(team3.getName())));
    }

    //delete happy
    @Test
    public void givenATeamId_whenDeleteRequest_thenTeamIsDeleted() throws Exception {
        //given
        Long id = team1.getId();
        //mocking
        given(service.getTeam(id)).willReturn(team1);
        //when
        teamRestController.perform(delete("/api/team/delete/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk());
        verify(service, times(1)).deleteTeam(id);
    }

    //delete unhappy
/*    @Test
    public void givenNonExistingTeamId_whenDeleteRequest_thenReturns404ErrorWithCorrectMessage() throws Exception {
        // Given
        Long id = Long.valueOf(123);
        String expectedErrorMessage = "Team with id " + id + " not found.";
        doThrow(new RuntimeException(expectedErrorMessage)).when(service).deleteTeam(id);

        // When
        MvcResult result = teamRestController.perform(delete("/api/team/delete/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        // Then
        String actualErrorMessage = result.getResponse().getContentAsString();
        assertThat(actualErrorMessage).isEqualTo(expectedErrorMessage);
        verify(service, times(1)).deleteTeam(id);
    }*/



    //add happy
    @Test
    public void givenNoTeams_whenPostRequestToAddAValidTeam_thenJSONIsReturned() throws Exception {
        //given
        List<Team> teams = Arrays.asList(team1);
        //mocking
        when(service.createTeam(teamDto1)).thenReturn(team1);
        when(service.getAllTeams()).thenReturn(teams);
        //when
        teamRestController.perform(post("/api/team/add")
                .content(mapper.writeValueAsString(teamDto1))
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", Is.is(teamDto1.getName())));
    }

    //add unhappy
    @Test
    public void givenNoTeams_whenPostRequestToAddAnInvalidTeam_thenErrorInJSONformatIsReturned() throws Exception {
        //when
        teamRestController.perform(post("/api/team/add")
                .content(mapper.writeValueAsString(teamDto2))
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", anyOf(equalTo("name.too.short"), equalTo("name.missing"))));

    }

    //update happy
    @Test
    public void givenATeamIdAndValidTeam_whenPutRequestToUpdate_thenTeamIsUpdated() throws Exception {
        //given
        Long id = team2.getId();
        TeamDto updatedTeamDto = TeamDtoBuilder.aTeamSurfers().build();
        Team updatedTeam = TeamBuilder.aTeamRacers().withId(id).build();
        //mocking
        given(service.updateTeam(id, updatedTeamDto)).willReturn(updatedTeam);
        given(service.getAllTeams()).willReturn(Arrays.asList(updatedTeam));
        //when
        teamRestController.perform(put("/api/team/update/{id}", id)
                        .content(mapper.writeValueAsString(updatedTeamDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", Is.is(updatedTeamDto.getName())));
    }

    //update unhappy
    @Test
    public void givenATeamIdAndInvalidTeam_whenPutRequestToUpdate_thenBadRequestIsReturned() throws Exception {
        //given
        Long id = team2.getId();
        TeamDto updatedTeamDto = teamDto2;
        //when
        teamRestController.perform(put("/api/team/update/{id}", id)
                        .content(mapper.writeValueAsString(updatedTeamDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isBadRequest());
    }

    //search-category happy
    @Test
    public void givenValidCategory_whenGetRequestToSearchTeamByCategory_thenTeamsAreReturned() throws Exception {
        // given
        String category = "Surfing";
        List<Team> teams = Arrays.asList(team3, team4);
        given(service.searchTeamByCategory(category)).willReturn(teams);

        // when
        teamRestController.perform(get("/api/team/search")
                        .param("category", category))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].category", Is.is(category)))
                .andExpect(jsonPath("$[1].category", Is.is(category)));
    }

    //search-occupants happy
    @Test
    public void givenValidNumberOfOccupants_whenGetRequestToSearchTeamByOccupants_thenTeamsAreReturned() throws Exception {
        // given
        int numberOfOccupants = 8;
        List<Team> teams = Arrays.asList(team1, team2, team4);
        given(service.searchTeamByOccupants(numberOfOccupants)).willReturn(teams);

        // when
        teamRestController.perform(get("/api/team/search/{number}", numberOfOccupants))
                // then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].passengers", lessThan(numberOfOccupants)))
                .andExpect(jsonPath("$[1].passengers", lessThan(numberOfOccupants)))
                .andExpect(jsonPath("$[2].passengers", lessThan(numberOfOccupants)));
    }



}

