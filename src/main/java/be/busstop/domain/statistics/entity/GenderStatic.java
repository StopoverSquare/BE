package be.busstop.domain.statistics.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class GenderStatic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Column
    private Long maleCnt;

    @Column
    private Long femaleCnt;

    @Column
    private Long etcCnt;

    public void plusCnt(String gender) {
        switch(gender){
            case "Male" -> this.maleCnt++;
            case "Female" -> this.femaleCnt++;
            default -> this.etcCnt++;
        }
    }
}
