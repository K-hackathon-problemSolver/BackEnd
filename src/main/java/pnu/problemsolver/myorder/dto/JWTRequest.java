package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JWTRequest {
        private String email;
        private MemberType memberType;
//        private String pw; //있으면 안됌.
}
