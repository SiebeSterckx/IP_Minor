package be.ucll.ip.minor.reeks1210.boat.domain;

import be.ucll.ip.minor.reeks1210.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoatRepository extends JpaRepository<Boat, Long> {


    Iterable<Boat> findAllByInsuranceNumber(String number);

    @Query("SELECT b FROM Boat b WHERE b.height = :#{#height} AND b.width = :#{#width} ")
    Iterable<Boat> findAllByHeightAndAndWidth(int height, int width);



}
