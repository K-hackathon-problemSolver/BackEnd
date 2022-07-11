package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class StoreServiceTest {


    @Autowired
    StoreService storeService;
    @Autowired
    ModelMapper mapper;

    @Test
    @Commit
//    @Transactional
    public void save테스트() {
        StoreDTO storeDto = StoreDTO.builder()
                .email("zhdhfhd33")
                .pw("testPW")
                .name("아름다운가게")
                .description("이건설명")
                .location("부산대앞")
                .store_phone_num("311")
                .owner_phone_num("123")
                .build();

        System.out.println(storeDto);
        System.out.println();

        Store store = Store.toEntity(storeDto);
        System.out.println(store);

        storeService.saveStore(storeDto);

    }

}