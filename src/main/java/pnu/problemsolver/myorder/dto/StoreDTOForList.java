package pnu.problemsolver.myorder.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class StoreDTOForList {
    private UUID uuid;
    private String name;//가게이름.
    private byte[] mainImg;
    private int minPrice;

}
