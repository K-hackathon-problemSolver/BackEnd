package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import java.nio.file.Path;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandDTO {
    private UUID uuid;

    private UUID customerUUID;

    private UUID cakeUUID;
    
    private UUID storeUUID;

    private DemandStatus status;

    private String option;//json

    private int price;

    private String filePath;
    
    public static DemandDTO toDTO(DemandSaveDTO d, Path filePath) {
        DemandDTO demandDTO = DemandDTO.builder()
                .customerUUID(d.getCustomerUUID())
                .cakeUUID(d.getCakeUUID())
                .option(d.getOption())
                .price(d.getPrice())
                .filePath(filePath.toString())
                .build();

        return demandDTO;
    }
    
    public static DemandDTO toDTO(Demand d) {
        DemandDTO demandDTO = DemandDTO.builder()
                .uuid(d.getUuid())
                .customerUUID(d.getCustomer().getUuid())
                .cakeUUID(d.getCake().getUuid())
                .storeUUID(d.getStore().getUuid())
                .status(d.getStatus())
                .option(d.getOption())
                .price(d.getPrice())
                .filePath(d.getFilePath())
                .build();
        
        return demandDTO;
    }

}
