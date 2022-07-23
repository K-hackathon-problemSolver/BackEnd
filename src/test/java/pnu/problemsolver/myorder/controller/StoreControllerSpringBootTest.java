package pnu.problemsolver.myorder.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.service.StoreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoreControllerSpringBootTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    StoreService storeService;

    @Test
    public void listTest() throws Exception {
        for (int i = 0; i < 10; ++i) {
            storeService.save(new StoreDTO());
        }

        mvc.perform(get("/store/list"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //TODO : 생각보다 test코드 짜기가 힘들다...ㅠ
//    @Test
//    public void StoreDTOForList_doDTOtest() {
//        StoreDTO dto = storeService.save(new StoreDTO());
//
//    }

}
