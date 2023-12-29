package be.busstop.domain.user.repository;

import be.busstop.domain.user.entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport,Long> {

    Optional<UserReport> findTopByReportedUserIdOrderByCreatedAtDesc(Long reportedUserId);

    List<UserReport> findAllByReportedUserId(Long reportedUserId);
}
