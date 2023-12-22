package be.busstop.domain.user.repository;

import be.busstop.domain.user.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReportRepository extends JpaRepository<UserReport,Long> {

}
