package pnu.problemsolver.myorder.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class JWTRequest {
        private String email;
        private MemberType memberType;
//        private String pw; //있으면 안됌.

        /**
         *
         * @param json
         * @param objectMapper static 메소드라서 DI받지 못한다. 어쩔수 없이 매개변수로 받아서 사용함.
         * @return 실패하면 null반환. 항상 올바른 요청이 온다는 보장이 없다.
         */
        public static JWTRequest getInstance(String json, ObjectMapper objectMapper)  {
                try {
                        Map<String, String> map = objectMapper.readValue(json, Map.class);
                        JWTRequest jwtRequest = new JWTRequest();
                        jwtRequest.setEmail(map.get("email"));
                        jwtRequest.setMemberType(MemberType.valueOf(map.get("memberType")));
                        return jwtRequest;
                } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        log.error("JWTRequest.getInstacne()에러!");
                }
                return null;


        }
}
//여기서만 쓰인다.
enum MemberType {
        CUSTOMER, STORE
}
