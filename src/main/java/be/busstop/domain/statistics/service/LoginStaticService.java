package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.entity.LoginStatic;
import be.busstop.domain.statistics.repository.LoginStaticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginStaticService {
    private final LoginStaticRepository loginStaticRepository;

    public void updateLoginStatic() {
        LocalDate date = LocalDate.now();
        LoginStatic loginStatic = loginStaticRepository.findByDate(date).orElse(new LoginStatic());

        loginStatic.plusLoginCnt();
        loginStaticRepository.save(loginStatic);
    }


    public List<Long> getTodayCntArray() {
        LocalDate currentDate = LocalDate.now();
        int daysInMonth = currentDate.lengthOfMonth();
        List<Long> todayCntArray = new ArrayList<>();

        for (int i = 0; i < daysInMonth; i++) {
            LocalDate loopDate = currentDate.withDayOfMonth(i + 1);
            LoginStatic loginStatic = loginStaticRepository.findByDate(loopDate).orElse(new LoginStatic());
            todayCntArray.add(loginStatic.getLoginCnt());
        }

        return todayCntArray;
    }

    public Long getStaticsByDate(LocalDate localDate){
        LoginStatic loginStatic = loginStaticRepository.findByDate(localDate).orElse(null);
        Long dateCnt = 0L;
        if(loginStatic != null){
            dateCnt = loginStatic.getLoginCnt();
        }
        return dateCnt;
    }

    public List<Long> getMonthCnt(){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29);

        return IntStream.range(0, 30)
                .mapToObj(i -> startDate.plusDays(i))
                .map(this::getStaticsByDate)
                .collect(Collectors.toList());
    }

    public Long getWeekCnt() {
        LocalDate date = LocalDate.now();
        List<LoginStatic> loginStatics = loginStaticRepository.findAllByDateBetween(date.minusDays(7), date);
        long weekCnt = 0L;
        for (LoginStatic loginStatic : loginStatics) {
            weekCnt += loginStatic.getLoginCnt();
        }
        return weekCnt;
    }

    public Long getAllDayCnt() {
        List<LoginStatic> loginStatics = loginStaticRepository.findAll();
        long allDayCnt = 0L;
        for (LoginStatic loginStatic : loginStatics) {
            allDayCnt += loginStatic.getLoginCnt();
        }
        return allDayCnt;
    }


    public void resetMonthlyLoginCntArray(LocalDate currentDate) {
        // 매월 1일 자정에 호출되어야 하는 로직을 추가
        loginStaticRepository.deleteByDate(currentDate.minusMonths(1));
    }
}
