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


//    @GetMapping("/login")
//    public Map<String, String> login() {
//        String encodedRedirectURL = null;
//        Map<String, String> urlMap = new HashMap<>();
//
//        try {
//            encodedRedirectURL = URLEncoder.encode(redirectURL, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            log.error("/login에서 url encode 에러!");
//        }
//        urlMap.put("naver", "https://nid.naver.com/oauth2.0/authorize?client_id=i6vA823oE3F_9QtAonj6&response_type=code&redirect_uri="
//                + encodedRedirectURL + "&state=" + stateToken);//나중에 네이버 뿐만 아니라 카카오 등 다른 플랫폼도 지원할 수 있기 때문에 Map으로 구현해 놓았다.
////        System.out.println(urlMap.get("naver"));
//
//        return urlMap;
//    }
    
    /**
     * redirect URL
     *
     * @param httpServletRequest
     * @return
     */
//    @GetMapping("/login/naver")
//    public LoginResponseDTO loginNaver(HttpServletRequest httpServletRequest) {
//        System.out.println(httpServletRequest.getQueryString());
//
//        String url = naverTokenURL + "?client_id="
//                + clientID
//                + "&client_secret="
//                + secret
//                + "&grant_type=authorization_code&state=" +
//                stateToken +
//                "&code=" +
//                httpServletRequest.getParameter("code");
//
//        System.out.println(url);
//
////        HttpHeaders headers = new HttpHeaders();
////        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
////        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);
//
//        ResponseEntity<String> response = Request.restTemplate.getForEntity(url, String.class);
//        log.info("body! : " + response.getBody());//accessToken, refreshToken 가져옴
//        log.info("header! : " + response.getHeaders());
//        Map<String, String> tokens;
//        try {
//            tokens = Mapper.objectMapper.readValue(response.getBody(), Map.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("tokens! : " + tokens);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(tokens.get("access_token"));
//        log.info("headers! : " + headers);
//        HttpEntity entity = new HttpEntity(headers);
//
//        ResponseEntity<Map> map = Request.restTemplate.exchange(naverMemberInfoURL, HttpMethod.GET, entity, Map.class);//여기서 Map으로 맵핑.
//
////        log.info(map.getClass().toString());
////        log.info(map.getBody().getClass().toString());
////
////        log.info(map.toString());
////        log.info(map.getBody().toString());
////        log.info(map.getBody().keySet().toString());
//        log.info(map.getBody().get("response").getClass().toString());//map에서 그대로 사용하면 나중에 또 문자열 출력해서 확인해야한다. 무조건 객체 만드는 것이 이득임. 관리, 유지보수가 편하다.
//        Map memberInfoMap = (Map) map.getBody().get("response");
//        NaverOAuthDTO naverOAuthDTO = new NaverOAuthDTO(memberInfoMap);//맵퍼로 동작안하면 생성자로 만들어주면 됨.
//        log.info(naverOAuthDTO.toString());
//        //일단 전부 손님의 자격으로 넣고 사장님만 추후 변경할 수 있게 한다.
//        //DB저장.
//        //소셜로그인은 이미 있는지 확인하고 save해야한다.!
//        UUID uuid = naverLogin(naverOAuthDTO);
//        //jwt 만들기.
//        String jwt = jwtTokenProvider.createToken();
//        LoginResponseDTO res = LoginResponseDTO.builder()
//                .jwt(jwt)
//                .uuid(uuid)
//                .build();
//        return res;
//    }
    
    /**
     * 소셜로그인은 따로 함수를 만들어서 사용한다. 있는지 확인해야 하기 떄문임!
     * @param naverOAuthDTO
     * @return uuid반환.
     */
//    private UUID naverLogin(NaverOAuthDTO naverOAuthDTO) {
//        CustomerDTO customerDTO = CustomerDTO.NaverOAuthDTOToDTO(naverOAuthDTO);
//        CustomerDTO res = customerService.findBySnsTypeAndSnsIdentifyKey(customerDTO);
//        if (res == null) {//이미 회원임.
//            CustomerDTO savedDTO = customerService.save(customerDTO);//PK가 uuid이기 때문에 기존 회원이더라도 새로 save해버림. 그래서 이렇게 따로 함수를 만든다.
//            return savedDTO.getUuid();
//        }
//        return res.getUuid();
//    }
    
    /**
     * @param dto
     * @return 기존회원이건 아니건 uuid를 반환한다.!
     */
    private UUID SNSgeneralCustomerLogin(GeneralOAuthDTO dto) {
        //TODO : 여기서 추가적인 검증을 필요로 할 수도 있다!
        CustomerDTO customerDTO = CustomerDTO.GeneralOAuthDTOtoDTO(dto);
        CustomerDTO res = customerService.findBySnsTypeAndSnsIdentifyKey(customerDTO);
        if (res == null) {//회원 아님. 저장.
            customerService.save(customerDTO);//PK가 uuid이기 때문에 기존 회원이더라도 새로 save해버림. 그래서 이렇게 따로 함수를 만든다.
            return customerDTO.getUuid();
        }
        return res.getUuid();
    }
    
    private UUID SNSgeneralStoreLogin(GeneralOAuthDTO dto) {
        //TODO : 여기서 추가적인 검증을 필요로 할 수도 있다!
        StoreDTO storeDTO = StoreDTO.GeneralOAuthDTOtoDTO(dto);
        StoreDTO res = storeService.findBySnsTypeAndSnsIdentifyKey(i -> StoreDTO.toDTO(i), dto.getSnsType(), dto.getSnsIdentifyKey());
        if (res == null) {//회원이 아닐 때!
            storeService.save(storeDTO);//PK가 uuid이기 때문에 기존 회원이더라도 새로 save해버림. 그래서 이렇게 따로 함수를 만든다.
            return storeDTO.getUuid();
        }
        return res.getUuid();
    }
    
    //이렇게 회사마다 로그인을 구현할 필요가 없어졌다. 클라이언트에서 알아서 해 줄 것임. 데이터 형식만 하나 정해주면 된다.!
//    @GetMapping("login/kakao")
//    public void kakaoLogin() {
//    }
    
    
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
