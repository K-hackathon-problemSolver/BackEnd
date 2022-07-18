package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private UUID uuid;

    private String email;

    private String pw;

    private String name;

    private String phone_num;
}
