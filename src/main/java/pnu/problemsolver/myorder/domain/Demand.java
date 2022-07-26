package pnu.problemsolver.myorder.domain;


import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Cake cake;

    @Column(nullable = false) //0 : 주문대기, 1 : 승락, 2 : 완료, -1 : 거절
    private int status;

    @Column(columnDefinition = "JSON")
    private String option;
//
//    @Column(nullable = false)
//    private String name; //케이크이름
//
//    private String description;

    @Column(nullable = false)
    private int price;

    private String fillPath;

    public void acceptDemand() {
        if (status == 0) {
            this.status = 1;
        }

    }
    public void rejectDemand() {
        if (status == 0) {
        this.status = -1;
        }
    }
    public void completeDemand() {
        if (status == 1) {
        this.status = 2;
        }
    }


}
