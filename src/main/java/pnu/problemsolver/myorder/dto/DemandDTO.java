package pnu.problemsolver.myorder.dto;

import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;

import javax.persistence.Column;
import java.util.UUID;

public class DemandDTO {
    private UUID uuid;

    private UUID customerUUID;

    private UUID cakeUUID;

    private int status;

    private String option;//json

    private int price;

    private byte[] file;

}
