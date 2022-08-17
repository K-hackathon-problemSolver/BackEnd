package pnu.problemsolver.myorder.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;

@SpringBootTest
//@Commit
//@Transactional
@Slf4j
class CakeServiceTest {

    @Autowired
    CakeService cakeService;

    @Autowired
    StoreService storeService;

    @Test
    public void saveTest() {

        StoreDTO storeDTO = StoreDTO.builder()
                .name("store!!")
                .build();
        storeService.save(storeDTO);
        CakeDTO cakeDTO = CakeDTO.builder()
//                .uuid(UUID.randomUUID())
                .storeUUID(storeDTO.getUuid())//없으면 자꾸 안되더라. store를 위에서 저장했기 때문에 영컨에 store가 있는 상황임. 근데 Transactional이 끝나니까 같은 컨텍스트는 아닐 텐데..
//                .demandUUID("UUID.randomUUID()")
                .filePath("path!")
                .option("{\"plate\":\"1\"}")
                .name("아아")
                .minPrice(123)
                .description("설명")
                .build();
    
        cakeService.save(cakeDTO);
        log.info(cakeDTO.toString());
        log.info(cakeDTO.toString());

    }

}