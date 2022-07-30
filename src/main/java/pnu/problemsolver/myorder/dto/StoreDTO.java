package pnu.problemsolver.myorder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.SNSType;
import pnu.problemsolver.myorder.domain.Store;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
	private UUID uuid;
	
	private String email;

//    private String pw;
	
	private String name;
	
	//기본값은 VARCHAR(255)
	private String description;
	
	//기본값은 VARCHAR(255)
	private String location;
	
	private String store_phone_num;
	
	private String owner_phone_num;
	
	private String impossibleDate;
	
	//위도 : 북쪽, 남쪽으로 얼마나
	private double latitude;
	
	//경도 : 동, 서쪽으로 얼마나?
	private double longitude;
	
	//여기서는 String이다. SaveDTO에서는 byte[]임.
	private String filePath;
	
	private SNSType snsType;
	private String snsIdentifyKey;
	private int birthYear;
	private Gender gender;
	@JsonFormat(pattern = "HH:mm")
	
	private LocalTime openTime;//jackson은 LocalTime은 처리못해준다.
	@JsonFormat(pattern = "HH:mm")
	
	private LocalTime closeTime;
	
	
	public static StoreDTO toDTO(Store s) {
		StoreDTO storeDTO = StoreDTO.builder()
				.uuid(s.getUuid())
				.email(s.getEmail())
				.name(s.getName())
				.description(s.getDescription())
				.location(s.getLocation())
				.store_phone_num(s.getStore_phone_num())
				.owner_phone_num(s.getOwner_phone_num())
				.impossibleDate(s.getImpossibleDate())
				.latitude(s.getLatitude())
				.longitude(s.getLongitude())
//                .filePath(s.getFilePath())
				.build();
		return storeDTO;
	}
	
	public static StoreDTO GeneralOAuthDTOtoDTO(GeneralOAuthDTO d) {
		return StoreDTO.builder()
				.email(d.getEmail())
				.name(d.getName())
				.owner_phone_num(d.getPhone_num())
				.snsType(d.getSnsType())
				.snsIdentifyKey(d.getSnsIdentifyKey())
				.birthYear(d.getBirthyear())
				.build();
		
	}
	
	public static StoreDTO toDTO(StoreEditDTO d, String filePath) {
		return StoreDTO.builder()
				.uuid(d.getUuid())
				.name(d.getName())
				.description(d.getDescription())
				.filePath(filePath)
				.build();
		
	}
}



