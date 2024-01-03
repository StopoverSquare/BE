package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.entity.LoginStatic;
import be.busstop.domain.statistics.repository.LoginStaticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginStaticService {
    private final LoginStaticRepository loginStaticRepository;
    public void updateLoginStatic(){
        LocalDate date = LocalDate.now();
        LoginStatic loginStatic = loginStaticRepository.findByDate(date).orElse(new LoginStatic());

        loginStatic.plusLoginCnt();
        loginStaticRepository.save(loginStatic);
    }
}
