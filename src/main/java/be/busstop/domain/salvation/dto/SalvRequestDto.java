package be.busstop.domain.salvation.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SalvRequestDto {
    @Pattern(regexp = ".{1,30}", message = "제목은 1자에서 30자까지만 허용됩니다.")
    private String title;
    @Pattern(regexp = "^[\\s\\S]{1,1500}$", message = "내용은 1자에서 1500자까지만 허용되며 줄바꿈도 허용됩니다.")
    private String content;
}
