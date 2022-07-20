package pnu.problemsolver.myorder.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import pnu.problemsolver.myorder.dto.NaverOAuthDTO;

import java.util.LinkedHashMap;
import java.util.Map;


public class Mapper {
    public static ObjectMapper objectMapper;
    public static ModelMapper modelMapper;

    static {
        objectMapper = new ObjectMapper();
        modelMapper = new ModelMapper();

        //Map으로 캐스팅하라니까 자동으로 LinkedHashMap을 사용하더라.
        modelMapper.typeMap(LinkedHashMap.class, NaverOAuthDTO.class).addMappings(mapper -> {
            mapper.map(hashMap -> hashMap.get("age"), (destination, value) -> destination.setAgeRange((String) value));
            mapper.map(hashMap -> hashMap.get("birthyear"), ((destination, value) -> destination.setBirthyear(Integer.parseInt((String) value))));//타입변환도 굳이 converter안쓰고 이렇게 가능하다.
        });
    }
}