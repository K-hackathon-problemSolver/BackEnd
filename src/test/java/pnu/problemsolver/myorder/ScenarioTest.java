package pnu.problemsolver.myorder;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.filter.JwtAuthenticationFilter;
import pnu.problemsolver.myorder.repository.CustomerRepository;
import pnu.problemsolver.myorder.repository.TestRepository;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@Transactional//테스트별로 독립적이기 위해서는 이게 필수임. TODO : 왜 이거 붙이면 주문목록 들고오는 테스트를 통과하지 못했는지 알아보자.
@Commit
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
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
	
	@Autowired
	StoreService storeService;
	
	@Autowired
	CakeService cakeService;
	@Autowired
	CustomerService customerService;
	@Autowired
	DemandService demandService;
	
	@BeforeAll
	public void setup() {
		testRepository.deleteAll();//시작하기 전에 다 삭제
		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.addFilters(new CharacterEncodingFilter("UTF-8", true))//한글 깨진다.!
				.addFilters(jwtAuthenticationFilter)//필터는 알아서 등록해야함..
				.build();
	}
	
	@Test
	public void midtermVideoScenarioTest() throws Exception {
		//init
//		testRepository.insertCustomer();
//		List<Store> storeList = testRepository.insertStore();
//		testRepository.insertCake(storeList);
		testRepository.insertAll();
		
		//소비자 로그인
		MvcResult mvcResult = mvc.perform(get("/get-test-customer"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jwt").exists())
				.andExpect(jsonPath("$.uuid").exists())
				.andReturn();
		
		String reponse = mvcResult.getResponse().getContentAsString();
		LoginResponseDTO testCustomer = Mapper.objectMapper.readValue(reponse, LoginResponseDTO.class);
		Optional<Customer> byId = customerRepository.findById(testCustomer.getUuid());
		assertEquals(true, byId.isPresent());
		assertEquals("진윤정", byId.get().getName());

//		가게 리스트 가져오기
//		StoreListRequestDTO listRequestDTO = StoreListRequestDTO.builder()
//				.location(PusanLocation.DONGLAE)
//				.offset(0)
//				.limit(6)
//				.build();
		MultiValueMap<String, String> paramas = new LinkedMultiValueMap<>();
		paramas.add("location", "DONGLAE");
		paramas.add("limit", "6");
		paramas.add("offset", "0");
		
		MvcResult mvcResult1 = mvc.perform(get("/store/list")
						.params(paramas))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("솔루션 메이커(동래점)"))//동래에서 가장 가까운 가게는 솔루션메이커임.
				.andExpect(jsonPath("$.size()").value(6))
				.andReturn();
		
		String StoreListResponseDTOJSON = mvcResult1.getResponse().getContentAsString();

//		System.out.println("테스트 : "+li);
//		System.out.println(li.get(0).getClass());//웃긴건 readValue()할 때가 아니라 .getClass()하면 예외남.
		
		//가게 하나 눌러서 케이크 자세히 보기
		List<StoreListResponseDTO> li = Mapper.objectMapper.readValue(StoreListResponseDTOJSON, new TypeReference<>() {
		});//abstract class라서 {}가 붙는다. 추상클래스를 익명클래스로 생성한 것임. 구현해야할 메소드가 하나도 없기 때문에 빈 {}이다.
		StoreListResponseDTO testStore = li.get(0);
		String strResult = mvc.perform(get("/store?id=" + testStore.getUuid()))
				.andExpect(jsonPath("$.uuid").exists())
				.andExpect(jsonPath("$.mainImg").exists())
				.andExpect(jsonPath("$.extension").exists())
				.andExpect(jsonPath("$.name").value("솔루션 메이커(동래점)"))
				.andExpect(jsonPath("$.cakeList").exists())
				.andExpect(jsonPath("$.cakeList[0]").exists())
				.andExpect(jsonPath("$.cakeList[1]").exists())//여기서 검사하면 문법도 빡시니까 밑에서 객체로 만든 다음에 assertEquals하는게 더 직관적일듯. 문법도 더 몰라도 되고!
				.andReturn().getResponse().getContentAsString();
		
		StoreEditDTO storeEditDTO = Mapper.objectMapper.readValue(strResult, StoreEditDTO.class);
		assertEquals(testStore.getUuid(), storeEditDTO.getUuid());

//		System.out.println(storeEditDTO.getCakeList().size());
		CakeEditDTO cakeEditDTO = storeEditDTO.getCakeList().get(0);
//		System.out.println(cakeEditDTO);

//		//주문하기
		File file = new File("src/main/resources/static/9.jpg");
		byte[] bytes = Files.readAllBytes(file.toPath());
		bytes = Base64.getEncoder().encode(bytes);
		
		String demandOption = "{\"시트 선택\" : \"기본맛(커스터드)\", \"사이즈\" : \"2호\", \"모양 선택\" : \"하트\", \"보냉 유무\" : \"유\", \"문구\" : \"솔루션 메이커 축하해~\"}";
		DemandSaveDTO demandSaveDTO = DemandSaveDTO.builder()
				.customerUUID(testCustomer.getUuid())
				.storeUUID(testStore.getUuid())
				.cakeUUID(cakeEditDTO.getUuid())
				.option(demandOption)
				.price(25000)
				.file(bytes)//파일은 없음. 만듦.
				.extension("jpg")
				.build();
		
		String s1 = Mapper.objectMapper.writeValueAsString(demandSaveDTO);
		Map map = Mapper.objectMapper.readValue(demandOption, Map.class); //예외 발생 안하면 json을 잘 짠것이다.
//		System.out.println(map);
		
		String contentAsString = mvc.perform(post("/demand/save").contentType(MediaType.APPLICATION_JSON)
						.content(s1))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		System.out.println(contentAsString);
		System.out.println(contentAsString.length());
		
		UUID orderUUID = UUID.fromString(contentAsString.substring(1, contentAsString.length() - 1));//이게 되면 잘 반환된 것임. substring은 인덱스로 계산한다.
		
		System.out.println(orderUUID);
		
		
		//소비자 주문확인하기.
		DemandListRequestDTO demandListRequestDTO = DemandListRequestDTO.builder()
				.uuid(testCustomer.getUuid())
				.size(6)
				.page(0)//0부터 시작.
				.build();
		String s2 = Mapper.objectMapper.writeValueAsString(demandListRequestDTO);
		String res = mvc.perform(post("/demand/WAITING")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + testCustomer.getJwt())
						.content(s2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].uuid").exists())
				.andExpect(jsonPath("$[0].cakeName").exists())
				.andExpect(jsonPath("$[0].price").exists())//option은 없을 수도 있기 때문에 이렇게 진행함.
				.andExpect(jsonPath("$.size()").value(6))//option은 없을 수도 있기 때문에 이렇게 진행함.
				.andReturn().getResponse().getContentAsString();
		
		List<DemandListResponseDTO> list = Mapper.objectMapper.readValue(res, new TypeReference<List<DemandListResponseDTO>>() {
		});
		DemandListResponseDTO demandListResponseDTO = list.get(0);
		assertEquals(demandOption, demandListResponseDTO.getOption());
		assertEquals(cakeEditDTO.getName(), demandListResponseDTO.getCakeName());
		assertEquals(orderUUID, demandListResponseDTO.getUuid());
		assertEquals(25000, demandListResponseDTO.getPrice());
		
		
		//가게 로그인
		String storeStr = mvc.perform(get("/get-test-store"))
				.andExpect(jsonPath("$.uuid").exists())
				.andExpect(jsonPath("$.jwt").exists())
				.andReturn().getResponse().getContentAsString();
		LoginResponseDTO storeUser = Mapper.objectMapper.readValue(storeStr, LoginResponseDTO.class);
		UUID storeUUID = storeUser.getUuid();
		StoreDTO storeDTO = storeService.findById(storeUUID);
		assertEquals(storeDTO.getUuid(), testStore.getUuid());//처음 나온 가게와 get-test-store의 가게가 같은지 검사
		assertEquals(storeDTO.getName(), testStore.getName());
		
		
		//가게 주문 확인하기
		DemandListRequestDTO demandListRequestDTO1 = DemandListRequestDTO.builder()
				.uuid(testStore.getUuid())
				.size(6)
				.page(0)//0부터 시작.
				.build();
		String s3 = Mapper.objectMapper.writeValueAsString(demandListRequestDTO1);
		String res1 = mvc.perform(post("/demand/WAITING")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + storeUser.getJwt())//store로 로그인.
						.content(s3))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].uuid").exists())
				.andExpect(jsonPath("$[0].cakeName").exists())
				.andExpect(jsonPath("$[0].price").exists())
				.andExpect(jsonPath("$.size()").value(6))//option은 없을 수도 있기 때문에 이렇게 진행함.
				.andReturn().getResponse().getContentAsString();
		
		List<DemandListResponseDTO> li2 = Mapper.objectMapper.readValue(res1, new TypeReference<List<DemandListResponseDTO>>() {
		});
		
		DemandListResponseDTO demandListResponseDTO1 = li2.get(0);
		Cake cake = cakeService.findById(i -> i, demandSaveDTO.getCakeUUID());
		
		
		assertEquals(25000, demandListResponseDTO1.getPrice());
		assertEquals(orderUUID, demandListResponseDTO1.getUuid());
		assertEquals(cake.getName(), demandListResponseDTO1.getCakeName());
		assertEquals(demandOption,demandListResponseDTO1.getOption());
		
		//주문수락
		ChangeStatusRequestDTO changeDemand = ChangeStatusRequestDTO.builder()
				.demandId(orderUUID)
				.changeStatusTo(DemandStatus.ACCEPTED)
				.build();
		
		String s4 = Mapper.objectMapper.writeValueAsString(changeDemand);
		String resStr = mvc.perform(post("/demand/change-status")
						.contentType(MediaType.APPLICATION_JSON)
						.content(s4))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		DemandDTO demandDTO = Mapper.objectMapper.readValue(resStr, DemandDTO.class);
		DemandDTO byId1 = demandService.findById(i -> DemandDTO.toDTO(i), demandDTO.getUuid());
		assertEquals(true, demandDTO.equals(byId1));
		assertEquals(true, demandDTO.getStatus() == DemandStatus.ACCEPTED);
		
		
	}
	
	@Test//필터가 필요하면 여기서 진행해야함.
		public void fcmTokenTest() throws Exception {
			testRepository.insertAll();
			
			String cusJson = mvc.perform(get("/get-test-customer"))
					.andExpect(status().isOk())
					.andReturn().getResponse().getContentAsString();
			
			LoginResponseDTO loginResponseDTO = Mapper.objectMapper.readValue(cusJson, LoginResponseDTO.class);
			UUID uuid = loginResponseDTO.getUuid();
			
			FcmTokenRequest tokenRequest = FcmTokenRequest.builder()
					.token("this is token")
					.uuid(uuid)
					.build();
		
		mvc.perform(post("/fcm-token")
						.contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", "Bearer " + loginResponseDTO.getJwt())//memberType을 확인하기 때문에 JWT가 필요하다.
						.content(Mapper.objectMapper.writeValueAsString(tokenRequest)))
				.andExpect(status().isOk())
				.andExpect(content().string("success"));
			
			Customer byId = customerService.findById(i -> i, uuid);
			assertEquals("this is token", byId.getFcmToken());
		}
	
}

