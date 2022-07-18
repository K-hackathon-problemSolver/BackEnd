package pnu.problemsolver.myorder.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

public class Mapper {
    public static ObjectMapper objectMapper = new ObjectMapper();
    public static ModelMapper modelMapper = new ModelMapper();

    static {
//        modelMapper.addConverter(Converter);
    }
}