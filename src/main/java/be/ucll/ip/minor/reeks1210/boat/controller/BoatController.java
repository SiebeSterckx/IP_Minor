package be.ucll.ip.minor.reeks1210.boat.controller;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.boat.domain.BoatService;
import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import be.ucll.ip.minor.reeks1210.regatta.domain.RegattaService;
import be.ucll.ip.minor.reeks1210.team.controller.TeamDto;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/boat")
public class BoatController {


    // GET          => @GetMapping
    // POST         => @PostMapping
    // UPDATE (PUT) => @PutMapping
    // DELETE       => @DeleteMapping

    // URL (GET, UPDATE, DELETE)
    // @PathVariable -> /api/patient/delete/3
    // @RequestParam  -> /api/patient/delete?id=3


    // POST
    // @Valid
    // @RequestBody


    //App server draait op spring framework
    //Restcontroller geeft boat object terug aan de app server
    //App server zet boat object om naar JSON en stopt dit in de request body voor de response




    @Autowired
    private BoatService boatService;

    @GetMapping("/overview")
    public List<Boat> overview (Model model) {
        List<Boat> allBoats = boatService.getAllBoats();

        if (allBoats.isEmpty()) {
            createSampleData();
            allBoats = boatService.getAllBoats();
        }
        return allBoats;
    }

    public void createSampleData() {
        BoatDto boat1 = new BoatDto();
        boat1.setName("Papihou");
        boat1.setEmail("ahaha@aha.ha");
        boat1.setLength(450);
        boat1.setWidth(5);
        boat1.setHeight(12);
        boat1.setInsuranceNumber("123456789A");

//        BoatDto storage2 = new BoatDto();
//        storage2.setName("Bie-Low");
//        storage2.setPostal("3300");
//        storage2.setSurface(525);
//        storage2.setHeight(14);

        boatService.createBoat(boat1);
//        storageService.createStorage(storage2);
//        i = 1;
    }

//    @PostMapping("/add")
//    public Iterable<Boat> add(@Valid @RequestBody BoatDto boat) {
//        boatService.createBoat(boat);
//        return boatService.getAllBoats();
//    }

     @PostMapping("/add")
        public Iterable<Boat> add(@Valid @RequestBody BoatDto boat) {
            boatService.createBoat(boat);
            return boatService.searchBoatunique(boat.getName(), boat.getEmail(), boat.getInsuranceNumber());
        }


    // Pathvariabele id wordt in variabele id gestoken
    // Met de inhoud van de request body wordt een nieuw object geprobeerd aan te maken
    @PutMapping("/update/{id}")
    public Iterable<Boat> updateBoat(@PathVariable("id") Long id, @Valid @RequestBody BoatDto boatDto) {
        boatService.updateBoat(id, boatDto);
        return boatService.getAllBoats();
    }


    @DeleteMapping("/delete/{id}")
    public void deleteBoat(@PathVariable("id") Long id) {
        try {
            boatService.deleteBoat(id);
        }
        catch(ServiceException e){throw new ServiceException(e.getAction(), e.getMessage());}
        catch (RuntimeException e) {
            throw new ServiceException("Delete", "boat with this id doesn't exist");
        }
    }


    @GetMapping("/search")
    public Iterable<Boat> searchBoatByInsuranceNumber(@RequestParam(value = "insurance") String insurance, Model model) {
        return boatService.searchBoatByInsuranceNumber(insurance);
    }



    @GetMapping("/search/{height}/{width}")
    public Iterable<Boat> searchBoatByHeightAndWidth(@PathVariable("height") int height,@PathVariable("width") int width , Model model) {
        return boatService.searchBoatByHeightAndWidth(height,width);
    }


/*
    @GetMapping("/search")
    public List<Boat> showSearchStorage(@RequestParam(value = "insurance") String insurance, Model model) {
        try {
//            if (insurance == null || insurance.isBlank()) {
//                LOGGER.error("ERRORS SEARCH");
//                throw new RuntimeException("No insurance given");
//            }

            List<Boat> boats = boatService.searchBoatInsurance(insurance);
            return boats;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/search/{height}/{width}")
    public List<Boat> showSearchStorage(@PathVariable("height") long height,@PathVariable("width") long width , Model model) {
        try{
            List<Boat> boats = boatService.searchBoatHW(height,width);
            return boats;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
 */



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
