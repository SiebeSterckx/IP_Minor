package be.ucll.ip.minor.reeks1210.Team;

import be.ucll.ip.minor.reeks1210.team.domain.Team;
import be.ucll.ip.minor.reeks1210.team.domain.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest


public class TeamRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TeamRepository teamRepository;


    @Test
    public void givenTeamsCreated_whenFindByCategory_thenAllTeamsFoundAreReturned() {
        // given
        Team team1 = TeamBuilder.aTeamRacers().build();
        entityManager.merge(team1);
        Team team2 = TeamBuilder.aTeamSpeeders().build();
        entityManager.merge(team2);
        Team team3 = TeamBuilder.aTeamSurfers().build();
        entityManager.merge(team3);
        entityManager.flush();

        // when
        Iterable<Team> teams = teamRepository.findAllByCategoryContainingIgnoreCase("Sailing");

        // then
        assertEquals(1, ((List<Team>) teams).size());
    }


    @Test
    public void givenTeamsCreated_whenFindByOccupants_thenAllTeamsFoundAreReturned() {
        // given
        Team team1 = TeamBuilder.aTeamRacers().build();
        entityManager.merge(team1);
        Team team2 = TeamBuilder.aTeamSpeeders().build();
        entityManager.merge(team2);
        Team team3 = TeamBuilder.aTeamSurfers().build();
        entityManager.merge(team3);
        entityManager.flush();

        // when
        Iterable<Team> teams = teamRepository.findAllByOccupants(7);

        // then
        assertEquals(2, ((List<Team>) teams).size());
    }


}
