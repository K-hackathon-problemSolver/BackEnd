package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandListRequestDTO {
	UUID uuid; //customer, store의 uuid
	int size; //한번에 몇개씩 요청?
	int page; //몇번째 페이지?
	String sort; //정렬기준{기본값은 최신순}
	
}
