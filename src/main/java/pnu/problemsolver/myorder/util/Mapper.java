package pnu.problemsolver.myorder.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.LinkedHashMap;
import java.util.Map;


public class Mapper {
    public static ObjectMapper objectMapper= new ObjectMapper();
    static {
        objectMapper.registerModule(new JavaTimeModule());//jackson에서 LocalDateTime을 처리하기 위함. 의존성 넣어줘야 쓸 수 있다.
    }
}