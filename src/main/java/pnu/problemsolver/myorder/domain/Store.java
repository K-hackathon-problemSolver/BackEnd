package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.mariadb.jdbc.internal.com.read.resultset.ColumnDefinition;
import pnu.problemsolver.myorder.dto.StoreDTO;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@ToString

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(columnDefinition = "VARCHAR(30)") //db에 check로 값이 설정되지는 않는다. enail이 낫겠는데?
    private String email;

    @Column(columnDefinition = "VARCHAR(20)")
    private String pw;

    @Size(min = 1)
    @Column(columnDefinition = "VARCHAR(30)")
    private String name;

    //기본값은 VARCHAR(255)
    private String description;

    //기본값은 VARCHAR(255)
    private String location;

    @Column(columnDefinition = "VARCHAR(15)")
    private String store_phone_num;

    @Column(columnDefinition = "VARCHAR(15)")
    private String owner_phone_num;

    @Column(columnDefinition = "JSON")
    private String impossibleDate;

    //위도 : 북쪽, 남쪽으로 얼마나
    private float latitude;

    //경도 : 동, 서쪽으로 얼마나?
   private float longitude;


    public static Store toEntity(StoreDTO storeDTO) {
        Store store = Store.builder()
                .email(storeDTO.getEmail())
                .pw(storeDTO.getPw())
                .name(storeDTO.getName())
                .description(storeDTO.getDescription())
                .location(storeDTO.getLocation())
                .store_phone_num(storeDTO.getStore_phone_num())
                .owner_phone_num(storeDTO.getOwner_phone_num())
                .impossibleDate(storeDTO.getImpossibleDate())
                .uuid(storeDTO.getUuid())
                .build();

        return store;
    }
}
