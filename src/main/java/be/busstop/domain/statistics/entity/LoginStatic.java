package be.busstop.domain.statistics.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Getter
public class LoginStatic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Builder.Default
    private LocalDate date = LocalDate.now();

    @Column
    @Builder.Default
    private Long loginCnt = 0L;


    public void plusLoginCnt(){
        this.loginCnt++;
    }
}
