package be.busstop.domain.post.service;

import be.busstop.domain.chat.entity.ChatRoomEntity;
import be.busstop.domain.chat.repository.ChatRoomRepository;
import be.busstop.domain.chat.service.ChatService;
import be.busstop.domain.notification.service.NotificationService;
import be.busstop.domain.notification.util.AlarmType;
import be.busstop.domain.post.dto.PostRequestDto;
import be.busstop.domain.post.dto.PostResponseDto;
import be.busstop.domain.post.dto.PostSearchCondition;
import be.busstop.domain.post.entity.Post;
import be.busstop.domain.post.entity.PostApplicant;
import be.busstop.domain.post.repository.PostRepository;
import be.busstop.domain.poststatus.entity.Status;
import be.busstop.domain.poststatus.repository.PostStatusRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.entity.UserRoleEnum;
import be.busstop.domain.user.repository.UserRepository;
import be.busstop.global.exception.InvalidConditionException;
import be.busstop.global.responseDto.ApiResponse;
import be.busstop.global.security.jwt.JwtUtil;
import be.busstop.global.utils.ResponseUtils;
import be.busstop.global.utils.S3;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static be.busstop.global.stringCode.ErrorCodeEnum.*;
import static be.busstop.global.stringCode.SuccessCodeEnum.POST_CREATE_SUCCESS;
import static be.busstop.global.stringCode.SuccessCodeEnum.POST_DELETE_SUCCESS;
import static be.busstop.global.utils.ResponseUtils.ok;
import static be.busstop.global.utils.ResponseUtils.okWithMessage;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostStatusRepository postStatusRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final JwtUtil jwtUtil;
    private final S3 s3;

    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return ok(postRepository.searchPostByPage(condition, pageable));
    }

    @Transactional
    public ApiResponse<?> getSinglePost(Long postId, HttpServletRequest req) {
        String token = jwtUtil.getTokenFromHeader(req);
        String subStringToken;
        boolean isComplete = false;
        if (token != null) {
            subStringToken = jwtUtil.substringHeaderToken(token);
            Claims userInfo = jwtUtil.getUserInfoFromToken(subStringToken);
            Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("?"));
            User user = userRepository.findByNickname(userInfo.getSubject()).orElseThrow(() -> new IllegalArgumentException("?"));

            if (postStatusRepository.findByPostAndUser(post, user).isPresent()) {
                isComplete = true;
            }
        }
        Post post = postRepository.findDetailPostWithParticipants(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));

        log.info("게시물 ID '{}' 조회 성공", postId);
        post.increaseViews();

        // 채팅방 참여자의 닉네임 가져오기
        List<String> chatParticipants = getChatParticipants(post.getChatroomId());
        List<PostApplicant> applicants = getApplicants(post.getId());

        return ok(new PostResponseDto(post, isComplete, chatParticipants, applicants));
    }



    private List<String> getChatParticipants(String chatRoomId) {
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        if (chatRoom != null) {
            return chatRoom.getChatRoomParticipants().stream()
                    .map(participant -> {
                        String userInfo = participant.getNickname() +
                                ", " +
                                participant.getAge() + ", " +
                                participant.getGender();
                        return userInfo;
                    })
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @Transactional
    public ApiResponse<?> approveOrDenyParticipant(User actionUser, Long postId, Long userId, boolean isApprove) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 정보입니다."));

        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자 정보입니다."));

        // 확인: 요청한 사용자가 게시글 작성자인지 확인
        if (!post.getUser().getNickname().equals(actionUser.getNickname())) {
            throw new IllegalArgumentException("게시글 작성자만이 신청자를 처리할 수 있습니다.");
        }

        List<PostApplicant> applicants = post.getApplicants();
        if (applicants == null || applicants.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글에 대한 신청자가 없습니다.");
        }

        // 신청자 목록에서 해당 유저를 찾아서 제거
        post.removeApplicant(targetUser.getNickname());
        postRepository.save(post);

        // 처리 결과에 따라 알림 메시지 설정
        String actionType = isApprove ? "허가" : "거부";
        String notificationMessage = String.format(" '%s'방에 참가신청이 %s되었습니다.", post.getTitle(), actionType);

        if (isApprove) {
            chatService.addParticipantToChatRoomByPost(post.getChatroomId(), userId);
        }

        notificationService.send(targetUser, AlarmType.eventCreateComment, notificationMessage,
                targetUser.getNickname(), targetUser.getProfileImageUrl(), "/feed/" + post.getId());

        String successMessage = isApprove ? "채팅 신청허가 성공" : "채팅 신청거부 성공";
        return ApiResponse.success(successMessage);
    }

    // 참여승인 메서드
    public ApiResponse<?> approveParticipant(User approver, Long postId, Long userId) {
        return approveOrDenyParticipant(approver, postId, userId, true);
    }

    // 참여거부 메서드
    public ApiResponse<?> denyParticipant(User denier, Long postId, Long userId) {
        return approveOrDenyParticipant(denier, postId, userId, false);
    }


    @Transactional
    public ApiResponse<?> addApplicant(User applicantUser, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 정보입니다."));

        PostApplicant applicant = new PostApplicant(
                applicantUser.getId(),
                applicantUser.getNickname(),
                applicantUser.getAge(),
                applicantUser.getGender(),
                applicantUser.getProfileImageUrl(),
                applicantUser.getReportCount(),
                post
        );
        post.getApplicants().add(applicant);
        User master = userRepository.findByNickname(post.getNickname())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        notificationService.send(master, AlarmType.eventCreateComment," '" + post.getTitle() + "'방에 참가신청을 하였습니다.",applicant.getNickname(), applicant.getProfileImageUrl(), "/feed/" + post.getId());
        return ApiResponse.success("채팅 참가신청 성공");
    }


    public List<PostApplicant> getApplicants(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 게시글 정보입니다."));

        return new ArrayList<>(post.getApplicants());
    }


    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, List<MultipartFile> images, User user) {
        List<String> imageUrlList = s3.uploads(images);
        postRequestDto.setImageUrlList(imageUrlList);
        ChatRoomEntity chatRoom = chatService.createRoomByPost(postRequestDto,user);
        String roomId = chatRoom.getRoomId();
        postRepository.save(new Post(postRequestDto, imageUrlList, user, roomId));
        log.info("'{}'님이 새로운 게시물을 생성했습니다.", user.getNickname());
        return ResponseUtils.okWithMessage(POST_CREATE_SUCCESS);
    }
    @Transactional
    public ApiResponse<?> deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        deleteImage(post);
        postRepository.delete(post);
        log.info("'{}'님이 게시물 ID '{}'를 삭제했습니다.", user.getNickname(), postId);
        return okWithMessage(POST_DELETE_SUCCESS);
    }
    @Transactional
    public ApiResponse<?> getRandomPosts(Pageable pageable) {
        List<Post> randomPosts = getRandomPostsFromDatabase(pageable);

        return ApiResponse.success(randomPosts.stream()
                .map(this::mapToPostResponseDto)
                .collect(Collectors.toList()));
    }

    private List<Post> getRandomPostsFromDatabase(Pageable pageable) {
        List<Post> allPosts = getRandomPostsFromDatabase();

        int totalPosts = allPosts.size();
        int pageSize = pageable.getPageSize();
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);

        int requestedPage = pageable.getPageNumber();
        if (requestedPage >= totalPages) {
            return Collections.emptyList();
        }

        int startIdx = requestedPage * pageSize;
        int endIdx = Math.min(startIdx + pageSize, totalPosts);

        return allPosts.subList(startIdx, endIdx);
    }

    @Transactional(readOnly = true)
    public List<Post> getRandomPostsFromDatabase() {
        List<Post> allPosts = postRepository.findAll();
        Collections.shuffle(allPosts);
        return allPosts;
    }

    public PostResponseDto mapToPostResponseDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getUser().getId(),
                post.getCategory(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getUser().getAge(),
                post.getUser().getGender(),
                post.getCreatedAt(),
                post.getImageUrlList().stream().limit(1).map(String::new).collect(Collectors.toList()),
                post.getEndDate(),
                post.getEndTime(),
                post.getLocation(),
                post.getUser().getProfileImageUrl()
        );
    }


    private void deleteImage(Post post) {
        List<String> imageUrlList = post.getImageUrlList();
        if (StringUtils.hasText(String.valueOf(imageUrlList))) {
            s3.delete(imageUrlList);
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private Post confirmPost(Long postId, User user) throws InvalidConditionException {
        Post post = findPost(postId);
        log.info("Confirming post access: postId={}, user={}, postUser={}, userRole={}",
                postId, user.getId(), post.getUser().getId(), user.getRole());
        if (!user.getId().equals(post.getUser().getId()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new InvalidConditionException(USER_NOT_MATCH);
        }

        log.info("Post access confirmed: postId={}, user={}", postId, user.getId());
        return post;
    }

    @Transactional
    @Async
    @Scheduled(cron = "0 0 0 * * *")
    public void checkEndTime() throws ParseException {
        List<Post> posts = postRepository.findAll();
        for(Post post : posts){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'y'MM'm'dd'd'");
            Date postEndDate = dateFormat.parse(post.getEndDate());
            if(postEndDate.before(DateTime.now().toDate())){
                postRepository.updateByStatus(post.getId(), Status.COMPLETED);
            }
        }
    }
}
