package be.ucll.ip.minor.reeks1210.storage.controller;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.boat.domain.BoatService;
import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.storage.domain.StorageService;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;


// RELATIONS
@RestController
@RequestMapping("/api/storage-boat")
public class StorageBoatRestController {

    @Autowired
    private StorageService storageService;
    @Autowired
    private BoatService boatService;

    // get http://localhost:8080/api/storage-boat/boats?storageId=8
    @GetMapping("/boats")
    public Set getBoatsByStorageId(@RequestParam("storageId") Long storageId) {
        return storageService.getBoatsByStorageId(storageId);
    }

    // post http://localhost:8080/api/storage-boat/add/boat/{boatId}/to/storage/{storageId}
    @PostMapping("/add/boat/{boatId}/to/storage/{storageId}")
    public Boat addBoatToStorage(@PathVariable("boatId") Long boatId, @PathVariable("storageId") Long storageId) {
        try {
            Boat boat = boatService.getBoat(boatId);
            storageService.addStorageBoat(storageId, boat);
            return boat;
        } catch (Exception e) {
            throw new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST   , e.getMessage(), e);
        }
    }

    // post http://localhost:8080/api/storage-boat/remove/boat/{boatId}/from/storage/{storageId}
    @PostMapping("/remove/boat/{boatId}/from/storage/{storageId}")
    public Boat removeBoatFromStorage(@PathVariable("boatId") Long boatId, @PathVariable("storageId") Long storageId) {
        try{
            Boat boat = boatService.getBoat(boatId);
            storageService.removeStorageBoat(storageId, boat);
            return boat;
        }catch(ServiceException e){throw new ServiceException(e.getAction(), e.getMessage());}
        catch (RuntimeException e){throw new ServiceException("Remove", "This boat or storage doesnt exist");
        }
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
