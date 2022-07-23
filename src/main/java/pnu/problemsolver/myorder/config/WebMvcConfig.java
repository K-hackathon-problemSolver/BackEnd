package pnu.problemsolver.myorder.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pnu.problemsolver.myorder.filter.JwtAuthenticationFilter;
import pnu.problemsolver.myorder.filter.JwtRoleFilter;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Filter;

@Configuration
@RequiredArgsConstructor

public class WebMvcConfig implements WebMvcConfigurer {//converter등록을 위해서는 implements WebMvcConfigurer해야한다.


    private final ApplicationContext applicationContext;



//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

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

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilter() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(applicationContext.getBean(JwtTokenProvider.class),
                applicationContext.getBean(StoreService.class), applicationContext.getBean(CustomerService.class)));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        registrationBean.setName("JwtAuthenticationFilter : Role을 배분");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtRoleFilter> jwtRoleFilter() {
        FilterRegistrationBean<JwtRoleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtRoleFilter());

        return registrationBean;
    }
}
