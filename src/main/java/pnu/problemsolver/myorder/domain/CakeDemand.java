package pnu.problemsolver.myorder.domain;

import lombok.*;

import javax.persistence.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Setter
@Getter
@Builder
@Entity
public class CakeDemand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //= AI
    private Long id;

//    @Column(nullable = false) //db에 check로 값이 설정되지는 않는다.

    @ManyToOne(fetch = FetchType.LAZY)//LAZY붙이면 메소드에 @Transactional붙여야한다!
    private Store store;


//    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)

    private Demand demand;


    private String file_path;

    @Column(columnDefinition = "JSON") //내부적으로는 LONGTEXT로 동작.
    private String option;//사용자가 선택한 옵션.

    @Column(nullable = false)
    private String name;

    private String description;//예약어는 사용못함.

    @Column(nullable = false)
    private int min_price;
}
