package pnu.problemsolver.myorder.domain;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.dto.DemandDTO;

import javax.persistence.*;
import java.util.UUID;

@ToString

@NoArgsConstructor
@AllArgsConstructor
@Builder

@Getter
//@Setter
@Entity
public class Demand extends BaseTimeEntitiy {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    @ManyToOne(optional = false)
    private Customer customer;

    @ManyToOne(optional = false)
    private Cake cake;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DemandStatus status;


    @Column(columnDefinition = "JSON")
    private String option;

//    @Column(nullable = false)
//    private String name; //케이크이름
//
//    private String description; //json에 포함된다.

    @Column(nullable = false)
    private int price;

    private String filePath;

    public void acceptDemand() {
        if (status == DemandStatus.WAITING) {
            status = DemandStatus.ACCEPTED;
        }

    }

    public static Demand toEntity(DemandDTO dto) {
        Demand demand = Demand.builder()
                .uuid(dto.getUuid())
                .cake(Cake.builder().uuid(dto.getCakeUUID()).build())
                .customer(Customer.builder().uuid(dto.getCustomerUUID()).build())

                .status(dto.getStatus())
                .option(dto.getOption())

                .price(dto.getPrice())
                .filePath(dto.getFilePath())
                .build();
        return demand;
    }


}
