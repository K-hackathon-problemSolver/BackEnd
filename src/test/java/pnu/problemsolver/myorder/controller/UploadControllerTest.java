package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import pnu.problemsolver.myorder.dto.CakeSaveDTO;
import pnu.problemsolver.myorder.dto.StoreSaveDTO;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@MockBean(JpaMetamodelMappingContext.class)//jpaAuditing때문에 해줘야함.
@WebMvcTest
class UploadControllerTest {

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

        StoreSaveDTO storeSaveDTO = StoreSaveDTO.builder()
                .name("가게1")
                .description("맛있다!")
                .cakeList(cakeList)
                .mainImg(mainImg)
                .build();

        String json = Mapper.objectMapper.writeValueAsString(storeSaveDTO);

        mvc.perform(post("/upload/store")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print());

    }


}