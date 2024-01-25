package be.busstop.domain.user.service;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.user.dto.UserReportDto;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserReport;
import be.busstop.domain.user.repository.UserReportRepository;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.SuccessCodeEnum;
import be.busstop.global.utils.S3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static be.busstop.global.stringCode.SuccessCodeEnum.USER_REPORT_SUCCESS;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserReportService {

    private final S3 s3Service;
    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;

    @Transactional
    public ApiResponse<?> reportUser(User reporter, Long reportedUserId, UserReportDto userReportDto, List<MultipartFile> images) {
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));

        List<String> imageUrlList = s3Service.uploads(images);
        UserReport userReport = new UserReport(reporter, reportedUserId, imageUrlList, userReportDto.getReport(),userReportDto.getReportDetail());
        reportedUser.increaseReportCount();
        userReportRepository.save(userReport);
        log.info("User {} reported user {}.", reporter.getId(), reportedUserId);
        return ApiResponse.okWithMessage(USER_REPORT_SUCCESS);
    }
}
