package be.busstop.domain.statistics.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenderStaticResponseDto {
    private Long maleCnt;
    private Long femaleCnt;
    private Long genderEtcCnt;
}
