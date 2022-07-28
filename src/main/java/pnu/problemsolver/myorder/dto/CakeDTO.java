package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CakeDTO {

    private UUID uuid;

    //얘들을 남겨놓나?..일단 남겨둬 보자. 일단 Entity를 참조하는 것은 말이 안됨.
    private UUID storeUUID;

//    private UUID demandUUID;

    private String filePath;

    private String option;//제공 가능한 옵션.

    private String name;

    private String description;//desc로 예약어는 사용못함.

    private int minPrice;

    public static CakeDTO toDTO(Cake cake) {
        CakeDTO cakeDTO = CakeDTO.builder()
                .uuid(cake.getUuid())
                .storeUUID((cake.getStore().getUuid()))
//                .demandUUID((cake.getDemand().getUuid()))
                .filePath(cake.getFilePath())
                .option(cake.getOption())
                .name(cake.getName())
                .description(cake.getDescription())
                .minPrice(cake.getMinPrice())
                .build();
        return cakeDTO;
    }
}
