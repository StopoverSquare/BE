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
public class UserReport extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id")
    private User reporter;

    @Column
    private Long reportedUserId;

    @Enumerated(EnumType.STRING)
    private UserReportEnum report;

    @Column
    private String type = "User";

    @ElementCollection
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 5)
    @Column
    private List<String> imageUrlList = new ArrayList<>();

    public UserReport(User user,Long reportedUserId, List<String> imageUrlList, UserReportEnum report) {
        this.reporter = user;
        this.reportedUserId = reportedUserId;
        this.user = user;
        this.report = report;
        this.imageUrlList = imageUrlList;
    }
}

