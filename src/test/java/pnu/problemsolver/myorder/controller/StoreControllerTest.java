package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.dto.CakeSaveDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreUpdateDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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


    @Test
    public void saveTest() throws Exception {
        StoreDTO storeDTo = StoreDTO.builder()
                .email("zhdf@")
                .name("na")
                .location("loc")
                .store_phone_num("324")
                .build();

        String json = Mapper.objectMapper.writeValueAsString(storeDTo);
        mvc.perform(post("/store/save")
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
    @Test
    public void uploadTest() throws Exception {
        File mainImgFile= new File("src/main/resources/static/testPicture.jpg");
        byte[] mainImg = Files.readAllBytes(mainImgFile.toPath());
        mainImg=  Base64.getEncoder().encode(mainImg);

        List<CakeSaveDTO> cakeList = new ArrayList<>();
        CakeSaveDTO cake1 = new CakeSaveDTO();
        cake1.setName("cake1");
        cake1.setMinPrice(200);
        cake1.setOption("{options~~~}");

        CakeSaveDTO cake2 = new CakeSaveDTO();
        cake2.setName("cake2");
        cake2.setMinPrice(300);
        cake2.setOption("{options~~~}");


        cakeList.add(cake1);
        cakeList.add(cake2);

        StoreUpdateDTO storeUpdateDTO = StoreUpdateDTO.builder()
                .name("가게1")
                .description("맛있다!")
                .cakeList(cakeList)
                .mainImg(mainImg)
                .build();

        String json = Mapper.objectMapper.writeValueAsString(storeUpdateDTO);

        mvc.perform(post("/upload/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print());

    }


}