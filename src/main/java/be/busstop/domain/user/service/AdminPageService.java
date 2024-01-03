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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminPageService {


    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;
    @Transactional(readOnly = true)
    public ApiResponse<?> userList(User user, @RequestParam(required = false) String nickname) {
        validateAdminRole(user);

        List<User> userList;

        if (StringUtils.hasText(nickname)) {
            // 닉네임이 제공된 경우 해당 닉네임으로 사용자를 검색
            userList = userRepository.findByNicknameContainingIgnoreCase(nickname);
        } else {
            // 닉네임이 제공되지 않은 경우 전체 사용자 목록 반환
            userList = userRepository.findAll();
        }

        List<UserResponseDto> userResponseDtoList = userList.stream().map(UserResponseDto::new).toList();

        return ApiResponse.success(userResponseDtoList);
    }
    @Transactional(readOnly = true)
    public ApiResponse<?> userBlackList(User user, @RequestParam(required = false) String nickname) {
        validateAdminRole(user);

        List<User> blackUserList;

        if (StringUtils.hasText(nickname)) {
            // 닉네임이 제공된 경우 해당 닉네임으로 사용자를 검색
            blackUserList = userRepository.findByNicknameContainingIgnoreCaseAndRole(nickname,UserRoleEnum.BLACK);
        } else {
            // 닉네임이 제공되지 않은 경우 전체 사용자 목록 반환
            blackUserList = userRepository.findAllByBlackUser(UserRoleEnum.BLACK);
        }
        List<UserResponseDto> userResponseDtoList = blackUserList.stream().map(UserResponseDto::new).toList();

        return ApiResponse.success(userResponseDtoList);
    }
    @Transactional
    public ApiResponse<?> makeUserAdmin(User user, Long userId) {
        validateAdminRole(user);
        User admin = findUserById(userId);
        admin.setRoleAdmin();
        userRepository.save(admin);
        return ApiResponse.success(admin.getNickname() + "유저의 권한을 ADMIN으로 변경하였습니다.");
    }

    @Transactional
    public ApiResponse<?> makeUserBlack(User user, Long userId) {
        validateAdminRole(user);
        User black = findUserById(userId);
        black.setRoleBlack();
        userRepository.save(black);
        return ApiResponse.success(black.getNickname() + " 유저의 권한을 제한 하였습니다.");
    }
    @Transactional(readOnly = true)
    public List<UserReportResponseDto> searchAllUserReports(User user, String nickname) {
        validateAdminRole(user);

        List<UserReport> userReportEntities;
        if (StringUtils.hasText(nickname)) {
            userReportEntities = userReportRepository.findAllByReportedUserId(getUserIdByNickname(nickname));
        } else {
            userReportEntities = userReportRepository.findAll();
        }

        return new ArrayList<>(userReportEntities.stream()
                .collect(Collectors.toMap(
                        UserReport::getReportedUserId,
                        userReport -> createUserReportDto(getUserById(userReport.getReportedUserId())),
                        (existing, replacement) -> existing
                ))
                .values());
    }

    @Transactional(readOnly = true)
    public List<UserReportResponseDto> getUserReportDetails(User user, Long reportedUserId) {
        validateAdminRole(user);
        User reportedUser = userRepository.findById(reportedUserId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. ID: " + reportedUserId));

        List<UserReport> allReports = userReportRepository.findByReportedUserIdOrderByCreatedAtDesc(reportedUserId);

        return allReports.stream()
                .map(report ->
                        new UserReportResponseDto(
                                reportedUser.getId(),
                                report.getReport(),
                                report.getReportDetail(),
                                report.getImageUrlList(),
                                report.getCreatedAt()
                        )
                )
                .collect(Collectors.toList());
    }


    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.USER_NOT_EXIST));
    }

    private Long getUserIdByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. 닉네임: " + nickname));
        return user.getId();
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
