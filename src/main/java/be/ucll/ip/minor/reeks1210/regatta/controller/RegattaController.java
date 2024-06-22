package be.ucll.ip.minor.reeks1210.regatta.controller;

import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import be.ucll.ip.minor.reeks1210.regatta.domain.RegattaService;
import be.ucll.ip.minor.reeks1210.storage.domain.Storage;
import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class RegattaController {

    private static Logger LOGGER = LoggerFactory.getLogger(RegattaController.class);

    private final RegattaService regattaService;

    private int i = 0;

    @Autowired
    public RegattaController(RegattaService regattaService) {
        this.regattaService = regattaService;
    }

    @GetMapping("/regatta/overview")
    public String overview (Model model) {
        List<Regatta> allRegattas = regattaService.getAllRegattas("");

        if (allRegattas.isEmpty() && i != 1) {
            createSampleData();
            allRegattas = regattaService.getAllRegattas("");
        }
        else {
            i = 1;
        }

        model.addAttribute("alreadysorted", "not");
        model.addAttribute("regattas", allRegattas);
        return "overview-regatta";
    }

    @GetMapping("/regatta/overview/{page}")
    public String overviewPage (@PathVariable("page") int page, Model model) {


        Page<Regatta> allRegattas = regattaService.getRegattaPage(page -1);

        if (allRegattas.isEmpty() && i != 1) {
            createSampleData();
            allRegattas = regattaService.getRegattaPage(page);
        }
        else {
            i = 1;
        }

        model.addAttribute("alreadysorted", "not");
        model.addAttribute("regattas", allRegattas);
        model.addAttribute("NOpages", listofInt(allRegattas.getTotalPages()));
        return "overview-regatta";
    }

    private void createSampleData() {

        RegattaDto storica = new RegattaDto();
        storica.setId(1);
        storica.setName("Storica");
        storica.setClubName("UCLL");
        storica.setDate(LocalDate.of(2001,4,12));
        storica.setMaxTeams(10);
        storica.setCategory("Zeil Regatta");

        RegattaDto Stella = new RegattaDto();
        Stella.setId(2);
        Stella.setName("Stella");
        Stella.setClubName("ABInbev");
        Stella.setDate(LocalDate.of(2005,2,5));
        Stella.setMaxTeams(10);
        Stella.setCategory("Kajak Regatta");

        RegattaDto filter1 = new RegattaDto();
        filter1.setName("BinnenFilter");
        filter1.setClubName("ABInbev");
        filter1.setDate(LocalDate.of(2021,3,20));
        filter1.setMaxTeams(8);
        filter1.setCategory("Zeevaart Regatta");

        RegattaDto filter2 = new RegattaDto();
        filter2.setName("FilterBuitenDate");
        filter2.setClubName("ABInbev");
        filter2.setDate(LocalDate.of(2019,12,30));
        filter2.setMaxTeams(8);
        filter2.setCategory("Zeil Regatta");

        RegattaDto filter3 = new RegattaDto();
        filter3.setName("FilterWrongCat");
        filter3.setClubName("ABInbev");
        filter3.setDate(LocalDate.of(2022,1,1));
        filter3.setMaxTeams(8);
        filter3.setCategory("Zeil Regatta");

        regattaService.createRegatta(storica);
        regattaService.createRegatta(Stella);
        regattaService.createRegatta(filter1);
        regattaService.createRegatta(filter2);
        regattaService.createRegatta(filter3);

        i = 1;
    }

    @GetMapping("/regatta/add")
    public String add(Model model) {
        model.addAttribute("regattaDto", new RegattaDto());
        return "add-regatta";
    }

    @PostMapping("/regatta/add")
    public String add(@Valid RegattaDto regatta, BindingResult result, Model model) {
        try{
            if (result.hasErrors()) {
                model.addAttribute("regattaDto", regatta);
                return "add-regatta";
            }

            regattaService.createRegatta(regatta);
            return "redirect:/regatta/overview/1";
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            return "add-regatta";
        }
    }

    @GetMapping("/regatta/update")
    public String update(@RequestParam("id") long id, Model model) {
        try {
            Regatta regatta = regattaService.getRegatta(id);
            model.addAttribute("regattaDto", toDto(regatta));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "update-regatta";
    }

    private RegattaDto toDto(Regatta regatta) {
        RegattaDto dto = new RegattaDto();

        dto.setId(regatta.getId());
        dto.setName(regatta.getName());
        dto.setClubName(regatta.getClubName());
        dto.setDate(regatta.getDate());
        dto.setMaxTeams(regatta.getMaxTeams());
        dto.setCategory(regatta.getCategory());
        return dto;
    }

    @PostMapping("/regatta/update/{id}")
    public String update (@PathVariable("id") long id, @Valid RegattaDto regatta, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                LOGGER.error("ERRORS UPDATING");

                regatta.setId(id);
                model.addAttribute("regattaDto", regatta);
                return "update-regatta";
            }

            regattaService.updateRegatta(id, regatta);
            return "redirect:/regatta/overview/1";
        }catch(Exception e){
            model.addAttribute("error", e.getMessage());
            return "update-regatta";
        }
    }

    @GetMapping("/regatta/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {
        try {
            Regatta regatta = regattaService.getRegatta(id);
            model.addAttribute("regatta", toDto(regatta));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "delete-regatta";
    }

    @PostMapping("/regatta/delete")
    public String delete (@RequestParam("id") long id) {

        regattaService.removeRegatta(id);
        return "redirect:/regatta/overview/1";
    }


    @GetMapping("/regatta/sort/date1")
    public String sortdate(Model model) {
        List<Regatta> allRegattas = regattaService.getAllRegattas("date1");

        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.getAllRegattas("date1");

        model.addAttribute("regattas", allRegattas);
        model.addAttribute("alreadysorted", "date");
        return "overview-regatta";
    }

    @GetMapping("/regatta/sort/date1/{page}")
    public String sortdatePage(@PathVariable("page") int page, Model model) {
        Page<Regatta> allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("date").ascending()));

        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("date").ascending()));

        model.addAttribute("regattas", allRegattas);
        model.addAttribute("alreadysorted", "date");
        model.addAttribute("NOpages", listofInt(allRegattas.getTotalPages()));
        return "overview-regatta";
    }

    @GetMapping("/regatta/sort/date2")
    public String sortdate2(Model model) {
        List<Regatta> allRegattas = regattaService.getAllRegattas("date2");

        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.getAllRegattas("date2");

        model.addAttribute("regattas", allRegattas);
        return "overview-regatta";
    }

    @GetMapping("/regatta/sort/date2/{page}")
    public String sortdate2Page(@PathVariable("page") int page, Model model) {
        Page<Regatta> allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("date").descending()));

        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("date").descending()));

        model.addAttribute("regattas", allRegattas);
        model.addAttribute("NOpages", listofInt(allRegattas.getTotalPages()));
        return "overview-regatta";
    }


    @GetMapping("/regatta/sort/name1")
    public String sortname(Model model) {
        List<Regatta> allRegattas = regattaService.getAllRegattas("name1");

        if (allRegattas.isEmpty()) {
            createSampleData();
            allRegattas = regattaService.getAllRegattas("name1");
        }

        model.addAttribute("regattas", allRegattas);
        model.addAttribute("alreadysorted", "name");
        return "overview-regatta";
    }

    @GetMapping("/regatta/sort/name1/{page}")
    public String sortnamePage(@PathVariable("page") int page, Model model) {
        Page<Regatta> allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("name").ascending()));

        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("name").ascending()));

        model.addAttribute("regattas", allRegattas);
        model.addAttribute("alreadysorted", "name");
        model.addAttribute("NOpages", listofInt(allRegattas.getTotalPages()));
        return "overview-regatta";
    }




    @GetMapping("/regatta/sort/name2")
    public String sortname2(Model model) {
        List<Regatta> allRegattas = regattaService.getAllRegattas("name2");

        if (allRegattas.isEmpty()) {
            createSampleData();
            allRegattas = regattaService.getAllRegattas("name2");
        }
        model.addAttribute("regattas", allRegattas);
        return "overview-regatta";
    }

    @GetMapping("/regatta/sort/name2/{page}")
    public String sortname2Page(@PathVariable("page") int page, Model model) {
        Page<Regatta> allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("name").descending()));

        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.getRegattaPage(PageRequest.of(page-1, 4, Sort.by("name").descending()));

        model.addAttribute("regattas", allRegattas);
        model.addAttribute("NOpages", listofInt(allRegattas.getTotalPages()));
        return "overview-regatta";
    }

    @GetMapping("/regatta/search")
    public String search(@RequestParam(value = "dateAfter", required = false) LocalDate firstdate,@RequestParam(value = "dateBefore", required = false) LocalDate lastdate,@RequestParam(value = "category", required = false) String category, Model model) {

        if (firstdate == null && lastdate == null && (category == null || category.isBlank())) {
            LOGGER.error("ERRORS SEARCH");
            model.addAttribute("error", "no.search.value");
            return "redirect:/regatta/overview/1";
        }

        List<Regatta> allRegattas = regattaService.getAllRegattas("");
        if (allRegattas.isEmpty()) {
            createSampleData();
        }

        allRegattas = regattaService.filterDateAndCat(firstdate,lastdate,category);
        model.addAttribute("regattas", allRegattas);
        return "overview-regatta";
    }

    @GetMapping("/regatta/search/{page}")
    public String showSearchPage(@PathVariable("page") int page, @RequestParam(value = "dateAfter", required = false) LocalDate firstdate,@RequestParam(value = "dateBefore", required = false) LocalDate lastdate,@RequestParam(value = "category", required = false) String category, Model model) {

        return searchPage(page,firstdate,lastdate,category, model);

        }

    @PostMapping("/regatta/search/{page}")
    public String searchPage(@PathVariable("page") int page, @RequestParam(value = "dateAfter", required = false) LocalDate firstdate,@RequestParam(value = "dateBefore", required = false) LocalDate lastdate,@RequestParam(value = "category", required = false) String category, Model model) {

        if (firstdate == null && lastdate == null && (category == null || category.isBlank())) {
            LOGGER.error("ERRORS SEARCH");
            model.addAttribute("error", "no.search.value");
            return "redirect:/regatta/overview/1";
        }

        Page<Regatta> allRegattas = regattaService.getRegattaPage(page);
        if (allRegattas.isEmpty()) {
            createSampleData();
        }
        allRegattas = regattaService.filterDateAndCatPage(firstdate,lastdate,category,page-1);
        String date1 = "";
        String date2 = "";
        if(firstdate != null){date1 = firstdate.toString();}
        if(lastdate != null){date2 = lastdate.toString();}
        model.addAttribute("regattas", allRegattas);
        model.addAttribute("NOpages", listofInt(allRegattas.getTotalPages()));
        model.addAttribute("requestparam","?dateAfter=" + date1 + "&dateBefore=" + date2 + "&category=" + category);
        model.addAttribute("startdate", firstdate);
        model.addAttribute("enddate", lastdate);
        model.addAttribute("category", category);
        return "overview-regatta";
    }

    private List<Integer> listofInt(int q){
        int p = 1;
        List<Integer> qs = new ArrayList<>();
        while(p <= q){
            qs.add(p);
            p++;
        }
        return qs;
    }
}



