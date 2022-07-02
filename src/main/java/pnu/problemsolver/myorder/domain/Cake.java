package pnu.problemsolver.myorder.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "cake")
@Entity
public class Cake {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //= AI
    private Long id;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false) //db에 check로 값이 설정되지는 않는다.
    private String store_id;

    @Column(nullable = false)
    private Long order_id;


    private String file_path;

    @Column(columnDefinition = "JSON") //내부적으로는 LONGTEXT로 동작.
    private String option;

    @Column(nullable = false)
    private String name;

    private String description;//예약어는 사용못함.

    @Column(nullable = false)
    private int min_price;
}
