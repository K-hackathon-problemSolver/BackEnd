package pnu.problemsolver.myorder.domain;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.dto.DemandSaveDTO;

import javax.persistence.*;
import java.nio.file.Path;
import java.util.UUID;

@ToString(callSuper = true)
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
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude //일단 기본으로 lazy설정해놓고 서비스 운영하면서 성능 최적화 하면 된다.!
    private Customer customer;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private Cake cake;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ToString.Exclude
    private Store store;
    
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
    
//    public void setCake(Cake cake) {
//        this.cake = cake;
//    }
    
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
    
    public void acceptDemand() {
        if (status == DemandStatus.WAITING) {
            status = DemandStatus.ACCEPTED;
        }
        
    }
    
    public static Demand toEntity(DemandDTO dto) {
        UUID storeUUID = dto.getStoreUUID();
        UUID customerUUID = dto.getCustomerUUID();
        UUID cakeUUID = dto.getCakeUUID();
    
    
        Demand demand = Demand.builder()
                .uuid(dto.getUuid())
                .cake(cakeUUID == null ? null : Cake.builder().uuid(cakeUUID).build())
                .customer(customerUUID == null ? null : Customer.builder().uuid(customerUUID).build())
                .store(storeUUID == null ? null : Store.builder().uuid(storeUUID).build())
                .status(dto.getStatus())
                .option(dto.getOption())
                .price(dto.getPrice())
                .filePath(dto.getFilePath())
                .build();
        return demand;
    }
    
//    public void setCreated(LocalDateTime ldt) {
//        created = ldt;
//    }

    
    //저장까지 여기서 하면 안된다. 너무 많은 기능을 함. 하나의 함수 = 하나의 기능.
    public static Demand toEntity(DemandSaveDTO d, Path filePath) {
        
        UUID customerUUID = d.getCustomerUUID();
        UUID cakeUUID = d.getCakeUUID();
        UUID storeUUID = d.getStoreUUID();
    
    
        Demand demand = Demand.builder()
                .customer(customerUUID == null ? null : Customer.builder().uuid(customerUUID).build())
                .cake(cakeUUID == null ? null : Cake.builder().uuid(cakeUUID).build())
                .store(storeUUID == null ? null : Store.builder().uuid(storeUUID).build())
                .option(d.getOption())
                .price(d.getPrice())
                .status(DemandStatus.WAITING)//처음에는 waiting이다.
                .filePath(filePath == null ? null : filePath.toString())
                .build();
        
        return demand;
        
    }
    public void setFilePath(Path path) {
        filePath = path.toString();
    }
    
    /**
     * 이렇게 용도에 따라 함수를 다르게 사용하는게 복잡성을 줄일 수 있다.
     * changeStatus(DemandStatus status) 이런 함수를 만들면 어디서 사용될지 모르고 잘못된 동작 유발 가능
     *
     * @return
     */
    public boolean changeToAccepted() {
        if (status == DemandStatus.WAITING) {
            System.out.println("테스트 : changeToAccepted! ");
            status = DemandStatus.ACCEPTED;
            return true;
        }
        return false;
    }
    
    public boolean changeToRejected() {
        
        if (status == DemandStatus.WAITING) {
            status = DemandStatus.REJECTED;
            return true;
        }
        return false;
    }
    
    public boolean changeToCompleted() {
        
        if (status == DemandStatus.ACCEPTED) {
            status = DemandStatus.COMPLETED;
            return true;
        }
        return false;
    }
    
    

    
    
}
