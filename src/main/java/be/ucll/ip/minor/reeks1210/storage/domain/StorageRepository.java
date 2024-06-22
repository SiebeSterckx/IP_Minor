package be.ucll.ip.minor.reeks1210.storage.domain;

import be.ucll.ip.minor.reeks1210.regatta.domain.Regatta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface StorageRepository extends PagingAndSortingRepository<Storage, Long>, JpaRepository<Storage, Long> {

    @Override
    List<Storage> findAll();

    Page<Storage> findByNameContaining(PageRequest pageRequest, String name);

    void delete(Storage storage);

}
