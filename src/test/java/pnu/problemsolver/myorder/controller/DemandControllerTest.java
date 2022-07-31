package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.dto.DemandSaveDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest
@MockBean(JpaMetamodelMappingContext.class)//jpaAuditing때문에 해줘야함.
class DemandControllerTest {
	
	@Autowired
	MockMvc mvc;
	
	@MockBean
	DemandService demandService;
	
	 //@Controller가 붙은 빈만 생성하기 때문에 직접적으로 JwtTokenProvider를 사용하지 않더라도 @MockBean주입해야한다.
    @MockBean
	 JwtTokenProvider jwtTokenProvider;
	
	@MockBean
	StoreService storeService;
	
	@MockBean
	CustomerService customerService;
	
	@MockBean
	CakeService cakeService;
	
	
	@Test
	public void saveDemandTest() throws Exception {
		File file = new File("src/main/resources/static/testPicture.jpg");
		assertEquals(file.exists(), true);
		byte[] bytes;
		try {
			bytes = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
			
		} catch (IOException e) {
			throw new RuntimeException("Files.readAllBytes Exception!!");
		}
		UUID cakeUUID = UUID.randomUUID();
		UUID customerUUID = UUID.randomUUID();
		UUID demandUUID = UUID.randomUUID();
		
		DemandSaveDTO demandSaveDTO = DemandSaveDTO.builder()
				.uuid(demandUUID)
				.cakeUUID(cakeUUID)
				.customerUUID(customerUUID)
				.file(bytes)
				.extension("jpg")
				.price(1000)
				.build();
		
		String json = Mapper.objectMapper.writeValueAsString(demandSaveDTO);
		System.out.println(json);
		
		mvc.perform(post("/demand/save")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
	
	
}