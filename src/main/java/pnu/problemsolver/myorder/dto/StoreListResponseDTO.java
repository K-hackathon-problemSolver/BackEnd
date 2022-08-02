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
    private String extension;
//    private int minPrice;

    public static StoreListResponseDTO toDTO(Store s) {
    //extract extension
        String filePath = s.getFilePath();
        int i = filePath.lastIndexOf(".");
        String extension = filePath.substring(i + 1);
    
        StoreListResponseDTO dto = StoreListResponseDTO.builder()
                .uuid(s.getUuid())
                .name(s.getName())
                .extension(extension)
                .build();
        File file = new File(filePath);
        byte[] mainImg=null;
        try {
            mainImg = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            e.printStackTrace();
            log.error("no file exists()!! ABSOLUTE PATH : " + file.toPath().toAbsolutePath());
        }
        dto.setMainImg(mainImg);
        return dto;
    }
}
