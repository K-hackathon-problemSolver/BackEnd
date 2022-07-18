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

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StoreRepositoryTest {
    @Autowired//final못 붙인다.
    public StoreRepository storeRepository;

    @BeforeEach//static이어야한다.
    public void testinsertDummies(){
        IntStream.rangeClosed(1, 20).forEach(i->{
            Store store = Store.builder()
                    .email("id" + i)
                    .pw("pw" + i)
                    .name("신민건")
                    .description("부산대" + i)
                    .location("부산대긱사")
                    .store_phone_num("051341342")
                    .owner_phone_num("01033916486")
                    .build();
            storeRepository.save(store);
        });
    }

    @Test
    public void DI확인() {
        System.out.println(storeRepository.getClass().getName());
    }


    @Test
    public void NotNUll테스트(){//entity에는 null이 들어갈 수 있다. @NotNull을 사용해도 마찬가지였음.
            Store store = Store.builder()
                    .email("id")
                    .pw("pw")
                    .name(null)
                    .location(null)
                    .description("부산대")
                    .store_phone_num("051341342")
                    .owner_phone_num("01033916486")
                    .build();
    }

    @Test
    public void updateTest() {
        Store store = Store.builder()
                .email("id5")
                .pw("update")
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
    public void 쿼리메소드테스트() {
        List<Store> list = storeRepository.findByEmailBetweenOrderByEmailDesc("id1", "id99");
        list.stream().forEach(System.out::println);
    }

    @Test
    //아래 2개 빠지면 동작안한다. select, delete가 따로 이뤄지기 때문이라고 함.
    @Transactional
    @Commit
    public void 쿼리메소드delete() {
        storeRepository.deleteStoreByEmailLessThan("id5");
    }

}