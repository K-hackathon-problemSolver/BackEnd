package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Commit
@Transactional
class StoreServiceTest {


    @Autowired
    StoreService storeService;

    @Test
    public void saveTest() {
        StoreDTO storeDto = StoreDTO.builder()
//                .uuid(UUID.randomUUID())
                .email("sdf")
                .name("아름다운가게")
                .description("이건설명")
                .location("dkv")
                .store_phone_num("311")
                .owner_phone_num("123")
                .impossibleDate("{\"a\":\"1\"}")
                .build();

        System.out.println(storeDto);
        System.out.println();

        Store store = Store.toEntity(storeDto);
        System.out.println(store);
//
        storeService.save(storeDto);
    }


    @Test
    public void partialSaveTest() {
        StoreDTO storeDto = StoreDTO.builder()
//                .uuid(UUID.randomUUID())//자동할당 해준다.
                .email("sdf")
                .name("아름다운가게")
                .location("dkv")
                .store_phone_num("000")
                .owner_phone_num("123")
                .build();

        StoreDTO newDTO = storeService.save(storeDto);
        System.out.println("테스트"+newDTO.getUuid());

        newDTO.setEmail("changed!!");//null이 아닌 부분만 알아서 update된다.
        storeService.save(newDTO);
    }

//    @Test
//    public void getAllPreflightsTest() {
//        for (int i = 0; i < 10; ++i) {
//            storeService.save(new StoreDTO());
//        }
//        List<StoreDTOForListPreflight> li = storeService.getAllPreflights();
//        assertEquals(li.size(), 10);
//
//    }


}