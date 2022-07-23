package pnu.problemsolver.myorder.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CakeEditDTO {
    private UUID uuid;

    private byte[] img;
    private String extension;
    private String name;//케잌이름.
    private String description;
    private int minPrice;

    //json옵션
    private String option;

}