package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.SNSType;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private UUID uuid;

    private String email;


    private String name;

    private String phone_num;
    private SNSType snsType;
    private String snsIdentifyKey; //고유식별자.
    private int birthYear;//age같은 것은 아무래도 통계에서 잘 사용되기 때문에 가지고 있는 것이 좋다.
    private Gender gender; //1 : 남자. 0 : 여자.


    public static CustomerDTO toDTO(Customer c) {
        CustomerDTO dto = CustomerDTO.builder()
                .uuid(c.getUuid())
                .email(c.getEmail())
                .name(c.getName())
                .phone_num(c.getPhone_num())
                .snsType(c.getSnsType())
                .snsIdentifyKey(c.getSnsIdentifyKey())
                .birthYear(c.getBirthYear())
                .gender(c.getGender())
                .build();
        return dto;

    }

//    public static CustomerDTO NaverOAuthDTOToDTO(NaverOAuthDTO c) {
//         CustomerDTO customerDTO= CustomerDTO.builder()
//                .email(c.getEmail())
//                .name(c.getName())
//                .phone_num(c.getMobile())
//                .snsType(SNSType.NAVER)
//                .snsIdentifyKey(c.getId())
//                .age(c.getAge())
//                .build();
//
//        return customerDTO;
//
//    }


    public static CustomerDTO GeneralOAuthDTOtoDTO(GeneralOAuthDTO d) {
        return CustomerDTO.builder()
                .email(d.getEmail())
                .name(d.getName())
                .phone_num(d.getPhone_num())
                .birthYear(d.getBirthyear())
                .snsType(d.getSnsType())
                .snsIdentifyKey(d.getSnsIdentifyKey())
                .build();

    }
}
