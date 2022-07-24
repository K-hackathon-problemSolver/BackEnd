package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.SNSType;

import javax.persistence.Column;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private UUID uuid;

    private String email;

    private String pw;

    private String name;

    private String phone_num;
    private int age;//age같은 것은 아무래도 통계에서 잘 사용되기 때문에 가지고 있는 것이 좋다.
    private SNSType snsType;
    private String snsIdentifyKey; //고유식별자.


    public static CustomerDTO toDTO(Customer c) {
        CustomerDTO dto = CustomerDTO.builder()
                .uuid(c.getUuid())
                .email(c.getEmail())
                .pw(c.getPw())
                .name(c.getName())
                .phone_num(c.getPhone_num())
                .snsType(c.getSnsType())
                .snsIdentifyKey(c.getSnsIdentifyKey())
                .age(c.getAge())
                .build();
        return dto;

    }

    public static CustomerDTO NaverOAuthDTOToDTO(NaverOAuthDTO c) {
         CustomerDTO customerDTO= CustomerDTO.builder()
                .email(c.getEmail())
                .name(c.getName())
                .phone_num(c.getMobile())
                .snsType(SNSType.NAVER)
                .snsIdentifyKey(c.getId())
                .age(c.getAge())
                .build();

        return customerDTO;

    }
}
