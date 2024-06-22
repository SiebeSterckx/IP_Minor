package be.ucll.ip.minor.reeks1210.team.controller;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import be.ucll.ip.minor.reeks1210.regatta.domain.RegattaService;
import be.ucll.ip.minor.reeks1210.regattaTeam.RegattaTeamDto;
import be.ucll.ip.minor.reeks1210.regattaTeam.RegattaTeamRestController;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import be.ucll.ip.minor.reeks1210.team.domain.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/team")
public class TeamRestController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private RegattaService regattaService;

    @GetMapping("/overview")
    public Iterable<TeamDto> getTeams() {
        return toDto(teamService.getAllTeams());
    }

    @DeleteMapping("/delete/{id}")
    public Team deleteTeam(@PathVariable("id") Long id) {
        try {
            Team team = teamService.getTeam(id);
            List<Long> regids = new ArrayList<>();
            for(Regatta regatta: team.getRegattaNames()){
                regids.add(regatta.getId());
            }
            for(long rid: regids){
                regattaService.removeTeamFromRegatta(rid,id);
            }
            teamService.deleteTeam(id);
            return team;
        }
        catch (ServiceException e) {
            throw new ServiceException(e.getAction(), e.getMessage());
        }catch (RuntimeException e){throw new ServiceException("Delete", "Team with this id doesn't exist");
        }
    }

    @PostMapping("/add")
    public TeamDto addTeam(@Valid @RequestBody TeamDto teamDto) {
        teamService.createTeam(teamDto);
        return teamDto;
    }

    @PutMapping("/update/{id}")
    public TeamDto updateTeam(@PathVariable("id") Long id, @Valid @RequestBody TeamDto teamDto) {
        teamService.updateTeam(id, teamDto);
        return teamDto;
    }

    // search  http://localhost:8080/api/team/search?category=ab452po
    @GetMapping("/search")
    public Iterable<TeamDto> searchTeamByCategory(@RequestParam("category") String category){
        return toDto(teamService.searchTeamByCategory(category));
    }
    // search  http://localhost:8080/api/team/search/{number}
    @GetMapping("/search/{number}")
    public Iterable<TeamDto> searchTeamByOccupants(@PathVariable("number") int number){
        return toDto(teamService.searchTeamByOccupants(number));
    }


    //Werkt als grote try-catch blok voor alle exceptions
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ServiceException.class, ResponseStatusException.class})
    public Map<String, String> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            ((MethodArgumentNotValidException)ex).getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        else if (ex instanceof ServiceException) {
            errors.put(((ServiceException) ex).getAction(), ex.getMessage());
        }
        else {
            errors.put(((ResponseStatusException) ex).getReason(), ex.getCause().getMessage());
        }
        return errors;
    }

    public static Iterable<TeamDto> toDto(Iterable<Team> teams){
        List<TeamDto> teamlist = new ArrayList<>();
        for(Team team: teams){
            teamlist.add(toDto(team));
        }
        return teamlist;
    }
    public static TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();

        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setClub(team.getClub());
        dto.setCategory(team.getCategory());
        dto.setPassengers(team.getPassengers());
        dto.setRegattas(team.getRegattaNames().stream().map(RegattaTeamRestController::toRegattaTeamDto).collect(Collectors.toList()));

        return dto;
    }

    public static RegattaTeamDto toRegattaTeamDto(Regatta regatta) {
        RegattaTeamDto dto = new RegattaTeamDto();
        dto.setName(regatta.getName());
        return dto;
    }
}
