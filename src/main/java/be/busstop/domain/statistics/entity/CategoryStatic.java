package be.busstop.domain.statistics.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class CategoryStatic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Column
    @Builder.Default
    private Long eatsCnt = 0L;

    @Column
    @Builder.Default
    private Long cultureCnt = 0L;

    @Column
    @Builder.Default
    private Long exerciseCnt = 0L;

    @Column
    @Builder.Default
    private Long studyCnt = 0L;

    @Column
    @Builder.Default
    private Long etcCnt = 0L;


    public void plusCnt(String category){
        switch (category) {
            case "Eats" -> this.eatsCnt++;
            case "Culture" -> this.cultureCnt++;
            case "Exercise" -> this.exerciseCnt++;
            case "Study" -> this.studyCnt++;
            default -> this.etcCnt++;
        }
    }
}
