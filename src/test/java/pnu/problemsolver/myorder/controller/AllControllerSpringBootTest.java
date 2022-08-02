package pnu.problemsolver.myorder.controller;

import org.jboss.jandex.Main;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.domain.constant.PusanLocation;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AllControllerSpringBootTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    StoreService storeService;

    @Autowired
    CakeService cakeService;
    
    @Autowired
    MainController mainController;
    
    @Autowired
    DemandService demandService;
    
    @Value("${myorder.upload.store}")
    public String uploadStorePath;
    
    @Autowired
    CustomerService customerService;
    
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    

    @Test
    public void listTest() throws Exception {
        List<DemandDTO> demandDTOList = mainController.insertAll();
    
        StoreListRequestDTO requestDTO = StoreListRequestDTO.builder()
                .location(PusanLocation.DONGLAE)
                .limit(3)//limit을 3으로 해놔서 2까지 온다.
                .offset(0)
                .build();
        String json = Mapper.objectMapper.writeValueAsString(requestDTO);
        System.out.println(json);
        mvc.perform(post("/store/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uuid").exists())
                .andExpect(jsonPath("$[0].name").value("store1"))
                .andExpect(jsonPath("$[0].mainImg").exists());
                
        
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
        mainImg = Base64.getEncoder().encode(mainImg);
        
        List<CakeEditDTO> cakeList = new ArrayList<>();
        CakeEditDTO cake1 = new CakeEditDTO();
        cake1.setName("cake1");
        cake1.setMinPrice(200);
        cake1.setOption("{\"plate\":\"1\"}");
        cake1.setImg(mainImg);
        cake1.setExtension("jpg");
        cake1.setUuid(cakeDTO1.getUuid());
        
        CakeEditDTO cake2 = new CakeEditDTO();
        cake2.setName("cake2");
        cake2.setMinPrice(300);
        cake2.setOption("{\"plate\":\"2\"}");
        cake2.setImg(mainImg);
        cake2.setExtension("jpg");
        cake2.setUuid(cakeDTO2.getUuid());
        
        cakeList.add(cake1);
        cakeList.add(cake2);
        
        StoreEditDTO storeEditDTO = StoreEditDTO.builder()
                .uuid(storeDTO.getUuid())
//                .name("가게1")
                .description("맛있다!")
                .cakeList(cakeList)
                .mainImg(mainImg)
                .extension("jpg")
//                .impossibleDate("[{start:2022-07-02, end:2022-07-06}, {start:2022-07-08, end:2022-07-10}]")
                .build();
        
        String json = Mapper.objectMapper.writeValueAsString(storeEditDTO);
        
        mvc.perform(post("/store/editMenu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print());
    
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
        List<StoreDTO> storeDTOList = mainController.insertStore();//dummy객체 넣을 때 사용했던 함수.
        mainController.insertCake(storeDTOList);
        StoreDTO storeDTO = storeDTOList.get(0);
        mvc.perform(get("/store").param("id", storeDTO.getUuid().toString()))
                .andExpect(jsonPath("$.mainImg").exists())
                .andExpect(jsonPath("$.cakeList[0].img").exists())
                .andDo(print());
        
    }
    
    @Test
    @Commit//commit하지 않으면 uuid를 받지 못한다.
    public void demandDetailedTest() {
        //given
        List<CustomerDTO> customerDTOS = mainController.insertCustomer();
//        for (CustomerDTO i : customerDTOS) {
//            System.out.println(i);
//        }
        List<StoreDTO> storeDTOList = mainController.insertStore();
//        for (StoreDTO i : storeDTOList) {
//            System.out.println(i);
//        }
        List<CakeDTO> cakeDTOS = mainController.insertCake(storeDTOList);
        List<DemandDTO> demandDTOList = mainController.insertDemand(cakeDTOS, customerDTOS, storeDTOList);
//        for (DemandDTO i : demandDTOList) {
//            System.out.println(i);
//        }
        //when
        DemandDetailResponseDTO res = demandService.findById(DemandDetailResponseDTO::toDTO, demandDTOList.get(0).getUuid());
        //then
        assertEquals(res.getImg() == null, false);
    }
    
    @Test
    public void customerDemandListTest() throws Exception {
        //given
        mainController.insertAll();
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
    
   
}
