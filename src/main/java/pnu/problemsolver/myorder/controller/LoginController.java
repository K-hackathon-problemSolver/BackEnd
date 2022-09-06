package pnu.problemsolver.myorder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;

import java.util.UUID;

//@Controller
@RestController//restController안에 @controller, @ResponseBody있다.
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final StoreService storeService;
    private final CustomerService customerService;
    
    
    private final String clientID;
    private final String secret;
    private final String redirectURL;
    private final String stateToken;
    private final String naverTokenURL = "https://nid.naver.com/oauth2.0/token";
    private final String naverMemberInfoURL = "https://openapi.naver.com/v1/nid/me";
    
    
    @Autowired
    public LoginController(Environment environment, JwtTokenProvider tokenProvider, StoreService storeService, CustomerService customerService) {
//        this.restTemplate = restTemplate;
        this.jwtTokenProvider = tokenProvider;
        this.storeService = storeService;
        this.customerService = customerService;
        
        stateToken = environment.getProperty("OAuth.stateToken");
        redirectURL = environment.getProperty("OAuth.naver.redirectURL");
        clientID = environment.getProperty("OAuth.naver.clientID");
        secret = environment.getProperty("OAuth.naver.secret");
    }

    
    /**
     * @param dto
     * @return 기존회원이건 아니건 uuid를 반환한다.!
     */
    private UUID SNSgeneralCustomerLogin(GeneralOAuthDTO dto) {
        //TODO : 여기서 추가적인 검증을 필요로 할 수도 있다!
        return customerService.login(dto);
//
    }
    
    private UUID SNSgeneralStoreLogin(GeneralOAuthDTO dto) {
        return storeService.login(dto);
    }
    
    
    /**
     * naver, kakao등 모두 대입가능한 login, 회원가입 컨트롤러 !
     *
     * @param dto
     * @return
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody GeneralOAuthDTO dto) {
        UUID uuid = null;
        String jwt = null;
        if (dto.getMemberType() == MemberType.STORE) {//store일 때
            uuid = SNSgeneralStoreLogin(dto);
            jwt = jwtTokenProvider.createToken(MemberType.STORE); //payload추가!
            
        } else if (dto.getMemberType() == MemberType.CUSTOMER) {//customer일 때
            uuid = SNSgeneralCustomerLogin(dto);
            jwt = jwtTokenProvider.createToken(MemberType.CUSTOMER);
        } else {
            throw new RuntimeException("storeLogin()에서 memberType이 CUSTOMER, STORE 중에 없습니다!");
        }
        //jwt 만들기.
        
        LoginResponseDTO res = LoginResponseDTO.builder()
                .jwt(jwt)
                .uuid(uuid)
                .build();
        return res;
        //멤버 타입에 따라 insert가 달라진다. 그리고 있는지 확인부터 해야함.
    }
    
    @PostMapping("/logout")
    public void logout() {
    
    }
}
