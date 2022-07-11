package pnu.problemsolver.myorder.dto;

import lombok.*;
import pnu.problemsolver.myorder.domain.Store;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {

    private String email;

    private String pw;

    private String name;

    private String description;

    private String location;

    private String store_phone_num;

    private String owner_phone_num;

}
