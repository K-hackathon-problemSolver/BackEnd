package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.repository.TestRepository;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockBean(JpaMetamodelMappingContext.class)//jpaAuditing때문에 해줘야함.
@WebMvcTest
class StoreControllerTest {

    @Autowired
    MockMvc mvc;

    //가짜객체 주입해줘야한다. 컨트롤러에서 사용되는
    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    StoreService storeService;

    @MockBean
    CustomerService customerService;

    @MockBean
    CakeService cakeService;
    
    @MockBean
    DemandService demandService;
    
    
    @MockBean
    TestRepository testRepository;
    
    
    @Test
    public void saveTest() throws Exception {
        LocalTime open = LocalTime.parse("09:00");
        LocalTime close = LocalTime.parse("21:00");
    
        StoreDTO storeDTO = StoreDTO.builder()
                .email("zhdf@")
                .name("na")
                .location("loc")
                .store_phone_num("324")
                .openTime(open)
                .closeTime(close)
                .build();
    
        String json = Mapper.objectMapper.writeValueAsString(storeDTO);
        System.out.println(json);
        mvc.perform(post("/store/save")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email" ).value("zhdf@"))
                .andExpect(jsonPath("$.name" ).value("na"))
                .andExpect(jsonPath("$.location" ).value("loc"))
                .andExpect(jsonPath("$.store_phone_num").value("324"))
                .andDo(print());
//                .andExpect(jsonPath("$.uuid").exists());//webmvcTest라서 동작안함.

    }



    
    
   
    
    
}
