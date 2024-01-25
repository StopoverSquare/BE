package be.busstop.domain.post.dto;

import be.busstop.domain.poststatus.entity.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSearchCondition {
    private String location;
    private String endDate;
    private String interest;
    private String titleOrContent;
    private String status;
}
