package pnu.problemsolver.myorder.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.SNSType;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreEditDTO;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@ToString

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store extends BaseTimeEntitiy {
	
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID uuid;
	
	@Column(columnDefinition = "VARCHAR(30)") //db에 check로 값이 설정되지는 않는다. enail이 낫겠는데?
	private String email;

//    @Column(columnDefinition = "VARCHAR(20)")
//    private String pw;
	
	@Column(columnDefinition = "VARCHAR(30)")
	private String name;
	
	//기본값은 VARCHAR(255)
	private String description;
	
	//기본값은 VARCHAR(255)
	private String location;
	
	@Column(columnDefinition = "VARCHAR(15)")
	private String store_phone_num;
	
	@Column(columnDefinition = "VARCHAR(15)")
	private String owner_phone_num;
	
	@Column(columnDefinition = "JSON")
	private String impossibleDate;
	
	//위도 : 북쪽, 남쪽으로 얼마나
	private double latitude;
	
	//경도 : 동, 서쪽으로 얼마나?
	private double longitude;
	
	private String filePath;
	
	//bank랑 account는 못받는다.!
//    private String account;
//    @Column(columnDefinition = "varchar(30)")
//    private String bank;
	
	@Enumerated(EnumType.STRING) //이게 없으면 숫자로 들어간다. 그리고 이건 성능이 안좋다고 함. 그래도 써야한다.
	private SNSType snsType; //enum바로 사용 가능!
	private String snsIdentifyKey; //고유식별자.
	
	private int birthYear;
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@JsonFormat(pattern = "HH:mm")
	private LocalTime openTime;
	@JsonFormat(pattern = "HH:mm")
	
	private LocalTime closeTime;
	
	public static Store toEntity(StoreDTO storeDTO) {
		Store store = Store.builder()
				.uuid(storeDTO.getUuid())
				.email(storeDTO.getEmail())
				.name(storeDTO.getName())
				.description(storeDTO.getDescription())
				.location(storeDTO.getLocation())
				.store_phone_num(storeDTO.getStore_phone_num())
				.owner_phone_num(storeDTO.getOwner_phone_num())
				.impossibleDate(storeDTO.getImpossibleDate())
				.latitude(storeDTO.getLatitude())
				.longitude(storeDTO.getLongitude())
				.filePath(storeDTO.getFilePath())
				.snsType(storeDTO.getSnsType())
				.snsIdentifyKey(storeDTO.getSnsIdentifyKey())
				.birthYear(storeDTO.getBirthYear())
				.gender(storeDTO.getGender())
				.openTime(storeDTO.getOpenTime())
				.closeTime(storeDTO.getCloseTime())
				.build();
		
		return store;
	}
	
	public void setOnlyNotNull(StoreEditDTO d, Path filePath) {
		UUID tmpUUID = d.getUuid();
		uuid = tmpUUID == null ? uuid : tmpUUID;
		
		String tmpPath = filePath.toString();
		this.filePath = tmpPath == null ? this.filePath : tmpPath;
		
		String tmpName = d.getName();
		name = tmpName == null ? name : tmpName;
		
		String tmpDesc = d.getDescription();
		description = tmpDesc == null ? description : tmpDesc;
	}
}
