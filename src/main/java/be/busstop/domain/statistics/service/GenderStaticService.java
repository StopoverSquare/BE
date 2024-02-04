package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.dto.GenderStaticResponseDto;
import be.busstop.domain.statistics.entity.AgeStatic;
import be.busstop.domain.statistics.entity.CategoryStatic;
import be.busstop.domain.statistics.entity.GenderStatic;
import be.busstop.domain.statistics.repository.GenderStaticRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GenderStaticService {
    private final UserRepository userRepository;
    private final GenderStaticRepository genderStaticRepository;

    public void setGenderStatic() {
        LocalDate currentDate = LocalDate.now();
        genderStaticRepository.deleteByDate(currentDate);

        List<User> userList = userRepository.findAll();
        GenderStatic genderStatic = new GenderStatic();

        for (User user : userList) {
            String gender = user.getGender();
            genderStatic.plusCnt(gender);
        }
        genderStaticRepository.save(genderStatic);
    }
    public GenderStaticResponseDto getAllGenderStatic(){
        LocalDate now = LocalDate.now();
        GenderStatic genderStatic = genderStaticRepository.findByDate(now);
        Long maleCnt = 0L;
        Long femaleCnt = 0L;

        maleCnt += genderStatic.getMaleCnt();
        femaleCnt += genderStatic.getFemaleCnt();

        return GenderStaticResponseDto.builder()
                .maleCnt(maleCnt)
                .femaleCnt(femaleCnt)
                .build();
    }
}
