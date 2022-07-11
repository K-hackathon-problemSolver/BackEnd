//package pnu.problemsolver.myorder.controller.converter;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.tomcat.util.json.JSONParser;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.stereotype.Component;
//import pnu.problemsolver.myorder.dto.JWTRequest;
//import pnu.problemsolver.myorder.domain.MemberType;
//
//import java.lang.annotation.Annotation;
//import java.util.Map;
//
//
//@RequiredArgsConstructor
//@Slf4j
//@Component
//public class StringToJWTRequestConverter implements Converter<String, JWTRequest> {
//
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public JWTRequest convert(String source) {
//        log.info("converter!!");
//        Map<String, String> map;
//        try {
//            map = objectMapper.readValue(source, Map.class);
//        } catch (JsonProcessingException e) {
//            log.error("StringToJWTRequestConverter 에러!");
//            throw new RuntimeException(e);
//        }
//        JWTRequest jwtRequest = new JWTRequest();
//        jwtRequest.setEmail(map.get("email")) ;
//        log.info(map.get("memberType").toUpperCase());
//        jwtRequest.setMemberType(MemberType.valueOf(map.get("memberType").toUpperCase()));
//
//        return jwtRequest;
//
//    }
//}
