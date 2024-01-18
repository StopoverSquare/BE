package be.busstop.domain.statistics.repository;

import be.busstop.domain.statistics.entity.CategoryStatic;
import be.busstop.domain.statistics.entity.GenderStatic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface GenderStaticRepository extends JpaRepository<GenderStatic, Long> {
    void deleteByDate(LocalDate date);
}
