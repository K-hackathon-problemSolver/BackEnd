package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
@ToString

@NoArgsConstructor
@AllArgsConstructor
@Getter
//@Setter
@Builder
@Entity
public class Customer {
    @Id
    private String email;//email이 낫겠는데?

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone_num;
}
