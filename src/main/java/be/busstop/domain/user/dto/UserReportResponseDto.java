package be.busstop.domain.user.dto;

import be.busstop.domain.user.entity.UserReportEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReportResponseDto {

    private Long reportedUserId;
    private Enum report;
    private List<String> reportImages;
    private Integer userReportCount;
    private String nickname;
    private String age;
    private String gender;
    private String profileImageUrl;
    private LocalDateTime createdAt;

    public UserReportResponseDto(Long reportedUserId, Integer userReportCount,String profileImageUrl, String nickname,String age, String gender) {
        this.reportedUserId = reportedUserId;
        this.userReportCount = userReportCount;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
    }

    public UserReportResponseDto(Long reportedUserId, UserReportEnum report, List<String> reportImages, LocalDateTime createdAt) {
        this.reportedUserId = reportedUserId;
        this.report = report;
        this.reportImages = reportImages;
        this.createdAt = createdAt;
    }
}
