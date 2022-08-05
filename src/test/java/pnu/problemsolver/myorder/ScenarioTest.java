package pnu.problemsolver.myorder;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.PusanLocation;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.filter.JwtAuthenticationFilter;
import pnu.problemsolver.myorder.repository.CustomerRepository;
import pnu.problemsolver.myorder.repository.TestRepository;
import pnu.problemsolver.myorder.util.Mapper;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional//테스트별로 독립적이기 위해서는 이게 필수임. TODO : 왜 이거 붙이면 주문목록 들고오는 테스트를 통과하지 못했는지 알아보자.
@Commit
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScenarioTest {//여기서 시나리오 테스트 하면 되겠다.
	@Autowired
	
	TestRepository testRepository;
	@Autowired
	
	CustomerRepository customerRepository;
	@Autowired
	
	WebApplicationContext webApplicationContext;
	
	@Autowired
	//MockMvc에서 한글을 사용하면 꺠진다. 실제로 동작할 때는 잘 동작하는데 테스트 할 때만 깨짐 아래의 코드로 해결가능
	MockMvc mvc; //= MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(new CharacterEncodingFilter("UTF-8", true)).build()
	
	@Autowired
	JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@BeforeAll
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
						.addFilters(new CharacterEncodingFilter("UTF-8", true))
						.addFilters(jwtAuthenticationFilter)
						.build();
	}
	
	@Test
	public void midtermVideoScenarioTest() throws Exception {
		//init
		testRepository.insertCustomer();
		List<Store> storeList = testRepository.insertStore();
		testRepository.insertCake(storeList);
//		testRepository.insertAll();
		//사용자 가져오기
		MvcResult mvcResult = mvc.perform(get("/get-test-customer"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt").exists())
				.andExpect(jsonPath("$.uuid").exists())
				.andReturn();
		
		
		String reponse = mvcResult.getResponse().getContentAsString();
		LoginResponseDTO cusLoginResponseDTO = Mapper.objectMapper.readValue(reponse, LoginResponseDTO.class);
		Optional<Customer> byId = customerRepository.findById(cusLoginResponseDTO.getUuid());
		assertEquals(byId.isPresent(), true);
		assertEquals(byId.get().getName(), "진윤정");
		
//		가게 리스트 가져오기
		StoreListRequestDTO listRequestDTO = StoreListRequestDTO.builder()
				.location(PusanLocation.DONGLAE)
				.offset(0)
				.limit(6)
				.build();
		String s = Mapper.objectMapper.writeValueAsString(listRequestDTO);
		MvcResult mvcResult1 = mvc.perform(post("/store/list")
						.contentType(MediaType.APPLICATION_JSON)
						.content(s))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("솔루션 메이커(동래점)"))//동래에서 가장 가까운 가게는 솔루션메이커임.
				.andReturn();
		String StoreListResponseDTOJSON = mvcResult1.getResponse().getContentAsString();
		
//		System.out.println("테스트 : "+li);
//		System.out.println(li.get(0).getClass());//웃긴건 readValue()할 때가 아니라 .getClass()하면 예외남.
		
		//가게 하나 눌러서 케이크 자세히 보기
		List<StoreListResponseDTO> li = Mapper.objectMapper.readValue(StoreListResponseDTOJSON, new TypeReference<>(){});//abstract class라서 {}가 붙는다. 추상클래스를 익명클래스로 생성한 것임. 구현해야할 메소드가 하나도 없기 때문에 빈 {}이다.
		StoreListResponseDTO storeListReponseDTO = li.get(0);
		String strResult = mvc.perform(get("/store?id=" + storeListReponseDTO.getUuid()))
				.andExpect(jsonPath("$.uuid").exists())
				.andExpect(jsonPath("$.mainImg").exists())
				.andExpect(jsonPath("$.extension").exists())
				.andExpect(jsonPath("$.cakeList").exists())
				.andExpect(jsonPath("$.cakeList[0]").exists())
				.andExpect(jsonPath("$.cakeList[1]").exists())
				.andReturn().getResponse().getContentAsString();
		
		StoreEditDTO storeEditDTO = Mapper.objectMapper.readValue(strResult, StoreEditDTO.class);
//		System.out.println(storeEditDTO.getCakeList().size());
		CakeEditDTO cakeEditDTO = storeEditDTO.getCakeList().get(0);
//		System.out.println(cakeEditDTO);
		
//		//주문하기
		String option = "{\"시트 선택\" : \"기본맛(커스터드)\", \"사이즈\" : \"2호\", \"모양 선택\" : \"하트\", \"보냉 유무\" : \"유\", \"문구\" : \"솔루션 메이커 축하해~\"}";
		DemandSaveDTO demandSaveDTO = DemandSaveDTO.builder()
				.customerUUID(cusLoginResponseDTO.getUuid())
				.storeUUID(storeListReponseDTO.getUuid())
				.cakeUUID(cakeEditDTO.getUuid())
				.option(option)
				.price(25000)
				.file(null)//파일은 없음.
				.extension(null)
				.build();
		
		String s1 = Mapper.objectMapper.writeValueAsString(demandSaveDTO);
		Map map = Mapper.objectMapper.readValue(option, Map.class); //예외 발생 안하면 json을 잘 짠것이다.
		System.out.println(map);
		
		mvc.perform(post("/demand/save").contentType(MediaType.APPLICATION_JSON)
						.content(s1))
				.andExpect(status().isOk())
				.andExpect(content().string("success"));
		
		//소비자 주문확인하기.
		DemandListRequestDTO demandListRequestDTO = DemandListRequestDTO.builder()
				.uuid(cusLoginResponseDTO.getUuid())
				.size(6)
				.page(0)//0부터 시작.
				.build();
		
		String s2 = Mapper.objectMapper.writeValueAsString(demandListRequestDTO);
		String res = mvc.perform(post("/demand/WAITING")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + cusLoginResponseDTO.getJwt())
						.content(s2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].uuid").exists())
				.andExpect(jsonPath("$[0].cakeName").exists())
				.andExpect(jsonPath("$[0].price").exists())//option은 없을 수도 있기 때문에 이렇게 진행함.
				.andReturn().getResponse().getContentAsString();
		System.out.println(res);;
	}
}

