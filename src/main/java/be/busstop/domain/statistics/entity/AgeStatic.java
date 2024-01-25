package be.busstop.domain.statistics.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
public class AgeStatic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Column
    @Builder.Default
    private Long tenCnt = 0L;

    @Column
    @Builder.Default
    private Long twentyCnt = 0L;

    @Column
    @Builder.Default
    private Long thirtyCnt = 0L;

    @Column
    @Builder.Default
    private Long fortyCnt = 0L;

    @Column
    @Builder.Default
    private Long fiftyCnt = 0L;

    @Column
    @Builder.Default
    private Long sixtyCnt = 0L;

    @Column
    @Builder.Default
    private Long etcCnt = 0L;

    public void plusCnt(String age) {
        if (age == null) {
            this.etcCnt++;
            return;
        }

        int ageValue;
        try {
            ageValue = Integer.parseInt(age);
        } catch (NumberFormatException e) {
            this.etcCnt++;
            return;
        }

        if (ageValue >= 10 && ageValue < 20) {
            this.tenCnt++;
        } else if (ageValue >= 20 && ageValue < 30) {
            this.twentyCnt++;
        } else if (ageValue >= 30 && ageValue < 40) {
            this.thirtyCnt++;
        } else if (ageValue >= 40 && ageValue < 50) {
            this.fortyCnt++;
        } else if (ageValue >= 50 && ageValue < 60) {
            this.fiftyCnt++;
        } else if (ageValue >= 60) {
            this.sixtyCnt++;
        } else {
            this.etcCnt++;
        }
    }
}