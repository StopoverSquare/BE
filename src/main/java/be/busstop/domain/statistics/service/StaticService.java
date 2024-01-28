package be.busstop.domain.statistics.service;

import be.busstop.domain.post.entity.Post;
import be.busstop.domain.post.repository.PostRepository;
import be.busstop.domain.statistics.dto.*;
import be.busstop.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaticService {
    private final AgeStaticService ageStaticService;
    private final CategoryStaticService categoryStaticService;
    private final GenderStaticService genderStaticService;
    private final LoginStaticService loginStaticService;
    private final PostRepository postRepository;

    public void updateStatistics() {
        ageStaticService.setAgeStatic();
        categoryStaticService.setCategoryStatic();
        genderStaticService.setGenderStatic();
        loginStaticService.updateLoginStatic();
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void resetMonthlyLoginCntArray() {
        LocalDate currentDate = LocalDate.now();
        loginStaticService.resetMonthlyLoginCntArray(currentDate);
    }

    public ApiResponse<?> getAllStatic() {
        updateStatistics();
        List<Long> monthCnt = loginStaticService.getMonthCnt();
        Long weekCnt = loginStaticService.getWeekCnt();
        Long allDayCnt = loginStaticService.getAllDayCnt();

        AgeStaticResponseDto ageStaticResponseDto = ageStaticService.getAllAgeStatic();
        CategoryStaticResponseDto categoryStaticResponseDto = categoryStaticService.getAllCategoryStatic();
        GenderStaticResponseDto genderStaticResponseDto = genderStaticService.getAllGenderStatic();

        StaticResponseDto staticResponseDto = StaticResponseDto.builder()
                .tenCnt(ageStaticResponseDto.getTenCnt())
                .twentyCnt(ageStaticResponseDto.getTwentyCnt())
                .thirtyCnt(ageStaticResponseDto.getThirtyCnt())
                .fortyCnt(ageStaticResponseDto.getFortyCnt())
                .fiftyCnt(ageStaticResponseDto.getFiftyCnt())
                .sixtyCnt(ageStaticResponseDto.getSixtyCnt())
                .ageEtcCnt(ageStaticResponseDto.getAgeEtcCnt())
                .eatsCnt(categoryStaticResponseDto.getEatsCnt())
                .cultureCnt(categoryStaticResponseDto.getCultureCnt())
                .exerciseCnt(categoryStaticResponseDto.getExerciseCnt())
                .studyCnt(categoryStaticResponseDto.getStudyCnt())
                .categoryEtcCnt(categoryStaticResponseDto.getCategoryEtcCnt())
                .maleCnt(genderStaticResponseDto.getMaleCnt())
                .femaleCnt(genderStaticResponseDto.getFemaleCnt())
                .genderEtcCnt(genderStaticResponseDto.getGenderEtcCnt())
                .monthCnt(monthCnt)
                .weekCnt(weekCnt)
                .allDayCnt(allDayCnt)
                .build();

        return ApiResponse.success(staticResponseDto);
    }

    public ApiResponse<?> getAllLocation() {
        List<Post> postList = postRepository.findAll();
        List<LocationResponseDto> locationList = new ArrayList<>();

        for(Post post : postList){
            Long postId = post.getId();
            String date = post.getEndDate();
            String location = post.getLocation();

            LocationResponseDto responseDto = LocationResponseDto.builder()
                    .postId(postId)
                    .date(date)
                    .location(location)
                    .build();
            locationList.add(responseDto);
        }
        return ApiResponse.success(locationList);
    }
}
