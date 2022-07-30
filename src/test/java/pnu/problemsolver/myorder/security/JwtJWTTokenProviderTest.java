package pnu.problemsolver.myorder.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import pnu.problemsolver.myorder.domain.constant.MemberType;

import java.util.Set;


@SpringBootTest
class JwtJWTTokenProviderTest {

    @Autowired
    public Environment env;

    @Autowired
    public JwtTokenProvider tokenProvider; //environment를 DI받아서 springbootTest해야한다.

    public final Logger logger = LoggerFactory.getLogger(this.getClass());



    @Test
    public void 스프링properties값_가져오기() {
        System.out.println(env.getProperty("jwt.secret"));
        System.out.println((env.getProperty("jwt.validity.time")));
    }

    @Test
    public void 검증() {
        String jwt = tokenProvider.createToken(MemberType.STORE);

        if (tokenProvider.isValidate(jwt)) {
            Claims claims = tokenProvider.getClaims(jwt);
            Set<String> keySet = claims.keySet();
            for (String key: keySet) {
                System.out.println(key+" : "+claims.get(key));

            }
        }
    }


}