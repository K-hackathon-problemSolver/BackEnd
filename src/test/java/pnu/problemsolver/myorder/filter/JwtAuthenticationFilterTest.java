package pnu.problemsolver.myorder.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import pnu.problemsolver.myorder.security.JwtTokenProvider;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtAuthenticationFilterTest {

    @Autowired
    ApplicationContext ac;


    @Test
    public void DI테스트() {
        JwtTokenProvider tokenProvider = (JwtTokenProvider) ac.getBean("jwtTokenProvider");
        System.out.println(tokenProvider.getClass());


        JwtAuthenticationFilter jwtAuthenticationFilter = (JwtAuthenticationFilter) ac.getBean("jwtAuthenticationFilter");
        System.out.println(jwtAuthenticationFilter.getClass());

    }

}