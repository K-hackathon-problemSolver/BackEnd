package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.mariadb.jdbc.internal.com.read.resultset.ColumnDefinition;

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
@Table(name = "store")
@Entity
public class Store {

    @Id
    @Column(columnDefinition = "VARCHAR(20)") //db에 check로 값이 설정되지는 않는다.
    private String id;

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
}
