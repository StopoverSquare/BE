package be.busstop.domain.post.dto;

import be.busstop.domain.post.entity.Category;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostRequestDto {
    private Category category;
    @Pattern(regexp = ".{1,30}", message = "제목은 1자에서 30자까지만 허용됩니다.")
    private String title;
    @Pattern(regexp = "^[\\s\\S]{1,1500}$", message = "내용은 1자에서 1500자까지만 허용되며 줄바꿈도 허용됩니다.")
    private String content;
    private String location;
    private String endDate;
    private String endTime;
    private List<String> imageUrlList;
    private String thumbnailImageUrl;
    private Long subLimit;
    private float lng;
    private float lat;
    private String placeName;


    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
    public void setImageUrlListAndThumbnail(List<String> imageUrlList, int thumbnailIndex) {
        this.imageUrlList = imageUrlList;
        if (thumbnailIndex >= 0 && thumbnailIndex < imageUrlList.size()) {
            this.thumbnailImageUrl = imageUrlList.get(thumbnailIndex);
        }
    }
}
