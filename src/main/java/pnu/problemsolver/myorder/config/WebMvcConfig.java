package pnu.problemsolver.myorder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pnu.problemsolver.myorder.filter.JwtAuthenticationFilter;
import pnu.problemsolver.myorder.repository.CustomerRepository;
import pnu.problemsolver.myorder.repository.StoreRepository;
import pnu.problemsolver.myorder.security.JwtTokenProvider;

@Configuration
@RequiredArgsConstructor

public class WebMvcConfig implements WebMvcConfigurer {//converter등록을 위해서는 implements WebMvcConfigurer해야한다.

    private final ApplicationContext applicationContext;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
//        return new JwtAuthenticationFilter((JwtTokenProvider) applicationContext.getBean("jwtTokenProvider")
//                , (StoreRepository) applicationContext.getBean("storeRepository")
//                , (CustomerRepository) applicationContext.getBean("customerRepository"));
//    }

//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter();
//
//    }
}