package be.busstop.domain.user.entity;

import be.busstop.domain.post.entity.Post;
import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_report")
public class UserReport extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Column
    private Long reportedUserId;

    @Enumerated(EnumType.STRING)
    private UserReportEnum report;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reportDetail;

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 5)
    @Column
    private List<String> imageUrlList = new ArrayList<>();

    public UserReport(User reporter,Long reportedUserId, List<String> imageUrlList, UserReportEnum report, String reportDetail) {
        this.reporter = reporter;
        this.reportedUserId = reportedUserId;
        this.report = report;
        this.reportDetail = reportDetail;
        this.imageUrlList = imageUrlList;
    }
}

