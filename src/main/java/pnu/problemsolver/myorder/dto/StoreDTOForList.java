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
public class StoreDTOForList {
    private UUID uuid;
    private String name;//가게이름.
    private byte[] mainImg;
    private int minPrice;

}
