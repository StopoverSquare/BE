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
        int ages = Integer.parseInt(age)/10;
        switch (ages) {
            case 10 -> this.tenCnt++;
            case 20 -> this.twentyCnt++;
            case 30 -> this.thirtyCnt++;
            case 40 -> this.fortyCnt++;
            case 50 -> this.fiftyCnt++;
            case 60 -> this.sixtyCnt++;
            default -> this.etcCnt++;
        }
    }
}
