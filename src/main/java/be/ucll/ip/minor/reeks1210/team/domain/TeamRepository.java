package be.ucll.ip.minor.reeks1210.team.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long>, CrudRepository<Team, Long> {

    @Query("SELECT t FROM Team t WHERE t.passengers < :#{#number}")
    Iterable<Team> findAllByOccupants(int number);


    Iterable<Team> findAllByCategoryContainingIgnoreCase(String category);
}
