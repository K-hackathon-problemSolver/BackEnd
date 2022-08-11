package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pnu.problemsolver.myorder.domain.Demand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class DemandDetailResponseDTO {
	
	private String img;
	
	
	public static DemandDetailResponseDTO toDTO(Demand d) {
			Path p = Paths.get(d.getFilePath());
		String bytes = null;
		try {
			bytes = Base64.getEncoder().encodeToString(Files.readAllBytes(p));
			
		} catch (IOException e) {
			log.error("Files.readAllBytes Exception!!");
			e.printStackTrace();
		}
		DemandDetailResponseDTO demandDetailResponseDTO = DemandDetailResponseDTO.builder()
				.img(bytes)
				.build();
		
		return demandDetailResponseDTO;
	}
}
