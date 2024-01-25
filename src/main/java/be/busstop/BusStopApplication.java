package be.busstop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class BusStopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusStopApplication.class, args);
    }

}
