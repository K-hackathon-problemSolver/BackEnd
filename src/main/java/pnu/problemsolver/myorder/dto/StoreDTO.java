package pnu.problemsolver.myorder.dto;

import lombok.*;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Store;

import javax.persistence.Column;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDTO {
    private UUID uuid;

    private String email;

    private String pw;

    private String name;

    //기본값은 VARCHAR(255)
    private String description;

    //기본값은 VARCHAR(255)
    private String location;

    private String store_phone_num;

    private String owner_phone_num;

    private String impossibleDate;

    //위도 : 북쪽, 남쪽으로 얼마나
    private float latitude;

    //경도 : 동, 서쪽으로 얼마나?
    private float longitude;

    //여기서는 String이다. SaveDTO에서는 byte[]임.
    private String filePath;

    public static StoreDTO toDTO(Store s) {
        StoreDTO storeDTO = StoreDTO.builder()
                .uuid(s.getUuid())
                .email(s.getEmail())
                .pw(s.getPw())
                .name(s.getName())
                .description(s.getDescription())
                .location(s.getLocation())
                .store_phone_num(s.getStore_phone_num())
                .owner_phone_num(s.getOwner_phone_num())
                .impossibleDate(s.getImpossibleDate())
                .latitude(s.getLatitude())
                .longitude(s.getLongitude())
//                .filePath(s.getFilePath())
                .build();
        return storeDTO;
    }
}



