package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.SNSType;
import pnu.problemsolver.myorder.dto.CustomerDTO;
import pnu.problemsolver.myorder.dto.GeneralOAuthDTO;

import javax.persistence.*;
import java.util.UUID;

@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
//@Setter
@Builder
@Entity
public class Customer extends BaseTimeEntitiy {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID uuid;
	
	private String email;
	//    private String pw;
	private String name;
	
	private String phone_num;
	
	@Enumerated(EnumType.STRING)
	private SNSType snsType;
	
	private String snsIdentifyKey; //고유식별자.
	
	private int birthYear;//age같은 것은 아무래도 통계에서 잘 사용되기 때문에 가지고 있는 것이 좋다.
	
	@Enumerated(EnumType.STRING)
	private Gender gender; //1 : 남자. 0 : 여자.
	
	private String fcmToken;
	
	
	public static Customer toEntity(CustomerDTO c) {
		Customer customer = Customer.builder()
				.uuid(c.getUuid())
				.email(c.getEmail())
				.name(c.getName())
				.phone_num(c.getPhone_num())
				.snsType(c.getSnsType())
				.snsIdentifyKey(c.getSnsIdentifyKey())
				.birthYear(c.getBirthYear())
				.build();
		return customer;
		
	}
	
	public static Customer toEntity(GeneralOAuthDTO dto) {
		Customer customer = Customer.builder()
				.email(dto.getEmail())
				.name(dto.getName())
				.phone_num(dto.getPhone_num())
				.snsType(dto.getSnsType())
				.snsIdentifyKey(dto.getSnsIdentifyKey())
				.birthYear(dto.getBirthyear())
				.build();
		
		return customer;
		
	}
	
	
	public void setFcmToken(String t) {
		fcmToken = t;
	}
	
	
}
