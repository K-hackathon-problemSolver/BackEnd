package pnu.problemsolver.myorder.filter;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	
	//    private final StringToJWTequestConverter stringToJWTRequestConverter;
	private final JwtTokenProvider tokenProvider;
	private final StoreService storeService;
	private final CustomerService customerService;
	
	final String memTypeKey = "memberType";
	final String role = "role";
	
	
	//filter에서도 Bean주입받을 수 있다. 예전에는 안되서 안된다고 설명한 책도 있음.
	@Autowired
	public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, StoreService storeService, CustomerService customerService) {
//        this.stringToJWTRequestConverter = stringToJWTRequestConverter;
		this.tokenProvider = tokenProvider;
		this.storeService = storeService;
		this.customerService = customerService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String jwt = getJwtFromRequest(request); //request에서 jwt 토큰을 꺼낸다.
		if (jwt != null && !jwt.isEmpty()) {
			if (tokenProvider.isValidate(jwt)) {
				Claims claims = tokenProvider.getClaims(jwt);
				if (claims.keySet().contains(memTypeKey)) {
					String memType = (String) claims.get(memTypeKey);//이까지 오면 로그인 된 상황.
					try {
						MemberType memberType = MemberType.valueOf(memType.toUpperCase());
						request.setAttribute("memberType", memberType);//customer or store
						
						log.info(memType.toString());
						
						log.info("jwt성공!");
					} catch (IllegalArgumentException e) {
						log.error("MemberType.valueOf() error!");
						e.printStackTrace();
					}
					
					//TODO : store, customer service에서 검사.!
				} else {
					log.info("memberType을 찾지 못했습니다.");
				}
			} else {
				log.info("JwtTokenProvider.isValidate()를 통과못함!");
			}
		} else {
			log.info("헤더에 jwt가 없습니다!");
		}
		request.setAttribute(role, MemberType.GUEST);//JWT가 있어도 올바르지 않다면 GUEST권한을 부여함.
		filterChain.doFilter(request, response); //다음 필터로 넘겨줘야함.
	}
	
	//헤더에서 bearer값만 가져온다. jwt는 헤더에 있는 값임. body에 없으니 안심!
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {//null을 먼저 검사해야함. NPE뜬다.
			return bearerToken.substring("Bearer ".length());
		}
		return null;
	}
}