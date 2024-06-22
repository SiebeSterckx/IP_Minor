package be.ucll.ip.minor.reeks1210.Team;

import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import be.ucll.ip.minor.reeks1210.team.domain.TeamRepository;
import be.ucll.ip.minor.reeks1210.team.domain.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)

public class TeamServiceTest {

    @Mock
    TeamRepository teamRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    public void givenNoTeam_whenValidTeamAdded_ThenTeamIsAddedAndTeamIsReturned() {
        // given
        Team racer = TeamBuilder.aTeamRacers().build();
        TeamDto racerDto = TeamDtoBuilder.aTeamRacers().build();

        //mocking
        when(teamRepository.save(any())).thenReturn(racer);

        // when
        Team addedTeam = teamService.createTeam(racerDto);

        // then
        assertThat(racer.getName()).isSameAs(addedTeam.getName());
    }

/*    @Test  //(expected = ServiceException.class)
    // THIS IS NOT ENGOUGH BECAUSE THEN YOU DON'T KNOW IF RIGHT MESSAGE IS THROWN
    public void givenTeams_whenValidTeamAddedWithAlreadyUsedNameAndCategoryCombination_ThenTeamIsNotAddedAndErrorIsReturned() {
        // given
        Team speeder = TeamBuilder.aTeamSpeeders().build();
        TeamDto speederDto = TeamDtoBuilder.aTeamSpeeders().build();

        when(teamRepository.findAllByCategoryContainingIgnoreCase(speeder.getCategory())).thenReturn(speederDto);

        // when
        final Throwable raisedException = catchThrowable(() -> teamService.createTeam(speederDto));

        // then
        assertThat(raisedException).isInstanceOf(ServiceException.class)
                .hasMessageContaining("team.unique");
    }*/

    @Test
    public void givenTeamId_whenGetTeam_thenTeamIsReturned() {
        // given
        Team team = TeamBuilder.aTeam().build();

        //mocking
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        // when
        Team foundTeam = teamService.getTeam(team.getId());

        // then
        assertThat(foundTeam).isEqualTo(team);
    }

    @Test
    public void givenTeamId_whenGetNonExistingTeam_thenExceptionIsThrown() {
        // given
        long nonExistingTeamId = Long.valueOf(123);

        //mocking
        when(teamRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> teamService.getTeam(nonExistingTeamId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void givenTeamIdAndTeamDto_whenUpdateTeam_thenTeamIsUpdatedAndReturned() {
        // given
        Team team = TeamBuilder.aTeam().build();
        TeamDto updatedTeamDto = TeamDtoBuilder.aTeam().withName("Updated Name").build();

        //mocking
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // when
        Team updatedTeam = teamService.updateTeam(team.getId(), updatedTeamDto);

        // then
        assertThat(updatedTeam.getName()).isEqualTo(updatedTeamDto.getName());
    }

    @Test
    public void givenTeamId_whenDeleteTeam_thenTeamIsDeleted() {
        // given
        Team team = TeamBuilder.aTeam().build();

        //mocking
        when(teamRepository.findById(anyLong())).thenReturn(Optional.of(team));

        // when
        teamService.deleteTeam(team.getId());

        // then
        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    public void givenCategory_whenSearchTeamByCategory_thenTeamsAreReturned() {
        // given
        String category = "Category";
        List<Team> teams = Arrays.asList(TeamBuilder.aTeam().withCategory(category).build(), TeamBuilder.aTeam().withCategory(category).build());

        //mocking
        when(teamRepository.findAllByCategoryContainingIgnoreCase(anyString())).thenReturn(teams);

        // when
        Iterable<Team> foundTeams = teamService.searchTeamByCategory(category);

        // then
        assertThat(foundTeams).containsExactlyElementsOf(teams);
    }

    @Test
    public void givenOccupants_whenSearchTeamByOccupants_thenTeamsAreReturned() {
        // given
        int occupants = 5;
        List<Team> teams = Arrays.asList(TeamBuilder.aTeam().withOccupants(occupants).build(), TeamBuilder.aTeam().withOccupants(occupants).build());

        //mocking
        when(teamRepository.findAllByOccupants(anyInt())).thenReturn(teams);

        // when
        Iterable<Team> foundTeams = teamService.searchTeamByOccupants(occupants);

        // then
        assertThat(foundTeams).containsExactlyElementsOf(teams);
    }

}
