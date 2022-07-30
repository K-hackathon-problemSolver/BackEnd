package pnu.problemsolver.myorder.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pnu.problemsolver.myorder.domain.Store;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class StoreListResponseDTO {
    private UUID uuid;
    private String name;//가게이름.
    private byte[] mainImg;//base64 인코딩 된 상태.
//    private int minPrice;

    public static StoreListResponseDTO toDTO(Store s) {
        StoreListResponseDTO dto = StoreListResponseDTO.builder()
                .uuid(s.getUuid())
                .name(s.getName())
                .build();
        File file = new File(s.getFilePath());
        byte[] mainImg=null;
        try {
            mainImg = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            e.printStackTrace();
            log.error("no file exists()!!");
        }
        dto.setMainImg(mainImg);
        return dto;
    }
}
