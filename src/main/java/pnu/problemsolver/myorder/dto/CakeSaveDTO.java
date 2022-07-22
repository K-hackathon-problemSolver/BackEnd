package pnu.problemsolver.myorder.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CakeSaveDTO {
    private byte[] img;
    private String name;//케잌이름.
    private int minPrice;

    //json옵션
    private String option;

}