package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;

import java.util.UUID;


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
                .pw("testPW")
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

    public void findByIdTest() {
        StoreDTO storeDto = StoreDTO.builder()
                .email("sdf")
                .pw("testPW")
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
        storeService.findById(storeDto);
    }

    @Test

    public void partialSaveTest() {

        StoreDTO storeDto = StoreDTO.builder()
//                .uuid(UUID.randomUUID())
                .email("sdf")
                .name("아름다운가게")
                .location("dkv")
                .store_phone_num("000")
                .owner_phone_num("123")
                .build();
        StoreDTO newDTO = storeService.save(storeDto);
        System.out.println("테스트"+newDTO.getUuid());

        newDTO.setEmail("changed!!");
        storeService.save(newDTO);


    }

}