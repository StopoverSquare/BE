package be.busstop.domain.user.service;

import be.busstop.domain.user.dto.UserReportResponseDto;
import be.busstop.domain.user.dto.UserResponseDto;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserReport;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserReportRepository;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminPageService {


    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;

    public ApiResponse<?> userList(User user) {
        validateAdminRole(user);
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = userList.stream().map(UserResponseDto::new).toList();

        return ApiResponse.success(userResponseDtoList);
    }

    public ApiResponse<?> userBlackList(User user) {
        validateAdminRole(user);
        List<User> blackUserList = userRepository.findAllByBlackUser(UserRoleEnum.BLACK);
        List<UserResponseDto> userResponseDtoList = blackUserList.stream().map(UserResponseDto::new).toList();

        return ApiResponse.success(userResponseDtoList);
    }

    public ApiResponse<?> makeUserAdmin(User user, Long userId) {
        validateAdminRole(user);
        User admin = findUserById(userId);
        user.setRole(UserRoleEnum.ADMIN);
        userRepository.save(admin);
        return ApiResponse.success(admin.getNickname() + "유저의 권한을 ADMIN으로 변경하였습니다.");
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.USER_NOT_EXIST));
    }

    public ApiResponse<?> makeUserBlack(User user, Long userId) {
        validateAdminRole(user);
        User black = findUserById(userId);
        user.setRole(UserRoleEnum.BLACK);
        userRepository.save(black);
        return ApiResponse.success(black.getNickname() + " 유저의 권한을 제한 하였습니다.");
    }

    public ApiResponse<?> makeUser(User user,Long userId) {
        validateAdminRole(user);
        User user1 = findUserById(userId);
        user.setRole(UserRoleEnum.USER);
        userRepository.save(user1);
        return ApiResponse.success(user1.getNickname() + " 유저의 권한을 활성화 하였습니다.");
    }

    @Transactional
    public List<UserReportResponseDto> searchAllUserReports(User user) {
        validateAdminRole(user);

        List<UserReport> userReportEntities = userReportRepository.findAll();

        return new ArrayList<>(userReportEntities.stream()
                .collect(Collectors.toMap(
                        UserReport::getReportedUserId,
                        userReport -> createUserReportDto(getUserById(userReport.getReportedUserId())),
                        (existing, replacement) -> existing
                ))
                .values());
    }


    @Transactional
    public UserReportResponseDto getUserReportDetail(User user, Long reportedUserId) {
        validateAdminRole(user);
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. ID: " + reportedUserId));

        Optional<UserReport> latestReport = userReportRepository.findTopByReportedUserIdOrderByCreatedAtDesc(reportedUserId);

        return latestReport.map(report ->
                new UserReportResponseDto(
                        reportedUser.getId(),
                        report.getReport(),
                        report.getImageUrlList(),
                        report.getCreatedAt()
                )
        ).orElse(null);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. ID: " + userId));
    }

    private void validateAdminRole(User user) {
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }

    private UserReportResponseDto createUserReportDto(User reportedUser) {
        return new UserReportResponseDto(
                reportedUser.getId(),
                reportedUser.getReportCount(),
                reportedUser.getNickname(),
                reportedUser.getAge(),
                reportedUser.getGender(),
                reportedUser.getProfileImageUrl()
        );
    }
}
