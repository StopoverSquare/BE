package be.busstop.domain.salvation.repository;

import be.busstop.domain.salvation.entity.Salvation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalvRepository extends JpaRepository<Salvation, Long>,SalvRepositoryCustom {

}
