package pnu.problemsolver.myorder.domain;


import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(value = {AuditingEntityListener.class})
@ToString
abstract class BaseTimeEntitiy {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime created;

    @LastModifiedDate
     protected LocalDateTime modified;

}
