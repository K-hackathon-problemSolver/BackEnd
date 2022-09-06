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
    
 
}
