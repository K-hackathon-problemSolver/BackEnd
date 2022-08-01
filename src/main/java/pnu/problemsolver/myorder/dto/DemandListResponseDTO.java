package pnu.problemsolver.myorder.dto;

import lombok.*;
import pnu.problemsolver.myorder.domain.Demand;

import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandListResponseDTO {
	private UUID uuid;
    
//    private Customer customer;
    
    private String cakeName;
    
	//status도 필요없음.
//    private DemandStatus status;
    
    private String option;

//    private String description; //json에 포함된다.
    
    private int price;
    
	//문자열만 보내기 때문에 없어도 된다.
//    private String filePath;
//    private byte[] file;
//	private String extension;
	
	public static DemandListResponseDTO toDTO(Demand d) {
		DemandListResponseDTO dto = DemandListResponseDTO.builder()
				.uuid(d.getUuid())
				.cakeName(d.getCake().getName())
//				.status(d.getStatus())
				.option(d.getOption())
				.price(d.getPrice())
				.build();
		return dto;
	}
	
}
