package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.CakeEditDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreEditDTO;
import pnu.problemsolver.myorder.repository.CakeRepositroy;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.Mapper;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Commit
//@Transactional
public class StoreControllerSpringBootTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    StoreService storeService;

    @Autowired
    CakeService cakeService;

//    @Test
//    public void listTest() throws Exception {
//        mvc.perform(get("/"));
//        mvc.perform(get("/store/list"))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }


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
//
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

        String storeDir = "upload" + File.separator + storeDTO.getUuid() + File.separator;

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
}
