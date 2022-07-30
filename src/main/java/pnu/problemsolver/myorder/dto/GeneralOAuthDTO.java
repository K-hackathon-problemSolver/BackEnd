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
    String snsIdentifyKey; //고유 식별 번호.
    SNSType snsType;
    MemberType memberType;
    Gender gender;
    String email;
    String phone_num;
    String name;
    String birthday;
    int birthyear;
    int age;

}
