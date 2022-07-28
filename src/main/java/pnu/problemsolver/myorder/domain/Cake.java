package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.dto.CakeDTO;

import javax.persistence.*;
import java.util.UUID;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Entity
public class Cake {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    //    @Column(/*columnDefinition = "VARCHAR(20)", nullable = false*/) //db에 check로 값이 설정되지는 않는다.
    @ManyToOne(optional = false, fetch = FetchType.LAZY)//LAZY붙이면 메소드에 @Transactional붙여야한다!
    private Store store;

    //    @Column(nullable = false) ManyToOne에는 @Column못쓴다.!
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Demand demand;

    private String filePath;

    @Column(columnDefinition = "JSON") //내부적으로는 LONGTEXT로 동작.
    private String option;//제공 가능한 옵션.

    @Column(nullable = false)
    private String name;

    private String description;//desc로 예약어는 사용못함.

    @Column(nullable = false)
    private int minPrice;

    public static Cake toEntity(CakeDTO cakeDTO) {
        Cake cake = Cake.builder()
                .uuid(cakeDTO.getUuid())
                .store(Store.builder().uuid(cakeDTO.getStoreUUID()).build())
                .filePath(cakeDTO.getFilePath())
                .option(cakeDTO.getOption())
                .name(cakeDTO.getName())
                .description(cakeDTO.getDescription())
                .minPrice(cakeDTO.getMinPrice())
                .build();
        return cake;
    }

}
