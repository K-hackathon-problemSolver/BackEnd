package pnu.problemsolver.myorder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor

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

    //{id=51ICfhCQb2yZ5P8B2zR1XrACj-O8aLaUSxljfFZ52g4, age=20-29, gender=M, email=zhdhfhd33@gmail.com, mobile=010-3391-6486
// , mobile_e164=+821033916486, name=신민건, birthday=10-15, birthyear=1999}
    public NaverOAuthDTO(Map map) {
        Map<String, String> newMap = new HashMap<>();
        for (Object i : map.entrySet()) {
            Map.Entry entry = (Map.Entry) i;
            newMap.put((String) entry.getKey(), (String) entry.getValue());
        }
        id = newMap.get("id");
        ageRange = newMap.get("age");
        gender = newMap.get("gener");
        email = newMap.get("email");
        mobile = newMap.get("mobile");
        mobile_e164 = newMap.get("mobile_e164");
        name = newMap.get("name");
        birthday = newMap.get("birthday");
        birthyear = Integer.parseInt(newMap.get("birthyear"));
    }

}
