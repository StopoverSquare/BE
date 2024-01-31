package be.busstop.domain.user.service;

import be.busstop.domain.user.dto.*;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserReport;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserReportRepository;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.ErrorCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

import static be.busstop.global.stringCode.ErrorCodeEnum.NOT_ACCESS;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminPageService {


    private final UserRepository userRepository;
    private final UserReportRepository userReportRepository;
    @Transactional(readOnly = true)
    public ApiResponse<Page<UserResponseDto>> userList(User user,
                                                       @RequestParam(required = false) String nickname,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        validateAdminRole(user);

        Page<User> userPage;

        if (StringUtils.hasText(nickname)) {
            // 닉네임이 제공된 경우 해당 닉네임으로 사용자를 검색
            Pageable pageable = PageRequest.of(page, size);
            userPage = userRepository.findByNicknameContainingIgnoreCaseAndRole(nickname, UserRoleEnum.USER, pageable);
        } else {
            // 닉네임이 제공되지 않은 경우 전체 사용자 목록 반환
            Pageable pageable = PageRequest.of(page, size);
            userPage = userRepository.findAllByUser(UserRoleEnum.USER, pageable);
        }

        Page<UserResponseDto> userResponseDtoPage = userPage.map(UserResponseDto::new);

        return ApiResponse.success(userResponseDtoPage);
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<UserResponseDto>> userBlackList(User user,
                                                            @RequestParam(required = false) String nickname,
                                                            @RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        validateAdminRole(user);

        Page<User> blackUserPage;

        if (StringUtils.hasText(nickname)) {
            // 닉네임이 제공된 경우 해당 닉네임으로 사용자를 검색
            Pageable pageable = PageRequest.of(page, size);
            blackUserPage = userRepository.findByNicknameContainingIgnoreCaseAndRole(nickname, UserRoleEnum.BLACK, pageable);
        } else {
            // 닉네임이 제공되지 않은 경우 전체 사용자 목록 반환
            Pageable pageable = PageRequest.of(page, size);
            blackUserPage = userRepository.findAllByBlackUser(UserRoleEnum.BLACK, pageable);
        }

        Page<UserResponseDto> userResponseDtoPage = blackUserPage.map(UserResponseDto::new);

        return ApiResponse.success(userResponseDtoPage);
    }

    @Transactional
    public ApiResponse<?> makeUserAdmin(User user, Long userId) {
        if (user.getRole() != UserRoleEnum.SUPER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
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
    @Transactional
    public ApiResponse<?> makeUser(User user, Long userId) {
        validateAdminRole(user);
        User user1 = findUserById(userId);
        user1.setRoleUser();
        userRepository.save(user1);
        return ApiResponse.success(user1.getNickname() + " 유저의 제한을 해제하였습니다.");
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

    public ApiResponse<?> searchNickname(String nickname) {
        List<User> allUsers = userRepository.findAll();
        List<SearchResponseDto> searchUsers = new ArrayList<>();
        for(User user : allUsers){
            if(user.getNickname() != null && user.getNickname().contains(nickname)){
                searchUsers.add(SearchResponseDto.builder()
                                .nickname(user.getNickname())
                                .age(user.getAge())
                                .gender(user.getGender())
                                .profileImg(user.getProfileImageUrl())
                                .role(user.getRole())
                                .build());
            }
        }
        return ApiResponse.success(searchUsers);
    }

    public ApiResponse<?> changeRoleToAdmin(User user, NicknameRequestDto nickname){
        if(user.getRole() != UserRoleEnum.SUPER){
            throw new InvalidConditionException(NOT_ACCESS);
        }

        User changeUser = userRepository.findByNickname(nickname.getNickname()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다.") );
        changeUser.setRoleAdmin();
        return ApiResponse.success("해당 사용자의 권한을 ADMIN으로 변경하였습니다.");
    }
    public ApiResponse<?> changeRoleToUser(User user, NicknameRequestDto nickname) {
        if(user.getRole() != UserRoleEnum.SUPER){
            throw new InvalidConditionException(NOT_ACCESS);
        }

        User changeUser = userRepository.findByNickname(nickname.getNickname()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다.") );
        changeUser.setRoleUser();
        return ApiResponse.success("해당 사용자의 권한을 USER로 변경하였습니다.");
    }
    public ApiResponse<?> getAllAdmin() {
        List<User> allUsers = userRepository.findAll();
        List<AdminResponseDto> adminUsers = new ArrayList<>();
        for(User user : allUsers){
            if(user.getRole() == UserRoleEnum.SUPER){
                adminUsers.add(AdminResponseDto.builder()
                                .isSuper(true)
                                .age(user.getAge())
                                .gender(user.getGender())
                                .profileImg(user.getProfileImageUrl())
                                .nickname(user.getNickname())
                                .build());
            }else if(user.getRole() == UserRoleEnum.ADMIN){
                adminUsers.add(AdminResponseDto.builder()
                                .isSuper(false)
                                .age(user.getAge())
                                .gender(user.getGender())
                                .profileImg(user.getProfileImageUrl())
                                .nickname(user.getNickname())
                                .build());
            }
        }
        return ApiResponse.success(adminUsers);
    }

}
