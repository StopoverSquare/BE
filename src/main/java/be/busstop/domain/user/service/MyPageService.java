package be.busstop.domain.user.service;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.user.dto.mypage.MypageRequestDto;
import be.busstop.domain.user.dto.mypage.MypageResponseDto;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.stringCode.SuccessCodeEnum;
import be.busstop.global.utils.ResponseUtils;
import be.busstop.global.utils.S3;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {

    private final UserRepository userRepository;
    private final KakaoService kakaoService;
    private final S3 s3;

    @Transactional
    public MypageResponseDto getCurrentUserDetails(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Post> userPosts = userRepository.findPostsByUserId(userId);
            boolean isPopularity = false;
            // 조회한 정보를 DTO로 변환하여 리턴합니다.
            MypageResponseDto responseDto = new MypageResponseDto(
                    user.getId(),
                    user.getNickname(),
                    user.getAge(),
                    user.getGender(),
                    user.getProfileImageUrl(),
                    userPosts
            );
            return responseDto;
        } else {
            // 사용자를 찾지 못한 경우 에러 응답을 리턴합니다.
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public ApiResponse<?> updateUserProfile(Long userId, MypageRequestDto mypageRequestDto,
                                            User user, HttpServletResponse response) {
        log.info("'{}'님이 프로필 정보와 이미지를 변경했습니다.", user.getNickname());

        User existingUser = findUser(userId);

        if (!existingUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("동일한 사용자가 아닙니다.");
        }

        if (mypageRequestDto.getNickname() != null) {
            existingUser.updateNickname(mypageRequestDto.getNickname());
        }

        existingUser.update(mypageRequestDto);

        kakaoService.addToken(existingUser, response);

        // 프로필 정보와 이미지 변경에 대한 성공 응답을 반환합니다.
        return ResponseUtils.okWithMessage(SuccessCodeEnum.USER_USERDATA_UPDATA_SUCCESS);
    }


    public ApiResponse<?> updateUserProfileImage(Long userId, MultipartFile image, User user, HttpServletResponse response) {
        User existingUser = findUser(userId);

        if (!existingUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("동일한 사용자가 아닙니다.");
        }

        updateUserImageDetail(image, existingUser);
        userRepository.save(existingUser);

        kakaoService.addToken(existingUser, response);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.USER_USERDATA_UPDATA_SUCCESS);
    }
    private void updateUserImageDetail(MultipartFile image, User user) {
        String imageUrl = s3.upload(image);
        user.setProfileImageUrl(imageUrl);
        // 기존 게시물 프로필 이미지 유지를 위해 기존 이미지 삭제 X
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
