package be.ucll.ip.minor.reeks1210.boat.domain;

import be.ucll.ip.minor.reeks1210.boat.controller.BoatDto;
import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.storage.domain.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private StorageService storageService;

    public List<Boat> getAllBoats() {
        return boatRepository.findAll();
    }

    public Boat getBoat(long id) {
        return boatRepository.findById(id).orElseThrow(() -> new RuntimeException("Boat with id " + id + " not found"));
    }

    public Boat createBoat(BoatDto dto) {
        try{
        Boat boat = new Boat();

        boat.setName(dto.getName());
        boat.setEmail(dto.getEmail());
        boat.setLength(dto.getLength());
        boat.setWidth(dto.getWidth());
        boat.setHeight(dto.getHeight());
        boat.setInsuranceNumber(dto.getInsuranceNumber());

        return boatRepository.save(boat);
        }catch(RuntimeException e){throw new ServiceException("AddBoat", "boat already exists");}
    }

    public Boat updateBoat(Long id, BoatDto dto) {
        Boat boat = new Boat();
        try {
            boat = getBoat(id);
        }catch (RuntimeException e){
            throw new ServiceException("Update", "Boat with this id doesn't exist");
        }
        boat.setName(dto.getName());
        boat.setEmail(dto.getEmail());
        boat.setLength(dto.getLength());
        boat.setWidth(dto.getWidth());
        boat.setHeight(dto.getHeight());
        boat.setInsuranceNumber(dto.getInsuranceNumber());


        if(boat.getStorage() != null){
            boat.boatFitsInStorage();
        }
        try{
            return boatRepository.save(boat);
        }catch (RuntimeException e){throw new ServiceException("Update", "These values are already used by a different boat");}
    }

    public void deleteBoat(Long id){
        Boat boat = getBoat(id);
        if (boat.getStorage() != null) {
            long storageID = boat.getStorage().getId();
            storageService.removeStorageBoat(storageID, boat);
        }
        boatRepository.delete(boat);
    }




    public List<Boat> searchBoatInsurance(String insuranceNumber) {
        List<Boat> boats = new ArrayList<>();
        for (Boat s : getAllBoats()) {
            if (s.getInsuranceNumber().toLowerCase().equals(insuranceNumber.toLowerCase())) {
                boats.add(s);
            }
        }
        return boats;
    }
    public Boat searchBoatID(long id) {
        for (Boat s : getAllBoats()) {
            if (s.getId() == id) {
                return s;
            }
        }
        throw new RuntimeException("boot bestaat niet");
    }

    public List<Boat> searchBoatunique(String name, String email, String insurancenumber) {
        for (Boat s : getAllBoats()) {
            if (s.getName().equals(name) && s.getEmail().equals(email) && s.getInsuranceNumber().equals(insurancenumber)) {
                List<Boat> x= new ArrayList<>();
                x.add(s);
                return x;
            }
        }
        throw new RuntimeException("boot bestaat niet");
    }


    public List<Boat> searchBoatHW(double height, double width) {
        List<Boat> boats = new ArrayList<>();
        for (Boat s : getAllBoats()) {
            if (s.getHeight() == height && s.getWidth() == width) {
                boats.add(s);
            }
        }
        return boats;
    }

    public Iterable<Boat> searchBoatByInsuranceNumber(String insuranceNumber) {
        return boatRepository.findAllByInsuranceNumber(insuranceNumber);
    }

    public Iterable<Boat> searchBoatByHeightAndWidth(int height, int width) {
        return boatRepository.findAllByHeightAndAndWidth(height,width);
    }
}