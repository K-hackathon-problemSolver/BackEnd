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

@MockBean(JpaMetamodelMappingContext.class)//jpaAuditing????????? ????????????.
@WebMvcTest
class StoreControllerTest {

    @Autowired
    MockMvc mvc;

    //???????????? ?????????????????????. ?????????????????? ????????????
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
//                .andExpect(jsonPath("$.uuid").exists());//webmvcTest?????? ????????????.

    }

    @Test
    public void editStoreMenuTest() throws Exception {
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

        CakeEditDTO cake2 = new CakeEditDTO();
        cake2.setName("cake2");
        cake2.setMinPrice(300);
        cake2.setOption("{\"plate\":\"2\"}");
        cake2.setImg(mainImg);
        cake2.setExtension("jpg");

        cakeList.add(cake1);
        cakeList.add(cake2);

        UUID tmpUUID = UUID.randomUUID();
        StoreEditDTO storeEditDTO = StoreEditDTO.builder()
                .uuid(tmpUUID)
                .name("??????1")
                .description("?????????!")
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

        String storeDir = "upload" + File.separator + tmpUUID + File.separator;
        File file = new File(storeDir + "mainImg.jpg");
        assertThat(file.exists()).isEqualTo(true);
        File f1 = new File(storeDir + cake1.getName() + "." + cake1.getExtension());
        File f2 = new File(storeDir + cake2.getName() + "." + cake2.getExtension());
        assertEquals(f1.exists(), true);
        assertEquals(f2.exists(), true);

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