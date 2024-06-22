package be.ucll.ip.minor.reeks1210.storage.controller;

import be.ucll.ip.minor.reeks1210.general.ServiceException;
import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import be.ucll.ip.minor.reeks1210.storage.domain.Storage;
import be.ucll.ip.minor.reeks1210.storage.domain.StorageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StorageController {

    public static Logger LOGGER = LoggerFactory.getLogger(StorageController.class);
    private final StorageService storageService;
    private int i = 0;

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



    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/storage/overview")
    public String overview (Model model) {

        List<Storage> allStorages = storageService.getAllStorages();

        if (allStorages.isEmpty() && i != 1) {
            createSampleData();
            allStorages = storageService.getAllStorages();
        }
        else {
            i = 1;
        }

        model.addAttribute("storages", allStorages);
        return "overview-storage";
    }

    @GetMapping("/storage/overview/{page}")
    public String overviewpage (@PathVariable("page") int page,Model model) {

        Page<Storage> allStorages = storageService.getStoragePage(page-1);

        if (allStorages.isEmpty() && i != 1) {
            createSampleData();
            allStorages = storageService.getStoragePage(page-1);
        }
        else {
            i = 1;
        }

        List<Integer> qs = listofInt(allStorages.getTotalPages());

        model.addAttribute("storages", allStorages);
        model.addAttribute("NOpages", qs);
        return "overview-storage";
    }


    public void createSampleData() {
        StorageDto storage1 = new StorageDto();
        storage1.setName("Opslag De Kuijper");
        storage1.setPostal("2030");
        storage1.setSurface("450");
        storage1.setHeight("12");

        StorageDto storage2 = new StorageDto();
        storage2.setName("Bie-Low");
        storage2.setPostal("3300");
        storage2.setSurface("525");
        storage2.setHeight("14");

        storageService.createStorage(storage1);
        storageService.createStorage(storage2);
        i = 1;
    }

    public List applySearchOnSort(List<Storage> allStorages, String searchValue, Model model) {
        model.addAttribute("searchValue", searchValue);
        return storageService.searchStorageInList(allStorages, searchValue);
    }

    @GetMapping("/storage/sort/reset")
    public String resetSort(@RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        if (searchValue == null || searchValue.isBlank()) {
            return "redirect:/storage/overview/1";
        } else {
            model.addAttribute("searchValue", searchValue);
            return showSearchResultStorage(searchValue, model);
        }
    }

    @GetMapping("/storage/sort/name")
    public String sortName(@RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        List<Storage> allStorages = storageService.sortByName();

        if (searchValue != null || searchValue.isBlank()) {
            allStorages = applySearchOnSort(allStorages, searchValue, model);
        }

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowName", "&#8681;");
        return "overview-storage";
    }

    @GetMapping("/storage/sort/name/{page}")
    public String sortNamePage(@PathVariable("page") int page,Model model) {
        Page<Storage> allStorages = storageService.getStoragePage(PageRequest.of(page-1, 4, Sort.by("name")));
        List<Integer> qs = listofInt(allStorages.getTotalPages());

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowName", "&#8681;");
        model.addAttribute("NOpages", qs);
        return "overview-storage";

    }

    @GetMapping("/storage/sort/gesorteerd/{page}")
    public String sortPage(@PathVariable("page") int page,Model model) {
        Page<Storage> allStorages = storageService.getStoragePage(PageRequest.of(page-1, 4, Sort.by("Postal").descending()));
        List<Integer> qs = listofInt(allStorages.getTotalPages());

        model.addAttribute("storages", allStorages);
        model.addAttribute("NOpages", qs);
        return "overview_storage_gesorteerd";
    }

    @GetMapping("/storage/sort/postal")
    public String sortPostal(@RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        List<Storage> allStorages = storageService.sortByPostal();

        if (searchValue != null || searchValue.isBlank()) {
            allStorages = applySearchOnSort(allStorages, searchValue, model);
        }

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowPostal", "&#8681;");
        return "overview-storage";
    }

    @GetMapping("/storage/sort/postal/{page}")
    public String sortPostalPage(@PathVariable("page") int page,Model model) {
        Page<Storage> allStorages = storageService.getStoragePage(PageRequest.of(page-1, 4, Sort.by("postal")));
        List<Integer> qs = listofInt(allStorages.getTotalPages());

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowPostal", "&#8681;");
        model.addAttribute("NOpages", qs);
        return "overview-storage";

    }


    @GetMapping("/storage/sort/surface")
    public String sortSurface(@RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        List<Storage> allStorages = storageService.sortBySurface();

        if (searchValue != null || searchValue.isBlank()) {
            allStorages = applySearchOnSort(allStorages, searchValue, model);
        }

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowSurface", "&#8681;");
        return "overview-storage";
    }

    @GetMapping("/storage/sort/surface/{page}")
    public String sortSurfacePage(@PathVariable("page") int page,Model model) {
        Page<Storage> allStorages = storageService.getStoragePage(PageRequest.of(page-1, 4, Sort.by("surface")));
        List<Integer> qs = listofInt(allStorages.getTotalPages());

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowSurface", "&#8681;");
        model.addAttribute("NOpages", qs);
        return "overview-storage";

    }

    @GetMapping("/storage/sort/height")
    public String sortHeight(@RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        List<Storage> allStorages = storageService.sortByHeight();

        if (searchValue != null || searchValue.isBlank()) {
            allStorages = applySearchOnSort(allStorages, searchValue, model);
        }


        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowHeight", "&#8681;");
        return "overview-storage";
    }

    @GetMapping("/storage/sort/height/{page}")
    public String sortHeightPage(@PathVariable("page") int page,Model model) {
        Page<Storage> allStorages = storageService.getStoragePage(PageRequest.of(page-1, 4, Sort.by("height")));
        List<Integer> qs = listofInt(allStorages.getTotalPages());

        model.addAttribute("storages", allStorages);
        model.addAttribute("arrowHeight", "&#8681;");
        model.addAttribute("NOpages", qs);
        return "overview-storage";

    }

    @GetMapping("/storage/add")
    public String add(Model model) {
        model.addAttribute("storageDto", new StorageDto());
        return "add-storage";
    }

    @PostMapping("/storage/add")
    public String add(@Valid StorageDto storage, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                return "add-storage";
            }

            storageService.createStorage(storage);
            return "redirect:/storage/overview/1";
        } catch (ServiceException e) {
            model.addAttribute("error", "height.invalid");
            return "add-storage";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "storage.unique");
            return "add-storage";
        }
    }

    @GetMapping("/storage/update/{id}")
    public String update(@PathVariable("id") long id, Model model) {
        try {
            Storage storage = storageService.getStorage(id);
            model.addAttribute("storageDto", toDto(storage));
        } catch (RuntimeException exc) {
            model.addAttribute("error", exc.getMessage());
        }

        return "update-storage";
    }

    private StorageDto toDto(Storage storage) {
        StorageDto dto = new StorageDto();

        dto.setId(storage.getId());
        dto.setName(storage.getName());
        dto.setPostal(storage.getPostal());
        dto.setSurface(storage.getSurface());
        dto.setHeight(storage.getHeight());

        return dto;
    }

    @PostMapping("/storage/update/{id}")
    public String update(@PathVariable("id") long id, @Valid StorageDto storageDto, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                LOGGER.error("ERRORS UPDATING");

                storageDto.setId(id);
                model.addAttribute("storageDto", storageDto);
                return "update-storage";
            }

            storageService.updateStorage(id, storageDto);
            return "redirect:/storage/overview/1";
        } catch (ServiceException e) {
            model.addAttribute("error", "height.invalid");
            return "update-storage";
        }
        catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "storage.unique");
            return "update-storage";
        }
    }



    @GetMapping("/storage/delete")
    public String deletePage(@RequestParam("id") long id, Model model) {
        Storage storage = storageService.getStorage(id);
        model.addAttribute("storage", storage);

        return "delete-storage";
    }

    @PostMapping("/storage/delete")
    public String deleteStorage(@RequestParam("id") long id) {
        storageService.deleteStorage(id);

        return "redirect:/storage/overview/1";
    }

    @GetMapping("/storage/search")
    public String showSearchStorage(@RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        if (searchValue != null && !searchValue.isBlank()) {
            return showSearchResultStorage(searchValue, model);
        } else {
            return "overview-storage";
        }
    }

    @GetMapping("/storage/search/{page}")
    public String showSearchStoragePage(@PathVariable("page") int page, @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        if (searchValue != null && !searchValue.isBlank()) {
            return showSearchResultStoragePage(page,searchValue, model);
        } else {
            return "overview-storage";
        }
    }

    @PostMapping("/storage/search")
    public String showSearchResultStorage(@RequestParam("searchValue") String searchValue, Model model) {
        if (searchValue == null || searchValue.isBlank()) {
            LOGGER.error("ERRORS SEARCH");
            model.addAttribute("error", "no.search.value");
            return "redirect:/storage/overview/1";
        }

        List<Storage> storages = storageService.searchStorage(searchValue);
        model.addAttribute("storages", storages);
        model.addAttribute("searchValue", searchValue);
        return "overview-storage";
    }

    @PostMapping("/storage/search/{page}")
    public String showSearchResultStoragePage(@PathVariable("page") int page, @RequestParam("searchValue") String searchValue, Model model) {
        if (searchValue == null || searchValue.isBlank()) {
            LOGGER.error("ERRORS SEARCH");
            model.addAttribute("error", "no.search.value");
            return "redirect:/storage/overview/1";
        }

        Page<Storage> storages = storageService.searchStoragePage(searchValue,page-1);
        model.addAttribute("storages", storages);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("NOpages", listofInt(storages.getTotalPages()));

        model.addAttribute("requestparam","?searchValue=" + searchValue);
        return "overview-storage";
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
