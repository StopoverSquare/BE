package be.busstop.domain.salvation.dto;

import be.busstop.domain.salvation.entity.Salvation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SalvResponseDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrlList;
    private Boolean isView;

    @QueryProjection
    public SalvResponseDto(Salvation salvation) {
        this.id = salvation.getId();
        this.title = salvation.getTitle();
        this.createdAt = salvation.getCreatedAt();
    }

    public SalvResponseDto(Salvation salvation, Boolean isView){
        this.id = salvation.getId();
        this.title = salvation.getTitle();
        this.content = salvation.getContent();
        this.imageUrlList = salvation.getImageUrlList();
        this.createdAt = salvation.getCreatedAt();
        this.isView = isView;

    }
}
