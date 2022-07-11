package pnu.problemsolver.myorder.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import pnu.problemsolver.myorder.controller.converter.StringToJWTRequestConverter;
import pnu.problemsolver.myorder.dto.MemberType;
import pnu.problemsolver.myorder.security.JwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtTokenProvider tokenProvider;
    private final StringToJWTRequestConverter stringToJWTRequestConverter;


    //filter에서도 Bean주입받을 수 있다.
    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, StringToJWTRequestConverter stringToJWTRequestConverter) {
        this.stringToJWTRequestConverter = stringToJWTRequestConverter;
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromRequest(request); //request에서 jwt 토큰을 꺼낸다.
        if (jwt != null && !jwt.isEmpty()) {

            if (tokenProvider.isValidate(jwt)) {
                Claims claims = tokenProvider.getClaims(jwt);
                if (!claims.keySet().contains("email")) {
                    log.info("email을 찾지 못했습니다.");
//                        request.setAttribute("unauthorization", "email을 찾지 못했습니다.");
                    filterChain.doFilter(request, response);
                    return;
                }
            } else {
                log.info("JwtTokenProvider.isValidate()를 통과못함!");
//                    request.setAttribute("unauthorization", "JwtTokenProvider.isValidate()를 통과못함!");
            }
        } else {
            log.info("헤더에 jwt가 없습니다!");
        }
        filterChain.doFilter(request, response); //다음 필터로 넘겨줘야함.
    }

    //헤더에서 bearer값만 가져온다.
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {//null을 먼저 검사해야함. NPE뜬다.
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }
}


