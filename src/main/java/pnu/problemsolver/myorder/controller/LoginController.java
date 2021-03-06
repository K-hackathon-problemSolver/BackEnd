package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.dto.NaverOAuthDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;
import pnu.problemsolver.myorder.util.Request;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//@Controller
@RestController//restController안에 @controller, @ResponseBody있다.
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final JwtTokenProvider tokenProvider;
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
        this.tokenProvider = tokenProvider;
        this.storeService = storeService;
        this.customerService = customerService;

        stateToken = environment.getProperty("OAuth.stateToken");
        redirectURL = environment.getProperty("OAuth.naver.redirectURL");
        clientID = environment.getProperty("OAuth.naver.clientID");
        secret = environment.getProperty("OAuth.naver.secret");
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping
    public StoreDTO saveStore(StoreDTO storeDTO) {
        return storeService.save(storeDTO);
    }

    @GetMapping("/login")
    public Map<String, String> login() {
        String encodedRedirectURL = null;
        Map<String, String> urlMap = new HashMap<>();

        try {
            encodedRedirectURL = URLEncoder.encode(redirectURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("/login에서 url encode 에러!");
        }
        urlMap.put("naver", "https://nid.naver.com/oauth2.0/authorize?client_id=i6vA823oE3F_9QtAonj6&response_type=code&redirect_uri="
                + encodedRedirectURL + "&state=" + stateToken);
//        System.out.println(urlMap.get("naver"));

        return urlMap;
    }

    @GetMapping("/login/naver")    //redirectURL
    public String loginNaver(HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getQueryString());

        String url = naverTokenURL + "?client_id="
                + clientID
                + "&client_secret="
                + secret
                + "&grant_type=authorization_code&state=" +
                stateToken +
                "&code=" +
                httpServletRequest.getParameter("code");

        System.out.println(url);

//        HttpHeaders headers = new HttpHeaders();
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = Request.restTemplate.getForEntity(url, String.class);
        log.info("body! : " + response.getBody());//accessToken, refreshToken 가져옴
        log.info("header! : " + response.getHeaders());
        Map<String, String> tokens;
        try {
            tokens = Mapper.objectMapper.readValue(response.getBody(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info("tokens! : " + tokens);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokens.get("access_token"));
        log.info("headers! : " + headers);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Map> map = Request.restTemplate.exchange(naverMemberInfoURL, HttpMethod.GET, entity, Map.class);//여기서 Map으로 맵핑.

//        log.info(map.getClass().toString());
//        log.info(map.getBody().getClass().toString());
//
//        log.info(map.toString());
//        log.info(map.getBody().toString());
//        log.info(map.getBody().keySet().toString());
        log.info(map.getBody().get("response").getClass().toString());//map에서 그대로 사용하면 나중에 또 문자열 출력해서 확인해야한다. 무조건 객체 만드는 것이 이득임. 관리, 유지보수가 편하다.
        Map memberInfoMap = (Map) map.getBody().get("response");
//        NaverOAuthDTO naverOAuthDTO = new NaverOAuthDTO(memberInfoMap);
        NaverOAuthDTO naverOAuthDTO = new NaverOAuthDTO(memberInfoMap);//맵퍼로 동작안하면 생성자로 만들어주면 됨.
        log.info(naverOAuthDTO.toString());

        //exchange에 Member정보가 들어있음.
        return "success";
    }

    @GetMapping("/test")
    public void tmp() {
        StoreDTO storeDTO = StoreDTO.builder()
                .email("test")
                .pw("testpw")
                .name("신민건")
                .owner_phone_num("123")
                .location("부산")
                .build();

        storeService.save(storeDTO);
        log.info("tmp()실행!");

    }
}
