package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.mariadb.jdbc.internal.com.read.resultset.ColumnDefinition;
import pnu.problemsolver.myorder.dto.StoreDTO;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@ToString

@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Store {

    @Id
    @Column(columnDefinition = "VARCHAR(20)") //db에 check로 값이 설정되지는 않는다. enail이 낫겠는데?
    private String email;

    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String pw;

    @Size(min = 1)
    @Column(columnDefinition = "VARCHAR(20)", nullable = false)
    private String name;

    //기본값은 VARCHAR(255)
    private String description;

    @Size(min = 1)
    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "VARCHAR(15)")
    private String store_phone_num;

    @Column(columnDefinition = "VARCHAR(11)", nullable = false)
    private String owner_phone_num;

    public static Store toEntity(StoreDTO storeDTO) {
        Store store = Store.builder()
                .email(storeDTO.getEmail())
                .pw(storeDTO.getPw())
                .name(storeDTO.getName())
                .description(storeDTO.getDescription())
                .location(storeDTO.getLocation())
                .store_phone_num(storeDTO.getStore_phone_num())
                .owner_phone_num(storeDTO.getOwner_phone_num())
                .build();
        return store;
    }
}
