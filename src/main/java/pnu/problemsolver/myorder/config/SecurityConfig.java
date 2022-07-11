package pnu.problemsolver.myorder.config;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import pnu.problemsolver.myorder.filter.JwtAuthenticationFilter;
import pnu.problemsolver.myorder.security.JwtTokenProvider;

import javax.servlet.Filter;
import javax.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity //기본적인 웹보안을 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationContext applicationContext;
//    private final JwtAuthenticationFilter jwtAuthenticationFilter; 이 클래스에서 생성하는데 이 클래스에서 DI받지는 못하는 듯.

    @Bean
    //암호화 객체
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public JwtAuthenticationFilter jwtAuthenticationFilter() {
////        System.out.println(applicationContext.getBean("테스트 : "+"jwtTokenProvider").getClass());
//        return new JwtAuthenticationFilter((JwtTokenProvider) applicationContext.getBean("jwtTokenProvider"));
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.
//                authorizeRequests()
//                .antMatchers("/api/hello").permitAll()
//                .anyRequest().authenticated();

        //빌더패턴을 사용함.
        http
                .httpBasic().disable()//기본 로그인 페이지 사용x
                .csrf().disable()   //REST API 사용하기 때문에 csrf x. cors는 enable해야할듯?
//                .authorizeRequests()
//                .antMatchers("/store/list", "/", "/login").permitAll()
////                .antMatchers("**").hasRole("USER")        //USER 접근 가능
////                .antMatchers("/admin/**").hasRole("ADMIN")     //ADMIN만 접근 가능
//                .anyRequest().authenticated()   //나머지는 로그인하면 접근가능.
//                .and()
//                .formLogin().disable()//form login안한다. 아래 줄은 form로그인을 disable하면 설정할 수 없는 애들임.
////                .loginPage("/login")    //로그인 페이지
////                .defaultSuccessUrl("/store/list") //로그인 성공 후
////                .and()
//                .logout()
//                .logoutSuccessUrl("/store/list")//로그아웃 성공
//                .and()
//                .exceptionHandling() //extends WebSecurityConfigureAdapter 하면 자동추가됌.
//                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //jwt사용을 위해 session해제.

    }

}
