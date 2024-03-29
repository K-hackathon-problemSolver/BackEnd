package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.domain.constant.PusanLocation;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.repository.TestRepository;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;
import pnu.problemsolver.myorder.util.MyUtil;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional//테스트별로 독립적이기 위해서는 이게 필수임.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AllControllerSpringBootTest {
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	StoreService storeService;
	
	@Autowired
	CakeService cakeService;
	
	@Autowired
//    MainController mainController;
	TestRepository testRepository;
	
	@Autowired
	DemandService demandService;
	
	@Value("${myorder.upload.store}")
	public String uploadStorePath;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	EntityManager em;
	
	
	@Autowired
	JwtTokenProvider jwtTokenProvider;
	
	@BeforeAll
	public void beforeAll() {
		testRepository.deleteAll();
		
	}
	
	@Test
	public void storeListTest() throws Exception {
		List<Demand> demandList = testRepository.insertAll();
		StoreListRequestDTO requestDTO = StoreListRequestDTO.builder()
				.location(PusanLocation.DONGLAE)
				.limit(3)//limit을 3으로 해놔서 2까지 온다.
				.offset(0)
				.build();
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("location", "DONGLAE");
		map.add("offset", "0");
		map.add("limit", "6");


//		String json = Mapper.objectMapper.writeValueAsString(requestDTO);
		String contentAsString = mvc.perform(get("/store/list").params(map))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].uuid").exists())
				.andExpect(jsonPath("$[0].name").exists())
				.andExpect(jsonPath("$[0].mainImg").exists())
				.andReturn().getResponse().getContentAsString();
		
		System.out.println(contentAsString);
		
		//GET에 파라미터 없어도 잘 되는지
		mvc.perform(get("/store/list"))
				.andExpect(status().isOk());
		
	}
	
	@Test
	public void editStoreMenuTest() throws Exception {
		
		StoreDTO storeDTO = StoreDTO.builder()
				.name("초기화")
				.description("초기화")
				.build();
		storeService.save(storeDTO);
		
		CakeDTO cakeDTO1 = CakeDTO.builder()
				.name("케이크1")
				.minPrice(1000)
				.storeUUID(storeDTO.getUuid())
				.build();
		System.out.println(cakeDTO1);
		
		cakeService.save(cakeDTO1);
		
		CakeDTO cakeDTO2 = CakeDTO.builder()
				.name("케이크2")
				.minPrice(2000)
				.storeUUID(storeDTO.getUuid())
				.build();
		cakeService.save(cakeDTO2);
		//파일 읽기
		File mainImgFile = new File("src/main/resources/static/testPicture.jpg");
		byte[] mainImg = Files.readAllBytes(mainImgFile.toPath());
		String mainImgStr = Base64.getEncoder().encodeToString(mainImg);
		
		System.out.println("테스트1 : " + mainImg);//TODO : 출력이 잘 안된다... 널문자?..
		List<CakeEditDTO> cakeList = new ArrayList<>();
		CakeEditDTO cake1 = new CakeEditDTO();
		cake1.setName("cake1");
		cake1.setMinPrice(200);
		cake1.setOption("{\"plate\":\"1\"}");
		cake1.setImg(mainImgStr);
		cake1.setExtension("jpg");
		cake1.setUuid(cakeDTO1.getUuid());
		
		CakeEditDTO cake2 = new CakeEditDTO();
		cake2.setName("cake2");
		cake2.setMinPrice(300);
		cake2.setOption("{\"plate\":\"2\"}");
		cake2.setImg(mainImgStr);
		cake2.setExtension("jpg");
		cake2.setUuid(cakeDTO2.getUuid());
		
		cakeList.add(cake1);
		cakeList.add(cake2);
		
		StoreEditDTO storeEditDTO = StoreEditDTO.builder()
				.uuid(storeDTO.getUuid())
//                .name("가게1")
				.description("맛있다!")
				.cakeList(cakeList)
				.mainImg(mainImgStr)
				.extension("jpg")
//                .impossibleDate("[{start:2022-07-02, end:2022-07-06}, {start:2022-07-08, end:2022-07-10}]")
				.build();
		
		String json = Mapper.objectMapper.writeValueAsString(storeEditDTO);
		
		mvc.perform(post("/store/editMenu")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andDo(print());
		System.out.println("테스트"+json);
		
		String storeDir = uploadStorePath + File.separator + storeDTO.getUuid() + File.separator;
		
		File file = new File(storeDir + "mainImg.jpg");
		assertThat(file.exists()).isEqualTo(true);
		File f1 = new File(storeDir + cake1.getName() + "." + cake1.getExtension());
		File f2 = new File(storeDir + cake2.getName() + "." + cake2.getExtension());
		assertEquals(f1.exists(), true);
		assertEquals(f2.exists(), true);
		
		//다시 가져왔을 때 null이 안되어 있고 null이 아닌 것만 바뀜.!
		StoreDTO byId = storeService.findById(storeDTO.getUuid());
		assertEquals(byId.getName().equals("초기화"), true);
		assertEquals(byId.getDescription().equals("맛있다!"), true);
	}
	
	@Test
	public void oneStoreTest() throws Exception {
		List<Store> storeList = testRepository.insertStore();//dummy객체 넣을 때 사용했던 함수.
		testRepository.insertCake(storeList);
		StoreDTO storeDTO = StoreDTO.toDTO(storeList.get(0));
		
		mvc.perform(get("/store").param("id", storeDTO.getUuid().toString()))
				.andExpect(jsonPath("$.mainImg").exists())
				.andExpect(jsonPath("$.cakeList[0].img").exists())
				.andExpect(jsonPath("$.cakeList[0].uuid").exists())
				.andExpect(jsonPath("$.extension").exists())
				.andExpect(jsonPath("$.name").exists())
				.andExpect(jsonPath("$.description").exists())
				.andDo(print());
	}
	
	@Test
//    @Commit//commit하지 않으면 uuid를 받지 못한다.
	public void demandDetailedTest() {
		//given
		List<Demand> demandList = testRepository.insertAll();
		UUID uuid = demandList.get(0).getUuid();
		
		//when
		Demand demand = demandService.findById(i -> i, uuid);
		DemandDetailResponseDTO res = DemandDetailResponseDTO.toDTO(demand);
		//then
		assertEquals(res.getImg() == null, false);
	}
	
	@Test
	public void customerDemandListTest() throws Exception {
		//given
		testRepository.insertAll();
		List<CustomerDTO> all = customerService.findAll(CustomerDTO::toDTO);
		CustomerDTO customerDTO = all.get(0);
		
		DemandListRequestDTO requestDTO = DemandListRequestDTO.builder()
				.uuid(customerDTO.getUuid())
				.page(0)
				.size(5)
				.build();
		
		String json = Mapper.objectMapper.writeValueAsString(requestDTO);
		
		String jwt = jwtTokenProvider.createToken(MemberType.CUSTOMER);
		System.out.println(jwt);
		jwt = "Bearer " + jwt;
		
		//when, then
		mvc.perform(post("/demand/WAITING").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", jwt) //헤더 Authorization : Bearer {jwt}
						.content(json))
				.andExpect(status().isOk())
				.andDo(print());
		
		mvc.perform(post("/demand/COMPLETED").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", jwt)
						.content(json))
				.andExpect(status().isOk())
				.andDo(print());
		
		mvc.perform(post("/demand/ACCEPTED").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", jwt)
						.content(json))
				.andExpect(status().isOk())
				.andDo(print());
		
		mvc.perform(post("/demand/REJECTED").contentType(MediaType.APPLICATION_JSON)
						.header("Authorization", jwt)
						.content(json))
				.andExpect(status().isOk())
				.andDo(print());
		
	}
	
	
	@Test
	public void demandSaveTest() throws Exception {
		List<Customer> customers = testRepository.insertCustomer();
		List<Store> stores = testRepository.insertStore();
		List<Cake> cakes = testRepository.insertCake(stores);
		
		List<Demand> demands = testRepository.insertDemand(cakes, customers, stores);
		
		File file = new File("src/main/resources/static/testPicture.jpg");
		assertEquals(file.exists(), true);
		String bytes;
		try {
			bytes = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
			
		} catch (IOException e) {
			throw new RuntimeException("Files.readAllBytes Exception!!");
		}
		String testStoreStr = mvc.perform(get("/get-test-store"))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		LoginResponseDTO store = Mapper.objectMapper.readValue(testStoreStr, LoginResponseDTO.class);
		
		String testCustomerStr = mvc.perform(get("/get-test-customer"))
						.andExpect(status().isOk())
						.andDo(print())
						.andReturn().getResponse().getContentAsString();
		LoginResponseDTO customer = Mapper.objectMapper.readValue(testCustomerStr, LoginResponseDTO.class);
		
		UUID cakeUUID = cakes.get(0).getUuid();
		UUID customerUUID = customer.getUuid();
		UUID storeUUID = store.getUuid();
		
		assertEquals(cakeUUID != null, true);
		assertEquals(customerUUID != null, true);
		assertEquals(storeUUID != null, true);
		DemandSaveDTO demandSaveDTO = DemandSaveDTO.builder()
				.cakeUUID(cakeUUID)
				.customerUUID(customerUUID)
				.storeUUID(storeUUID)
				.file(bytes)
				.extension("jpg")
				.price(1000)
				.build();
		
		String json = Mapper.objectMapper.writeValueAsString(demandSaveDTO);
		System.out.println(json);
		String contentAsString = mvc.perform(post("/demand/save")
						.content(json)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		
		UUID uuid = UUID.fromString(contentAsString.substring(1, contentAsString.length() - 1));
		
		File f = new File("upload/demand/" + demandSaveDTO.getCustomerUUID().toString() + File.separator + uuid + ".jpg");
		byte[] bytes1 = Files.readAllBytes(f.toPath());
		byte[] decode = MyUtil.decoder.decode(bytes1);
		File f2 = new File("upload/test");
		if (!f2.exists()) {
			f2.mkdirs();
		}
		Files.write(Path.of("upload/test/1.jpg"), decode);//디코딩안하고 저장한 것을 디코딩했을 떄 잘되는지 확인.
		
	}
	
	@Test
	public void editImpossibleDateTest() throws Exception {
		testRepository.insertAll();
		List<Store> testStore = testRepository.getTestStore();
		assertEquals(1, testStore.size());
		Store store = testStore.get(0);
		//        Map<String, Object> map = new HashMap<>();
		//        map.put("uuid", store.getUuid());
		//
		//        map.put("impossibleDate", "[{\"start\":\"2022-07-20\", \"end\" : \"2022-07-24\"}]");
		String json = "{\"uuid\":\"" + store.getUuid() + "\", \"impossibleDate\" : [{\"start\":\"2022-02-03\", \"end\" : \"2022-02-03\"}]}";
		
		//        List<Tmp> li = new ArrayList<>();
		//        li.add(new Tmp("2022-07-20", "2022-07-21"));
		//        li.add(new Tmp("2022-07-20", "2022-07-21"));
		//        map.put("impossibleDate", li);
		//        String json = Mapper.objectMapper.writeValueAsString(map);
		//        System.out.println(json);
		
		//이렇게도 써지는지 테스트
		ArrayList res = Mapper.objectMapper.readValue("[{\"start\":\"2022-07-20\", \"end\" : \"2022-07-24\"}]", ArrayList.class);
		System.out.println(res);
		
		//json을 잘 썻는지 테스트
		Map map1 = Mapper.objectMapper.readValue(json, Map.class);
		System.out.println(map1);
		
		String contentAsString = mvc.perform(post("/store/editImpossibleDate")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn().getResponse().getContentAsString();
		assertEquals(contentAsString, "success");
		
		StoreDTO byId = storeService.findById(store.getUuid());
		System.out.println(byId);
		
		String impossibleDate = Mapper.objectMapper.writeValueAsString(map1.get("impossibleDate"));
		assertEquals(byId.getImpossibleDate(), impossibleDate);
		
	}
	
	@Test
	public void changeStatus() throws Exception {
		List<Demand> demandList = testRepository.insertAll();
		Demand demand = demandList.get(0);
		ChangeStatusRequestDTO dto = ChangeStatusRequestDTO.builder()
				.demandId(demand.getUuid())
				.changeStatusTo(DemandStatus.ACCEPTED)
				.build();
		String s = Mapper.objectMapper.writeValueAsString(dto);
		
		mvc.perform(post("/demand/change-status")
						.contentType(MediaType.APPLICATION_JSON)
						.content(s))
				.andExpect(status().isOk())
				.andDo(print());
				
		Demand byId = demandService.findById(i -> i, demand.getUuid());
		assertEquals(byId.getStatus(), DemandStatus.ACCEPTED);;
	}
	
}
