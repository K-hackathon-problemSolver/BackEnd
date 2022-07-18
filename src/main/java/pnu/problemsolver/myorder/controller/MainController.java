package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

//@Controller
@RestController//restController안에 @controller, @ResponseBody있다.
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final JwtTokenProvider tokenProvider;
    private final StoreService storeService;


    private final String clientID = "i6vA823oE3F_9QtAonj6";
    private final String secret = "6U0jc_RTWC";
    private final String redirectURL = "http://localhost/login/naver";
    String stateToken = "UE5VLXpoZGhmaGQzMy1rLWhhY2thdGhvbg==";

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping
    public StoreDTO saveStore(StoreDTO storeDTO) {
        return storeService.save(storeDTO);
    }

    @GetMapping("/login")
    public String loginNaver() {

        String encodedRedirectURL = null;

        try {
            encodedRedirectURL = URLEncoder.encode(redirectURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return "https://nid.naver.com/oauth2.0/authorize?client_id=i6vA823oE3F_9QtAonj6&response_type=code&redirect_uri="
                + encodedRedirectURL + "&state=" + stateToken;
    }

    //    @PostMapping("/login/naver")
    @GetMapping("/login/naver")
    public String loginNaver(HttpServletRequest httpServletRequest) {
        System.out.println(httpServletRequest.getQueryString());

        String url = "https://nid.naver.com/oauth2.0/token?client_id="
                + clientID
                + "&client_secret="
                + secret
                + "&grant_type=authorization_code&state=" +
                stateToken +
                "&code=" +
                httpServletRequest.getParameter("code");

//        HttpHeaders headers = new HttpHeaders();
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        HttpEntity<?> httpEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("body! : "+response.getBody());//accessToken, refreshToken 가져옴
        System.out.println("header! : "+response.getHeaders().toString());
        Map<String, String> tokens;
        try {
            tokens = Mapper.objectMapper.readValue(response.getBody(), Map.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("tokens : "+tokens);
        tokens.get("refresh_token");

        HttpHeaders headers1 = new HttpHeaders();
        headers1.setBearerAuth(tokens.get("access_token"));
        System.out.println("headers1! : "+headers1);
        HttpEntity entity = new HttpEntity(headers1);
        url = "https://openapi.naver.com/v1/nid/me";
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        System.out.println(exchange);
        return "success!";
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
