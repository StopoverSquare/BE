package be.busstop.domain.salvation.entity;

import be.busstop.domain.salvation.dto.SalvRequestDto;
import be.busstop.global.utils.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Salvation extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "salvation_id")
    private Long id;

    @Column
    private Long userId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ElementCollection
    @BatchSize(size = 5)
    @Column
    private List<String> imageUrlList = new ArrayList<>();

    public Salvation(SalvRequestDto salvRequestDto, Long userId) {
        this.title = salvRequestDto.getTitle();
        this.content = salvRequestDto.getContent();
        this.userId = userId;

    }
}
