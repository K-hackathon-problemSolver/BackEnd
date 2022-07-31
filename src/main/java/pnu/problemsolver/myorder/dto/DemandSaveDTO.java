package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandSaveDTO {
	private UUID uuid;
	
	private UUID customerUUID;
	
	private UUID cakeUUID;
	
//	private DemandStatus status;
	
	private String option;//json
	
	private int price;
	
	private byte[] file;
	private String extension;//file과 함께 항상 확장자가 있어야 한다.
	
	
}
