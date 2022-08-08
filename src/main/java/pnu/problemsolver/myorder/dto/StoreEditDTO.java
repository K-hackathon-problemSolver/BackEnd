package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pnu.problemsolver.myorder.domain.Store;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * 가게 저장, 수정하는 DTO
 * 저장, 수정시 같은 형식을 사용한다. 수정된 부분만 반영하는게 아니라 그냥 덮어쓰기함.
 * 편집모드로 구현했기 때문에 당연함.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class StoreEditDTO {
    private UUID uuid;
    private String mainImg; //base64인코딩 된 상태.
    private String extension; //byte파일의 확장자.

    private String name;//가게이름.

    private String description;

//    private String impossibleDate;

    private List<CakeEditDTO> cakeList;
    
    public static StoreEditDTO toDTO(Store s) {
        Path path = Paths.get(s.getFilePath());
        String bytes=null;
        try {
            bytes = Base64.getEncoder().encodeToString(Files.readAllBytes(path));//항상 까먹지말자!
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("파일이 존재하지 않습니다. : " + path.toAbsolutePath());
        }
        String fileName = path.getFileName().toString();
		int i = fileName.lastIndexOf(".");//파일이름에.가 들어갈 수 있기 때문에 lastIndexOf
		String extension = fileName.substring(i + 1);
    
        StoreEditDTO editDTO = StoreEditDTO.builder()
                .uuid(s.getUuid())
                .name(s.getName())
                .mainImg(bytes)
                .description(s.getDescription())
                .extension(extension)
                .build();
        return editDTO;
    }

}

