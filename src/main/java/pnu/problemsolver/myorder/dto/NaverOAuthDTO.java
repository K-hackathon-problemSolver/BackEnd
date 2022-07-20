package pnu.problemsolver.myorder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.util.Mapper;

import java.util.Map;

@Data
@NoArgsConstructor
//{id=51ICfhCQb2yZ5P8B2zR1XrACj-O8aLaUSxljfFZ52g4, age=20-29, gender=M, email=zhdhfhd33@gmail.com, mobile=010-3391-6486
// , mobile_e164=+821033916486, name=신민건, birthday=10-15, birthyear=1999}
public class NaverOAuthDTO {
    String id;
    String ageRange;
    String gender; //M(남자)/F(여자)
    String email;
    String mobile;
    String mobile_e164;
    String name;
    String birthday;
    int birthyear;
    int age;//내가 추가함.

    public NaverOAuthDTO(Map map) {
        NaverOAuthDTO naverOAuthDTO = Mapper.modelMapper.map(map, NaverOAuthDTO.class);
        naverOAuthDTO.setAgeRange((String) map.get("age"));


    }


}
