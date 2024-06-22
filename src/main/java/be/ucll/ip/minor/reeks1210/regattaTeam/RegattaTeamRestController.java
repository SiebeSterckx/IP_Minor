package be.ucll.ip.minor.reeks1210.regattaTeam;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.regatta.controller.RegattaDto;
import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import be.ucll.ip.minor.reeks1210.regatta.domain.RegattaService;
import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import be.ucll.ip.minor.reeks1210.team.domain.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/regatta-team")
public class RegattaTeamRestController {

    @Autowired
    private RegattaService regattaService;
    @Autowired
    private TeamService teamService;

    @PostMapping("/add/team/{teamId}/to/regatta/{regattaId}")
    public TeamDto  addTeamToRegatta(@PathVariable("regattaId") Long regattaId, @PathVariable("teamId") Long teamId) {
        regattaService.addTeamToRegatta(regattaId, teamId);
        return toDto(teamService.getTeam(teamId));
    }

    @PostMapping("/remove/team/{teamId}/from/regatta/{regattaId}")
    public TeamDto  RemoveTeamForRegatta(@PathVariable("regattaId") Long regattaId, @PathVariable("teamId") Long teamId) {
        regattaService.removeTeamFromRegatta(regattaId, teamId);
        return toDto(teamService.getTeam(teamId));
    }

    @GetMapping("/teams")
    public Iterable<TeamDto> getTeamsInRegatta(@RequestParam("regattaId") Long regattaId) {
        List<TeamDto> list = new ArrayList<>();
        try{
        for(Team team: regattaService.getRegatta(regattaId).getTeams()){
            list.add(toDto(team));
        }
        return list;
        }catch(RuntimeException e){throw new ServiceException("SearchTeams", e.getMessage());}
    }

    @GetMapping("/regatta")
    public Iterable<RegattaDto> overviewR (Model model) {
        List<RegattaDto> list = new ArrayList<>();
        for(Regatta reg: regattaService.getAllRegattas("")){
            list.add(toDto(reg));
        }

        return list;
    }

    @GetMapping("/team")
    public Iterable<TeamDto> getTeams() {
        List<TeamDto> list = new ArrayList<>();
        for(Team team: teamService.getAllTeams()){
            list.add(toDto(team));
        }
        return list;
    }




    public static RegattaDto toDto(Regatta regatta) {
        RegattaDto dto = new RegattaDto();

        dto.setId(regatta.getId());
        dto.setName(regatta.getName());
        dto.setClubName(regatta.getClubName());
        dto.setDate(regatta.getDate());
        dto.setMaxTeams(regatta.getMaxTeams());
        dto.setCategory(regatta.getCategory());
        dto.setTeams(regatta.getTeams().stream().map(RegattaTeamRestController::toRegattaTeamDto).collect(Collectors.toList()));

        return dto;
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

    public static RegattaTeamDto toRegattaTeamDto(Team team) {
        RegattaTeamDto dto = new RegattaTeamDto();
        dto.setName(team.getName());
        return dto;
    }
    public static RegattaTeamDto toRegattaTeamDto(Regatta regatta) {
        RegattaTeamDto dto = new RegattaTeamDto();
        dto.setName(regatta.getName());
        return dto;
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
}