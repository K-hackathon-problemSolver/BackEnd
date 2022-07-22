package pnu.problemsolver.myorder.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@ToString

@NoArgsConstructor
@AllArgsConstructor
@Getter
//@Setter
@Builder
@Entity
public class Customer extends BaseTimeEntitiy{
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID uuid;

    private String email;

    private String pw;

    private String name;

    private String phone_num;

    private String sns;
}
