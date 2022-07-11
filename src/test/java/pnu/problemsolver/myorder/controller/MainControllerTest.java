package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.MemberType;
import pnu.problemsolver.myorder.repository.StoreRepository;
import pnu.problemsolver.myorder.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc//MVC와 통합테스트 같이하고 싶을 떄 사용.
class MainControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    StoreRepository storeRepository;

//    @Autowired
//    JwtTokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void index() throws Exception {
        mvc.perform(get("/"))
                .andDo(print());
    }

    @Test
    void login() throws Exception {


//        objectMapper.
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("email", "testEmail");
        bodyMap.put("memberType", "store");//pw를 보내는 것이 아니라 memberType을 대문자로 보내야한다.
        String bodyStr= objectMapper.writeValueAsString(bodyMap);

        System.out.println(bodyStr);

        Store store = Store.builder()
                .email("testEmail")
                .pw("testPW")
                .location("myloc")
                .name("빵집")
                .owner_phone_num("324")
                .build();

        storeRepository.save(store);
        System.out.println("save!!!!");
//
        mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(bodyStr))
                .andDo(print());

    }

//    @Test
//    public void enumTest() {
//        MemberType type = MemberType.valueOf("store");
//        System.out.println(type);
//    }
}