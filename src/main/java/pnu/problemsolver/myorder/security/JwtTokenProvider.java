package pnu.problemsolver.myorder.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@RequiredArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor
// 이 클래스에서는 거의 jjwt라이브러리만 쓴다.
@Component
public class JwtTokenProvider {

    //Logger import할 때 slf4j.Logger사용해야함. 함수 먼저 사용하고 리팩토링 기능 사용하자.
    private static final Logger log = LoggerFactory.getLogger("JwtTokenProvider");
    private final Environment environment;

//    @Value("${jwt.secrete}") //lombok이 아니라 spring 어노테이션
    private final String SECRET_KEY;
//    @Value("${jwt.validity.time}")
    public final long JWT_TOKEN_VALIDITY;

    @Autowired
    public JwtTokenProvider(Environment environment) {
        this.environment = environment;
        this.SECRET_KEY = environment.getProperty("jwt.secret");
        this.JWT_TOKEN_VALIDITY = Long.parseLong(environment.getProperty("jwt.validity.time"));//string으로 읽어오기 때문
    }

    public String createToken() {

//헤더생성
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Date date = new Date();
        date.setTime(System.currentTimeMillis() + JWT_TOKEN_VALIDITY);

        //payload생성.
        Claims claims = Jwts.claims()
                .setSubject("access_token")
                .setIssuedAt(new Date())
                .setExpiration(date);//하루 뒤로 설정

        // TODO : 정의, 추가
        claims.put("key", "value");//임의로 내가 넣으면 된다.

        String jwt = Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();

        return jwt;
    }

    public boolean isValidate(String jwt) {
            try {
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwt);
                return true;
            } catch (SignatureException ex) {
                log.error("Invalid JWT signature");
            } catch (MalformedJwtException ex) {
                log.error("Invalid JWT token");
            } catch (ExpiredJwtException ex) {
                log.error("Expired JWT token");
            } catch (UnsupportedJwtException ex) {
                log.error("Unsupported JWT token");
            } catch (IllegalArgumentException ex) {
                log.error("JWT claims string is empty.");
            }
            return false;
        }


    public Claims getClaims(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
}

