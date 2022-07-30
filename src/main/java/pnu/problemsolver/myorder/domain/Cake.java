package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.CakeEditDTO;

import javax.persistence.*;
import java.nio.file.Path;
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
    @ManyToOne(fetch = FetchType.LAZY)//LAZY붙이면 메소드에 @Transactional붙여야한다!
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
        UUID storeUUID = cakeDTO.getStoreUUID();
    
        Cake cake = Cake.builder()
                .uuid(cakeDTO.getUuid())
                .store(storeUUID == null ? null : Store.builder().uuid(storeUUID).build())//이렇게 해야한다..
                .filePath(cakeDTO.getFilePath())
                .option(cakeDTO.getOption())
                .name(cakeDTO.getName())
                .description(cakeDTO.getDescription())
                .minPrice(cakeDTO.getMinPrice())
                .build();
        
        System.out.println("Cake.toEntity : " + cake);

        return cake;
    }

    public void setOnlyNotNull(CakeEditDTO d, Path filePath) {
        UUID tmpUUID = d.getUuid();
        uuid = tmpUUID == null ? uuid : tmpUUID;


        String tmpName = d.getName();
        name = tmpName == null ? name : tmpName;


        String tmpDesc = d.getDescription();
        description = tmpDesc == null ? description : tmpDesc;

        int tmpMinPrice = d.getMinPrice();
        minPrice = tmpMinPrice == 0 ? minPrice : tmpMinPrice;

        String tmpOption = d.getOption();
        option = tmpOption == null ? option : tmpOption;

        String tmpPath =filePath.toString();

        this.filePath = tmpPath == null ? this.filePath : tmpPath;
    }

}
