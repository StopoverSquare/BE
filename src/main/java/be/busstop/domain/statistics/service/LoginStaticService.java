package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.entity.LoginStatic;
import be.busstop.domain.statistics.repository.LoginStaticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    public Long getAllCnt(){
        List<LoginStatic> allStatic = loginStaticRepository.findAll();
        Long allCnt = 0L;
        for(LoginStatic loginStatic : allStatic){
            allCnt += loginStatic.getLoginCnt();
        }
        return allCnt;
    }
    public Long getWeekCnt(){
        LocalDate date = LocalDate.now();
        List<LoginStatic> loginStatics = loginStaticRepository.findAllByDateBetween(date.minusDays(7), date);
        long weekCnt = 0L;
        for(LoginStatic loginStatic : loginStatics){
            weekCnt += loginStatic.getLoginCnt();
        }
        return weekCnt;
    }

}
