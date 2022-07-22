package pnu.problemsolver.myorder.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Commit
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

        storeDTO=storeService.save(storeDTO);

        CakeDTO cakeDTO = CakeDTO.builder()
                .uuid(UUID.randomUUID())
                .storeUUID(storeDTO.getUuid())//없으면 자꾸 안되더라. TODO 영속성 ~ 때문에 안되는 거였음. JPA책 사서 보자.
//                .demandUUID("UUID.randomUUID()")
                .filePath("path!")
                .option("{\"plate\":\"1\"}")
                .name("신민건")
                .min_price(123)
                .description("설명")
                .build();

        log.info(cakeDTO.toString());

        cakeDTO=cakeService.save(cakeDTO);
        log.info(cakeDTO.toString());


    }

}