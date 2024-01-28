package be.busstop.domain.salvation.service;

import be.busstop.domain.salvation.dto.SalvRequestDto;
import be.busstop.domain.salvation.dto.SalvResponseDto;
import be.busstop.domain.salvation.dto.SalvSearchCondition;
import be.busstop.domain.salvation.entity.Salvation;
import be.busstop.domain.salvation.repository.SalvRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.ErrorCodeEnum;
import be.busstop.global.utils.ResponseUtils;
import be.busstop.global.utils.S3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static be.busstop.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;
import static be.busstop.global.stringCode.ErrorCodeEnum.USER_NOT_EXIST;
import static be.busstop.global.stringCode.SuccessCodeEnum.*;
import static be.busstop.global.utils.ResponseUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalvService {
    private final S3 s3;
    private final SalvRepository salvRepository;
    private final UserRepository userRepository;

    public ApiResponse<?> searchSalvation(User user, SalvSearchCondition condition, Pageable pageable) {
        validateAdminRole(user);
        return ok(salvRepository.searchSalvationByPage(condition, pageable));
    }

    @Transactional(readOnly = true)
    public ApiResponse<?> getSingleSalvation(User user, Long salvId) {
        validateAdminRole(user);
        boolean isView = false;
        Salvation salvation = salvRepository.findById(salvId)
                .orElseThrow(() -> new InvalidConditionException(POST_NOT_EXIST));
        // 강제로 세션 초기화
        salvation.getImageUrlList().size();

        return ok(new SalvResponseDto(salvation, isView));
    }

    @Transactional
    public ApiResponse<?> createSalvation(SalvRequestDto salvRequestDto) {
        if (salvRepository.existsByTitle(salvRequestDto.getTitle())) {

            return error("이미 구제신청을 하였습니다.",400);
        }

        // 새로운 Salvation 생성
        User user = userRepository.findByNickname(salvRequestDto.getTitle())
                .orElseThrow(() -> new InvalidConditionException(USER_NOT_EXIST));
        salvRepository.save(new Salvation(salvRequestDto, user.getId()));

        return okWithMessage(POST_SALVATION_SUCCESS);
    }


    @Transactional
    public ApiResponse<?> salvationOk(Long salvId, User user) {
        validateAdminRole(user);
        Salvation salvation = confirmSalv(salvId);
        deleteImage(salvation);
        makeUser(user,salvation.getUserId());
        salvRepository.delete(salvation);
        log.info("'{}'님이 게시물 ID '{}'를 삭제했습니다.", user.getNickname(), salvId);
        return okWithMessage(SALVATION_SUCCESS);
    }

    @Transactional
    public void makeUser(User user, Long userId) {
        validateAdminRole(user);
        User user1 = findUserById(userId);
        user1.setRoleUser();
        userRepository.save(user1);
        ApiResponse.success(user1.getNickname() + " 유저의 권한을 활성화 하였습니다.");
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.USER_NOT_EXIST));
    }

    private void deleteImage(Salvation salvation) {
        List<String> imageUrlList = salvation.getImageUrlList();
        if (StringUtils.hasText(String.valueOf(imageUrlList))) {
            s3.delete(imageUrlList);
        }
    }

    private Salvation confirmSalv(Long salvId) throws InvalidConditionException {
        Salvation salvation = findSalvation(salvId);
        return salvation;
    }

    private Salvation findSalvation(Long salvId) {
        return salvRepository.findById(salvId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private void validateAdminRole(User user) {
        if (user.getRole() != UserRoleEnum.ADMIN && user.getRole() != UserRoleEnum.SUPER) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
