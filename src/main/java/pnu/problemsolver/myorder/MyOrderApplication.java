package pnu.problemsolver.myorder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class MyOrderApplication {


    public static void main(String[] args) {
        SpringApplication.run(MyOrderApplication.class, args);


    }
}
