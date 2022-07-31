package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Store;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CakeDTO {


    //얘들을 남겨놓나?..일단 남겨둬 보자. 일단 Entity를 참조하는 것은 말이 안됨.
    
    private UUID uuid;
    private UUID storeUUID;

//    private UUID demandUUID;

    private String filePath;

    private String option;//제공 가능한 옵션.

    private String name;

    private String description;//desc로 예약어는 사용못함.

    private int minPrice;

    public static CakeDTO toDTO(Cake cake) {
        Store store = cake.getStore();
    
        CakeDTO cakeDTO = CakeDTO.builder()
                .uuid(cake.getUuid())
                .storeUUID(store == null ? null : store.getUuid())//getUuid()호출하면 NPE뜬다..
//                .demandUUID((cake.getDemand().getUuid()))
                .filePath(cake.getFilePath())
                .option(cake.getOption())
                .name(cake.getName())
                .description(cake.getDescription())
                .minPrice(cake.getMinPrice())
                .build();
        return cakeDTO;
    }

    public static CakeDTO toDTO(CakeEditDTO d, String filePath, UUID storeUUID) {

        CakeDTO cakeDTO = CakeDTO.builder()
                .uuid(d.getUuid())
                .storeUUID(storeUUID)
                .filePath(filePath)
                .option(d.getOption())
                .name(d.getName())
                .description(d.getDescription())
                .minPrice(d.getMinPrice())
                .build();
        return cakeDTO;
    }
}
