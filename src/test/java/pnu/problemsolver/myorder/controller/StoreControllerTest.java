package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.CakeEditDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreDTOForList;
import pnu.problemsolver.myorder.dto.StoreEditDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

import static java.util.Collections.copy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.shouldHaveThrown;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    @Test
    public void saveTest() throws Exception {
        StoreDTO storeDTO = StoreDTO.builder()
                .email("zhdf@")
                .name("na")
                .location("loc")
                .store_phone_num("324")
                .build();


        given(storeService.save(storeDTO)).willReturn(storeDTO);
        String json = Mapper.objectMapper.writeValueAsString(storeDTO);
        mvc.perform(post("/store/save")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", "zhdf@").exists())
                .andExpect(jsonPath("$.name", "na").exists())
                .andExpect(jsonPath("$.location", "loc").exists())
                .andExpect(jsonPath("$.store_phone_num", "324").exists())
                .andDo(print());
//                .andExpect(jsonPath("$.uuid").exists());//webmvcTest라서 동작안함.

    }



    @Test
    public void editImpossibleDateTest() throws JsonProcessingException {

        Map<String, Object> map = new HashMap<>();
        map.put("uuid", UUID.randomUUID().toString());
        map.put("impossibleDate", "[{\"start\":\"2022-07-20\", \"end\" : \"2022-07-24\"}]");
        String json = "{\"uuid\":\"" + UUID.randomUUID().toString() + "\", \"impossibleDate\" : [{\"start\":\"2022-02-03\", \"end\" : \"2022-02-03\"}]}";
//        List<Tmp> li = new ArrayList<>();
//        li.add(new Tmp("2022-07-20", "2022-07-21"));
//        li.add(new Tmp("2022-07-20", "2022-07-21"));
//        map.put("impossibleDate", li);
//        String json = Mapper.objectMapper.writeValueAsString(map);
//        System.out.println(json);

        ArrayList res = Mapper.objectMapper.readValue("[{\"start\":\"2022-07-20\", \"end\" : \"2022-07-24\"}]", ArrayList.class);
        System.out.println(res);
        Map map1 = Mapper.objectMapper.readValue(json, Map.class);
        System.out.println(map1);
        System.out.println(map1.getClass());
    }



}

@AllArgsConstructor
class Tmp {
    public String start;
    public String end;
}