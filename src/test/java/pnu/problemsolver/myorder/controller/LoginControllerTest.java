package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.repository.StoreRepository;
import pnu.problemsolver.myorder.util.Mapper;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc//MVC와 통합테스트 같이하고 싶을 떄 사용.
class LoginControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    StoreRepository storeRepository;

//    @Autowired
//    JwtTokenProvider tokenProvider;

    @Test
    void index() throws Exception {
        mvc.perform(get("/"))
                .andExpect(content().string("index"));

    }

//    @Test
//    void login() throws Exception {
//
//
////        objectMapper.
//        Map<String, String> bodyMap = new HashMap<>();
//        bodyMap.put("email", "testEmail");
//        bodyMap.put("memberType", "store");//pw를 보내는 것이 아니라 memberType을 대문자로 보내야한다.
//        String bodyStr= objectMapper.writeValueAsString(bodyMap);
//
//        System.out.println(bodyStr);
//
//        Store store = Store.builder()
//                .email("testEmail")
//                .pw("testPW")
//                .location("myloc")
//                .name("빵집")
//                .owner_phone_num("324")
//                .build();
//
//        storeRepository.save(store);
//        System.out.println("save!!!!");
////
//        mvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(bodyStr))
//                .andDo(print());
//
//    }
//
//

    @Test
    public void JSONparesTest() {//계층구조를 가지고 있어서 맵 잘 된다.
        String json = "{\"resultcode\":\"00\", \"message\":\"success\", \"response\":{\"id\":\"51ICfhCQb2yZ5P8B2zR1XrACj-O8aLaUSxljfFZ52g4\", \"age\":\"20-29\"}}";
        Map<Object, Object> map;
        try {
            map = Mapper.objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println(map.get("response").getClass());
        System.out.println(map.get("response"));

    }
}