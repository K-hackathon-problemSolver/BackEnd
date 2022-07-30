package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Commit;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.service.StoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Commit
class StoreRepositoryTest {
    @Autowired//final못 붙인다.
    public StoreRepository storeRepository;


    @BeforeEach//static이어야한다.
    public void testinsertDummies(){
        IntStream.rangeClosed(1, 20).forEach(i->{
            Store store = Store.builder()
                    .email("id" + i)
                    .name("신민건")
                    .description("부산대" + i)
                    .location("부산대긱사")
                    .store_phone_num("051341342")
                    .owner_phone_num("01033916486")
                    .latitude(i)
                    .longitude(i)
                    .build();
            storeRepository.save(store);
        });
    }

    @Test
    public void updateTest() {
        Store store = Store.builder()
                .email("id5")
                .name("update")
                .description("update")
                .location("update")
                .store_phone_num("update")
                .owner_phone_num("update")
                .build();
        assertEquals(storeRepository.save(store).getEmail(), "id5");//저장한 엔티티를 반환한다.
    }

    @Test
    public void findById테스트() {//key로 찾는다.
//        Optional<Store> store = storeRepository.findById("id5");
//        store.ifPresent(st -> {
//            assertEquals(st.getEmail(), "id5");
//        });
    }

    @Test
    public void delete테스트() {//key로 찾는다.
//        String id = "id5";
//        storeRepository.deleteById(id);
//
//        Optional<Store> res = storeRepository.findById(id);
//        assertEquals(res.isPresent(), false);

    }

    @Test
    public void 페이징테스트() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Store> result = storeRepository.findAll(pageable);
        System.out.println("페이징테스트 : "+result);

        result.getContent().stream().forEach(store ->{
            System.out.println(store);
        });
    }

    @Test
    public void findAllInListTest() {
        List<Store> list = storeRepository.findByName("신민건");
        List<UUID> uuidList = new ArrayList<>();
        for(Store i : list){
            uuidList.add(i.getUuid());
        }

        List<Store> resList = storeRepository.findAllInUUIDList(uuidList);
        System.out.println(resList);
        System.out.println(resList.size());

    }
    
    @Test
    public void findByLocation() {
        for (int i = 0; i < 20; ++i) {
            Store s = Store.builder()
                    .name("가게" + i)
                    .latitude(i)
                    .longitude(i)
                    .build();
            storeRepository.save(s);
        }
        
        List<Store> res = storeRepository.findByLocation(0, 0, 6, 0);
        for (Store i : res) {
            System.out.println(i);
        }
        assertEquals(res.size(), 6);
        assertEquals(res.get(0).getLatitude(), 0);
        assertEquals(res.get(0).getLongitude(), 0);
    }


}