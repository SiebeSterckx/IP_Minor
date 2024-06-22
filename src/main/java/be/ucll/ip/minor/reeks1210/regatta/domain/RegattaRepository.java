package be.ucll.ip.minor.reeks1210.regatta.domain;


import be.ucll.ip.minor.reeks1210.storage.domain.Storage;
import be.ucll.ip.minor.reeks1210.team.domain.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
interface RegattaRepository extends PagingAndSortingRepository<Regatta, Long>, JpaRepository<Regatta, Long>, CrudRepository<Regatta, Long> {
    Page<Regatta> findByDateBetweenAndCategoryContaining(PageRequest pageRequest, LocalDate date1, LocalDate date2, String category);
    Page<Regatta> findByDateAfterAndCategoryContaining(PageRequest pageRequest, LocalDate date, String category);
    Page<Regatta> findByCategoryContaining(PageRequest pageRequest, String category);

}
