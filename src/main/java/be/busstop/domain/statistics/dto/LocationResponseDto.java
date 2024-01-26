package be.busstop.domain.statistics.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LocationResponseDto {
    private Long postId;
    private String location;
    private String date;
}
