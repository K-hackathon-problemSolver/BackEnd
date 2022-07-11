package pnu.problemsolver.myorder.domain;


import lombok.*;

import javax.persistence.*;

@ToString

@NoArgsConstructor
@AllArgsConstructor
@Builder

@Getter
//@Setter
@Entity
public class Demand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customer_id;

    private String store_id;

    @Column(nullable = false)
    private int status;

    @Column(columnDefinition = "JSON", nullable = false)
    private String option;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int min_price;

}
