package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.dto.AgeStaticResponseDto;
import be.busstop.domain.statistics.dto.CategoryStaticResponseDto;
import be.busstop.domain.statistics.dto.GenderStaticResponseDto;
import be.busstop.domain.statistics.dto.StaticResponseDto;
import be.busstop.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaticService {
    private final AgeStaticService ageStaticService;
    private final CategoryStaticService categoryStaticService;
    private final GenderStaticService genderStaticService;
    private final LoginStaticService loginStaticService;
    public ApiResponse<?> getAllStatic() {
        Long todayCnt = loginStaticService.getTodayCnt();
        Long weekCnt = loginStaticService.getWeekCnt();

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
                .todayCnt(todayCnt)
                .weekCnt(weekCnt)
                .build();
        return ApiResponse.success(staticResponseDto);
    }
}
