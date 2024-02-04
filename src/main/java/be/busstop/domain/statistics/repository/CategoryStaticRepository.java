package be.busstop.domain.statistics.repository;

import be.busstop.domain.statistics.entity.CategoryStatic;
import be.busstop.domain.statistics.entity.LoginStatic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CategoryStaticRepository extends JpaRepository<CategoryStatic, Long> {
    void deleteByDate(LocalDate date);
    CategoryStatic findByDate(LocalDate date);
}
