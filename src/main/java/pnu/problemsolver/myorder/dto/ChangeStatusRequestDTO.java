package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStatusRequestDTO {
//	UUID storeId; //TODO : 사실 필요는 없는데 검사하고 바꾸는게 맞지 않을까?...
	UUID demandId;
	DemandStatus changeStatusTo;
}
