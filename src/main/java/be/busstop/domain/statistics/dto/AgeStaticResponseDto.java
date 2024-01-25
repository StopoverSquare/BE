package be.busstop.domain.statistics.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AgeStaticResponseDto {
    private Long tenCnt;
    private Long twentyCnt;
    private Long thirtyCnt;
    private Long fortyCnt;
    private Long fiftyCnt;
    private Long sixtyCnt;
    private Long ageEtcCnt;
}
