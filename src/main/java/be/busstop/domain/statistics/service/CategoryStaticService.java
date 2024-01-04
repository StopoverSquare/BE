package be.busstop.domain.statistics.service;

import be.busstop.domain.statistics.dto.CategoryStaticResponseDto;
import be.busstop.domain.statistics.entity.CategoryStatic;
import be.busstop.domain.statistics.repository.CategoryStaticRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryStaticService {
    private final UserRepository userRepository;
    private final CategoryStaticRepository categoryStaticRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void setCategoryStatic(){
        LocalDate date = LocalDate.now();
        List<User> userList = userRepository.findAllByCreatedAt(date.atStartOfDay());
        CategoryStatic categoryStatic = categoryStaticRepository.findByDate(date).orElse(new CategoryStatic());

        for(User user : userList){
            String interest = String.valueOf(user.getInterest());
            categoryStatic.plusCnt(interest);
        }
        categoryStaticRepository.save(categoryStatic);
    }

    public CategoryStaticResponseDto getAllCategoryStatic(){
        List<CategoryStatic> categoryStatics = categoryStaticRepository.findAll();
        Long eatsCnt = 0L;
        Long cultureCnt = 0L;
        Long exerciseCnt = 0L;
        Long studyCnt = 0L;
        Long categoryEtcCnt = 0L;
        for(CategoryStatic categoryStatic : categoryStatics){
            eatsCnt += categoryStatic.getEatsCnt();
            cultureCnt += categoryStatic.getCultureCnt();
            exerciseCnt += categoryStatic.getExerciseCnt();
            studyCnt += categoryStatic.getStudyCnt();
            categoryEtcCnt += categoryStatic.getEtcCnt();
        }
        return CategoryStaticResponseDto.builder()
                .eatsCnt(eatsCnt)
                .cultureCnt(cultureCnt)
                .exerciseCnt(exerciseCnt)
                .studyCnt(studyCnt)
                .categoryEtcCnt(categoryEtcCnt)
                .build();
    }
}
