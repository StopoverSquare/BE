package be.busstop.domain.salvation.service;

import be.busstop.domain.salvation.dto.SalvRequestDto;
import be.busstop.domain.salvation.dto.SalvResponseDto;
import be.busstop.domain.salvation.dto.SalvSearchCondition;
import be.busstop.domain.salvation.entity.Salvation;
import be.busstop.domain.salvation.repository.SalvRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
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
import static be.busstop.global.stringCode.SuccessCodeEnum.POST_CREATE_SUCCESS;
import static be.busstop.global.stringCode.SuccessCodeEnum.POST_DELETE_SUCCESS;
import static be.busstop.global.utils.ResponseUtils.ok;
import static be.busstop.global.utils.ResponseUtils.okWithMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class SalvService {
    private final S3 s3;
    private final SalvRepository salvRepository;

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
    public ApiResponse<?> createSalvation(SalvRequestDto salvRequestDto, List<MultipartFile> images) {
        List<String> imageUrlList = s3.uploads(images);
        salvRequestDto.setImageUrlList(imageUrlList);
        salvRepository.save(new Salvation(salvRequestDto, imageUrlList));
        return ResponseUtils.okWithMessage(POST_CREATE_SUCCESS);
    }

    @Transactional
    public ApiResponse<?> deleteSalvation(Long salvId, User user) {
        validateAdminRole(user);
        Salvation salvation = confirmSalv(salvId);
        deleteImage(salvation);
        salvRepository.delete(salvation);
        log.info("'{}'님이 게시물 ID '{}'를 삭제했습니다.", user.getNickname(), salvId);
        return okWithMessage(POST_DELETE_SUCCESS);
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
        if (user.getRole() != UserRoleEnum.ADMIN) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
