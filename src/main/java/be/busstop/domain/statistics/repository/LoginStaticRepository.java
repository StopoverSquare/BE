package be.busstop.domain.statistics.repository;

import be.busstop.domain.statistics.entity.LoginStatic;
import com.mysql.cj.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface LoginStaticRepository extends JpaRepository<LoginStatic, Long> {
    Optional<LoginStatic> findByDate(LocalDate date);
    List<LoginStatic> findAllByDateBetween(LocalDate start, LocalDate end);
    void deleteByDate(LocalDate date);
}
