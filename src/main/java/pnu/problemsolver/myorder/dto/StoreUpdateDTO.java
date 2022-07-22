package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * 가게 저장, 수정하는 DTO
 * 저장, 수정시 같은 형식을 사용한다. 수정된 부분만 반영하는게 아니라 그냥 덮어쓰기함.
 * 편집모드로 구현했기 때문에 당연함.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreUpdateDTO {
    private UUID uuid;
    private byte[] mainImg; //base64인코딩 된 상태.

    private String name;//가게이름.

    private String description;

    private List<CakeSaveDTO> cakeList;

}

