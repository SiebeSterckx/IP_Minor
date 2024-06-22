package be.ucll.ip.minor.reeks1210.storage.domain;

import be.ucll.ip.minor.reeks1210.boat.domain.Boat;
import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.storage.controller.StorageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class StorageService {

    @Autowired
    private StorageRepository storageRepository;

    public List<Storage> getAllStorages() {
        return storageRepository.findAll();
    }

    public Page<Storage> getStoragePage(int page) {
        PageRequest firstPageWithTwoElements = PageRequest.of(page, 4);
        return storageRepository.findAll(firstPageWithTwoElements);
    }

    public Page<Storage> getStoragePage(PageRequest pageRequest) {
        return storageRepository.findAll(pageRequest);
    }

    public Storage getStorage(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new RuntimeException("Storage with id " + id + " not found"));
    }

    public Storage createStorage(StorageDto dto) {
        Storage storage = new Storage();

        storage.setName(dto.getName());
        storage.setPostal(dto.getPostal());
        storage.setSurface(dto.getSurface());
        storage.setHeight(dto.getHeight());

        return storageRepository.save(storage);
    }

    public Storage updateStorage(Long id, StorageDto dto) {
        Storage storage = getStorage(id);

        storage.setId(dto.getId());
        storage.setName(dto.getName());
        storage.setPostal(dto.getPostal());
        storage.setSurface(dto.getSurface());
        storage.setHeight(dto.getHeight());

        if(!storage.getBoats().isEmpty()){
            for(Boat boat: storage.getBoats()){
                boat.boatFitsInStorage();
            }
        }

        storageRepository.save(storage);
        return storage;
    }

    public void deleteStorage(Long id){
        Storage storage = getStorage(id);
        if (storage.getBoats() != null) {
            Set<Boat> boats = storage.getBoats();
            for (Boat b : boats) {
                removeStorageBoat(storage.getId(),b);
            }
        }
        storageRepository.delete(storage);
    }

    public List<Storage> searchStorage(String searchValue) {
        List<Storage> storages = new ArrayList<>();
        searchValue = searchValue.toLowerCase();
        for (Storage s : getAllStorages()) {
            if (s.getName().toLowerCase().contains(searchValue)) {
                storages.add(s);
            }
        }
        return storages;
    }

    public Page<Storage> searchStoragePage(String searchValue, int page) {

        return storageRepository.findByNameContaining(PageRequest.of(page,4),searchValue);
    }

    public List<Storage> sortByName() {
        return storageRepository.findAll((Sort.by(Sort.Direction.ASC, "name")));
    }

    public List<Storage> sortByHeight() {
        return storageRepository.findAll(Sort.by(Sort.Direction.DESC, "height"));
    }

    public List<Storage> sortByPostal() {
        return storageRepository.findAll(Sort.by(Sort.Direction.ASC, "postal"));
    }

    public List<Storage> sortBySurface() {
        return storageRepository.findAll(Sort.by(Sort.Direction.DESC, "surface"));
    }

    public List searchStorageInList(List<Storage> allStorages, String searchValue) {
        List<Storage> storages = new ArrayList<>();
        searchValue = searchValue.toLowerCase();
        for (Storage s : allStorages) {
            if (s.getName().toLowerCase().contains(searchValue)) {
                storages.add(s);
            }
        }
        return storages;
    }

    // RELATIONS
    public Storage addStorageBoat(Long id, Boat boat) {
        Storage storage = getStorage(id);

        // check if the boat is already in another storage
        if (boat.getStorage() != null)
            throw new ServiceException("AddBoat","Boat is already in another storage");

        // check if the owner (with email) is already in the storage
        for (Boat b : storage.getBoats()) {
            if (b.getEmail().equals(boat.getEmail()))
                throw new ServiceException("AddBoat","This owner has already a boat in this storage");
        }

        if (!checkIfBoatFits(storage, boat))
            throw new ServiceException("AddBoat","Boat does not fit in this storage");

        boat.setStorage(storage);
        storage.addBoat(boat);
        return storageRepository.save(storage);
    }

    public Boolean checkIfBoatFits(Storage storage, Boat boat) {
        // check if the boat is not too high for the storage
        if (Double.parseDouble(storage.getHeight()) < boat.getHeight())
            throw new ServiceException("Boat_Fits_In_Storage","Boat is too high for this storage");

        // calculate the used surface of the storage
        Double usedSurface = 0.0;
        for (Boat b : storage.getBoats()) {
            Double boatSurface = b.getWidth() * b.getLength();
            usedSurface += boatSurface;
        }

        // check if the usedSurface is not bigger than 80% of the storage surface
        if (usedSurface + (boat.getWidth() * boat.getLength()) > (Double.parseDouble(storage.getSurface()) * 0.8))
            throw new ServiceException("Boat_Fits_In_Storage","Boat is too big for this storage");

        return true;
    }

    public Boolean checkIfStorageIsLargeEnough(Storage storage) {
        // check if all the boats still fit in the storage
        Double usedSurface = 0.0;
        for (Boat b : storage.getBoats()) {
            Double boatSurface = b.getWidth() * b.getLength();
            usedSurface += boatSurface;
        }
        return usedSurface < (Double.parseDouble(storage.getSurface()) * 0.8);
    }

    // RELATIONS
    public Storage removeStorageBoat(Long id, Boat boat) {
        Storage storage = getStorage(id);
        if(boat.getStorage() == null){throw new ServiceException("Remove", "Boat not in Storage");}
        boat.setStorage(null);
        storage.getBoats().remove(boat);
        return storageRepository.save(storage);
    }

    // RELATIONS
    public Set getBoatsByStorageId (Long id) {
        try{
        Storage storage = getStorage(id);
        return storage.getBoats();}
        catch(RuntimeException e){throw new ServiceException("search", "Storage with this id doesn't exist");}
    }
}
