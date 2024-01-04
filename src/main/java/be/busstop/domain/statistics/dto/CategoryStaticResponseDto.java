package be.busstop.domain.statistics.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CategoryStaticResponseDto {
    private Long eatsCnt;
    private Long cultureCnt;
    private Long exerciseCnt;
    private Long studyCnt;
    private Long categoryEtcCnt;
}
