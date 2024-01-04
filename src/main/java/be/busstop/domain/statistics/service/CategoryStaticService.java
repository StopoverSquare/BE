package be.busstop.domain.statistics.service;

import be.busstop.domain.post.entity.Category;
import be.busstop.domain.statistics.entity.CategoryStatic;
import be.busstop.domain.statistics.entity.LoginStatic;
import be.busstop.domain.statistics.repository.CategoryStaticRepository;
import be.busstop.domain.user.entity.User;
import be.busstop.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.parameters.P;
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
}
