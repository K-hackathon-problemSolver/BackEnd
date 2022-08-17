package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.domain.constant.SNSType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralOAuthDTO { //없는 것은 안보내도 된다.
    String email;
    String name;
    String phone_num;
    String snsIdentifyKey; //고유 식별 번호.
    SNSType snsType;
    String birthday;
    Gender gender;
    MemberType memberType;
    int birthyear;
    int age;
    
    

}
