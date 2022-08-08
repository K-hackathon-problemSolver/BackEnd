package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pnu.problemsolver.myorder.domain.Cake;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class CakeEditDTO {
	private UUID uuid;
	
	private String img;
	private String extension;
	private String name;//케잌이름.
	private String description;
	private int minPrice;
	
	//json옵션
	private String option;
	
	public static CakeEditDTO toDTO(Cake c) {
		String filePath = c.getFilePath();
		Path path = Paths.get(filePath);
		String bytes = null;
		try {
//			bytes = Base64.getEncoder().encode(Files.readAllBytes(path));
			bytes = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
			
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("파일이 존재하지 않습니다. : " + path.toAbsolutePath());//디버깅할 때에는 absolutePath정보가 유의미하다.!
			
		}
		String fileName = path.getFileName().toString();
		int i = fileName.lastIndexOf(".");
		String extension = fileName.substring(i + 1);
		
		
		CakeEditDTO dto = CakeEditDTO.builder()
				.uuid(c.getUuid())
				.name(c.getName())
				.description(c.getDescription())
				.minPrice(c.getMinPrice())
				.img(bytes)
				.extension(extension)
				.option(c.getOption())
				.build();
		return dto;
	}
	
}