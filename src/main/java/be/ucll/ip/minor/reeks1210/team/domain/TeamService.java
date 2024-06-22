package be.ucll.ip.minor.reeks1210.team.domain;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.regatta.controller.RegattaDto;
import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import be.ucll.ip.minor.reeks1210.regatta.domain.RegattaService;
import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        if (teamRepository.findAll().isEmpty()) {
            sampleTeams();
        }
        return teamRepository.findAll();
    }

    public void sampleTeams() {
        Team team1 = new Team();
        team1.setName("Racers");
        team1.setCategory("Sailing");
        team1.setPassengers(7);
        team1.setClub("Club 1");
        teamRepository.save(team1);

        Team team4 = new Team();
        team4.setName("Racers");
        team4.setCategory("Zeil Regatta");
        team4.setPassengers(7);
        team4.setClub("Club 1");
        teamRepository.save(team4);

        Team team5 = new Team();
        team5.setName("Rakers");
        team5.setCategory("Zeil Regatta");
        team5.setPassengers(7);
        team5.setClub("Club 1");
        teamRepository.save(team5);

        Team team6 = new Team();
        team6.setName("Racers");
        team6.setCategory("Zeevaart Regatta");
        team6.setPassengers(7);
        team6.setClub("Club 1");
        teamRepository.save(team6);

        Team team7 = new Team();
        team7.setName("Speedos");
        team7.setCategory("Zeevaart Regatta");
        team7.setPassengers(7);
        team7.setClub("Club 1");
        teamRepository.save(team7);

        Team team2 = new Team();
        team2.setName("Speeders");
        team2.setCategory("Sailing");
        team2.setPassengers(7);
        team2.setClub("Club 2");
        teamRepository.save(team2);

        Team team3 = new Team();
        team3.setName("Surfers");
        team3.setCategory("Sailing");
        team3.setPassengers(7);
        team3.setClub("Club 3");
        teamRepository.save(team3);
    }

    public Team getTeam(long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team with id: " + id + " not found"));
    }

    public Team createTeam(TeamDto dto) {
        try{
        Team team = new Team();

        team.setName(dto.getName());
        team.setCategory(dto.getCategory());
        team.setPassengers(dto.getPassengers());
        team.setClub(dto.getClub());

        return teamRepository.save(team);
        }catch (RuntimeException e){throw new ServiceException("AddTeam", "This Team already exists");}
    }

    public Team updateTeam(Long id, TeamDto dto) {
        try {
            Team team = getTeam(id);

            team.setName(dto.getName());
            team.setCategory(dto.getCategory());
            team.setPassengers(dto.getPassengers());
            team.setClub(dto.getClub());
            if(!team.getRegattaNames().isEmpty()){
                for (Regatta regatta : team.getRegattaNames()) {
                    regatta.addableTeam(team);
                }
            }
            Set<Regatta> r = team.getRegattaNames();

            return teamRepository.save(team);
        }catch (RuntimeException e){throw new ServiceException("UpdateTeam", "Cannot update this team   ");}

    }

    public void deleteTeam(Long id) {
        Team team = getTeam(id);
//        if(!team.getRegattaNames().isEmpty()){throw new ServiceException("This team cannot be removed while it is participating in a race");}
        teamRepository.delete(team);
    }

    public Iterable<Team> searchTeamByCategory(String category) {
        return teamRepository.findAllByCategoryContainingIgnoreCase(category);
    }

    public Iterable<Team> searchTeamByOccupants(int number) {
        return teamRepository.findAllByOccupants(number);
    }
}
