package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.entity.AgeStatic;
import be.busstop.domain.statistics.repository.AgeStaticRepository;
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
public class AgeStaticService {
    private final UserRepository userRepository;
    private final AgeStaticRepository ageStaticRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void setAgeStatic(){
        LocalDate date = LocalDate.now();
        List<User> userList = userRepository.findAllByCreatedAt(date.atStartOfDay());
        AgeStatic ageStatic = ageStaticRepository.findByDate(date).orElse(new AgeStatic());

        for(User user : userList){
            String age = user.getAge();
            ageStatic.plusCnt(age);
        }
        ageStaticRepository.save(ageStatic);
    }
}
