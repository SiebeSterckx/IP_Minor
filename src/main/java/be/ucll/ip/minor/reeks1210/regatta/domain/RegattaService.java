package be.ucll.ip.minor.reeks1210.regatta.domain;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.regatta.controller.RegattaDto;
import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import be.ucll.ip.minor.reeks1210.team.domain.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class RegattaService {

    @Autowired
    private RegattaRepository regattaRepository;

//    many to many thingy
    @Autowired
    private TeamService teamService;

    public List<Regatta> getAllRegattas(String sorter) {

        // PagingAndSortingRepository (which is part of JpaRepository)
        // model.addAttribute("patients", patientRepository.findAll(Sort.by("name").ascending()));
        if (sorter.equals("name1")) {
            return regattaRepository.findAll((Sort.by(Sort.Direction.ASC, "name")));
        }
        else if (sorter.equals("name2")) {
            return regattaRepository.findAll((Sort.by(Sort.Direction.DESC, "name")));
        }
        else if (sorter.equals("date1")) {
            return regattaRepository.findAll((Sort.by(Sort.Direction.ASC, "date")));
        }
        else if (sorter.equals("date2")) {
            return regattaRepository.findAll((Sort.by(Sort.Direction.DESC, "date")));
        }
        return regattaRepository.findAll();
    }

    public Page<Regatta> getRegattaPage(int page) {
        PageRequest firstPageWithTwoElements = PageRequest.of(page, 4);
        return regattaRepository.findAll(firstPageWithTwoElements);
    }

    public Page<Regatta> getRegattaPage(PageRequest pageRequest) {
        return regattaRepository.findAll(pageRequest);
    }



    public Regatta getRegatta(long id) {
        return regattaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Regatta with " + id + " not found"));
    }

    public Regatta createRegatta(RegattaDto dto) {
        Regatta regatta = new Regatta();

        regatta.setName(dto.getName());
        regatta.setClubName(dto.getClubName());
        regatta.setDate(dto.getDate());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategory(dto.getCategory());

        // CrudRepository (which is part of JpaRepository)
        return regattaRepository.save(regatta);
    }

    public Regatta updateRegatta(Long id, RegattaDto dto) {
        Regatta regatta = getRegatta(id);

        regatta.setName(dto.getName());
        regatta.setClubName(dto.getClubName());
        regatta.setDate(dto.getDate());
        regatta.setMaxTeams(dto.getMaxTeams());
        regatta.setCategory(dto.getCategory());
        if(!regatta.getTeams().isEmpty()) {
            for (Team team : regatta.getTeams()) {
                regatta.addableTeam(team);
            }
        }
        return regattaRepository.save(regatta);
    }

    public void removeRegatta(Long id){
        Regatta regatta = getRegatta(id);
//        if(!regatta.getTeams().isEmpty()){throw new ServiceException("This regatta cannot be removed while it has participants");}
        List<Long> teamids = new ArrayList<>();
        for(Team team: regatta.getTeams()){
            teamids.add(team.getId());
        }
        for(long tid: teamids){
            removeTeamFromRegatta(id,tid);
        }
        regattaRepository.delete(regatta);
    }

    public List<Regatta> filterdate(LocalDate first, LocalDate second, List<Regatta> regattaRepositor){
        List<Regatta> filteredlist = new ArrayList<>();
        for(Regatta regatta: regattaRepositor){
            if(first.isBefore(regatta.getDate()) && second.isAfter(regatta.getDate())){
                filteredlist.add(regatta);
            }
        }
        return filteredlist;
    }

    public List<Regatta> filterdate(LocalDate first, List<Regatta> regattaRepositor){
        List<Regatta> filteredlist = new ArrayList<>();
        for(Regatta regatta: regattaRepositor){
            if(first.isBefore(regatta.getDate())){
                filteredlist.add(regatta);
            }
        }
        return filteredlist;
    }

    public List<Regatta> filtercat(String category, List<Regatta> regattaRepositor){
        List<Regatta> filteredlist = new ArrayList<>();
        for(Regatta regatta: regattaRepositor){
            if(regatta.getCategory().toLowerCase().contains(category.toLowerCase())){
                filteredlist.add(regatta);
            }
        }
        return filteredlist;
    }

    public List<Regatta> filterDateAndCat(LocalDate first, LocalDate last, String category){
        List<Regatta> filteredlist = regattaRepository.findAll();
        if(first != null && last != null){
            filteredlist = filterdate(first,last, filteredlist);
        }
        else if(first != null && last == null){
            filteredlist = filterdate(first,filteredlist);
        }
        if(category != null && !category.isEmpty()){
            filteredlist = filtercat(category,filteredlist);
        }
        return filteredlist;
    }

    public Page<Regatta> filterDateAndCatPage(LocalDate first, LocalDate last, String category, int page){
        Page<Regatta> pagelist = regattaRepository.findByCategoryContaining(PageRequest.of(page,4),category);
        if(first != null && last != null){
            pagelist = regattaRepository.findByDateBetweenAndCategoryContaining(PageRequest.of(page,4),first,last,category);
        }
        else if(first != null && last == null) {
            pagelist = regattaRepository.findByDateAfterAndCategoryContaining(PageRequest.of(page, 4), first, category);
        }
        return pagelist;
    }



    public Regatta addTeam(Long regattaId, TeamDto teamDto) {
        Regatta regatta = getRegatta(regattaId);

        Team team = new Team();
        team.setName(teamDto.getName());
        team.setCategory(teamDto.getCategory());
        team.setPassengers(teamDto.getPassengers());
        team.setClub(teamDto.getClub());

        regatta.addTeam(team);

        return regattaRepository.save(regatta);
    }

    public Regatta addTeamToRegatta(Long regattaId, Long teamId) {
        try{
            Regatta regatta = getRegatta(regattaId);
            Team team = teamService.getTeam(teamId);
            regatta.addTeam(team);
            return regattaRepository.save(regatta);
        }catch(ServiceException e){throw new ServiceException(e.getAction(), e.getMessage());}
        catch (RuntimeException e){
            throw new ServiceException("AddTeam", "team or regatta doesn't exist");
        }
    }

    public Regatta removeTeamFromRegatta(Long regattaId, Long teamId){
        try{
            Regatta regatta = getRegatta(regattaId);
            regatta.removeTeam(teamService.getTeam(teamId));
            regattaRepository.save(regatta);
            return regatta;
        }catch(ServiceException e){throw new ServiceException(e.getAction(), e.getMessage());}
        catch (RuntimeException e){
                throw new ServiceException("RemoveTeam", "Regatta or Team Doesn't exist");
        }
    }
}
