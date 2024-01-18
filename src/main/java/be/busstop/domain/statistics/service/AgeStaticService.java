package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.dto.AgeStaticResponseDto;
import be.busstop.domain.statistics.entity.AgeStatic;
import be.busstop.domain.statistics.repository.AgeStaticRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgeStaticService {
    private final UserRepository userRepository;
    private final AgeStaticRepository ageStaticRepository;

    public void setAgeStatic() {
        LocalDate currentDate = LocalDate.now();
        ageStaticRepository.deleteByDate(currentDate);

        List<User> userList = userRepository.findAll();
        AgeStatic ageStatic = new AgeStatic();

        for (User user : userList) {
            String age = user.getAge();
            ageStatic.plusCnt(age);
        }
        ageStaticRepository.save(ageStatic);
    }

    public AgeStaticResponseDto getAllAgeStatic(){
        List<AgeStatic> ageStatics = ageStaticRepository.findAll();
        Long tenCnt = 0L;
        Long twentyCnt = 0L;
        Long thirtyCnt = 0L;
        Long fortyCnt = 0L;
        Long fiftyCnt = 0L;
        Long sixtyCnt = 0L;
        Long ageEtcCnt = 0L;
        for(AgeStatic ageStatic : ageStatics){
            tenCnt += ageStatic.getTenCnt();
            twentyCnt += ageStatic.getTwentyCnt();
            thirtyCnt += ageStatic.getThirtyCnt();
            fortyCnt += ageStatic.getFortyCnt();
            fiftyCnt += ageStatic.getFiftyCnt();
            sixtyCnt += ageStatic.getSixtyCnt();
            ageEtcCnt += ageStatic.getEtcCnt();
        }

        return AgeStaticResponseDto.builder()
                .tenCnt(tenCnt)
                .twentyCnt(twentyCnt)
                .thirtyCnt(thirtyCnt)
                .fortyCnt(fortyCnt)
                .fiftyCnt(fiftyCnt)
                .sixtyCnt(sixtyCnt)
                .ageEtcCnt(ageEtcCnt)
                .build();
    }
}
