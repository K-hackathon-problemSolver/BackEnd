package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.DemandStatus;

import javax.persistence.Column;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class DemandDTO {
    private UUID uuid;

    private UUID customerUUID;

    private UUID cakeUUID;

    private DemandStatus status;

    private String option;//json

    private int price;

    private String filePath;

}
