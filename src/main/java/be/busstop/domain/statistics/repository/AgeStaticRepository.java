package be.busstop.domain.statistics.repository;

import be.busstop.domain.statistics.entity.AgeStatic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AgeStaticRepository extends JpaRepository<AgeStatic, Long> {
    Optional<AgeStatic> findByDate(LocalDate date);
}
