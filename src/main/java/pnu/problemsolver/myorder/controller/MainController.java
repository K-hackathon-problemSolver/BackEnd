package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.dto.JWTRequest;
import pnu.problemsolver.myorder.dto.JWTResponse;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.StoreService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Controller
@RestController//restController안에 @controller, @ResponseBody있다.
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final JwtTokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;
    private final ObjectMapper objectMapper;


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping
    public StoreDTO saveStore(StoreDTO storeDTO) {
        return storeService.save(storeDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response, @RequestBody String jsonBody) {
        log.info("/login 까지 왔다!");
        JWTRequest jwtRequest = JWTRequest.getInstance(jsonBody, objectMapper);


        StoreDTO storeDto = storeService.findById(jwtRequest.getEmail());
        if (storeService.login(storeDto) == null) {
            return null;
        }
        JWTResponse jwtResponse = JWTResponse.builder()
                .jwt(tokenProvider.createToken())
                .build();

        ResponseEntity<JWTResponse> res = new ResponseEntity<JWTResponse>(jwtResponse, HttpStatus.OK);
        return res;

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
