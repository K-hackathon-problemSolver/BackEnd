package pnu.problemsolver.myorder.dto;

import lombok.*;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandListDTO {
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
	
	public static DemandListDTO toDTO(Demand d) {
		DemandListDTO dto = DemandListDTO.builder()
				.uuid(d.getUuid())
				.cakeName(d.getCake().getName())
//				.status(d.getStatus())
				.option(d.getOption())
				.price(d.getPrice())
				.build();
		return dto;
	}
	
}
