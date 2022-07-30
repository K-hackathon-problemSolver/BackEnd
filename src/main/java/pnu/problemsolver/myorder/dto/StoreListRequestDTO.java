package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.constant.PusanLocation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreListRequestDTO {
	private PusanLocation location;
	private int offset;
	private int limit;
}
