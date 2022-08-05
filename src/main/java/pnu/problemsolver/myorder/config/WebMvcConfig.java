package pnu.problemsolver.myorder.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pnu.problemsolver.myorder.filter.JwtAuthenticationFilter;
import pnu.problemsolver.myorder.filter.JwtRoleFilter;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;

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
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterReg() {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(applicationContext.getBean(JwtTokenProvider.class),
                applicationContext.getBean(StoreService.class), applicationContext.getBean(CustomerService.class)));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        registrationBean.setName("JwtAuthenticationFilter : Role을 배분");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<JwtRoleFilter> jwtRoleFilterReg() {
        FilterRegistrationBean<JwtRoleFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtRoleFilter());
        registrationBean.addUrlPatterns("/*");//설정안해도 된다.
         registrationBean.setOrder(2);
        registrationBean.setName("JwtRoleFilter : Role에 따라 접근제한");
        return registrationBean;
    }
    
    //TODO : 시간남으면 enum 소문자로 써도 되게 하기
//    @Bean
//    public FilterRegistrationBean<EnumToUpperCaseFilter> enumToUpperCaseFilter() {
//        FilterRegistrationBean<EnumToUpperCaseFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new EnumToUpperCaseFilter());
////        registrationBean.addUrlPatterns("/*"); //디폴트값
//        registrationBean.setOrder(3);
//        registrationBean.setName("EmumToUpperCaseFileter : enum사용을 위해 대문자로 변경");
//        return registrationBean;
//    }
}
