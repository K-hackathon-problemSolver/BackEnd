package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Store;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
//@Transactional
class CakeRepositroyTest {
	@Autowired
	CakeRepositroy cakeRepositroy;
	@Autowired
	StoreRepository storeRepository;
	
	@Test
	public void saveTest() {
		Store s = Store.builder()
				.name("스토어")
				.build();
		storeRepository.save(s);
		UUID uuid = s.getUuid();
		
		s.setFcmToken("1");//영속상태 아님. 변경감지 동작안함.
		
		
		Store store = Store.builder()
				.uuid(uuid)
				.description("테스트")
				.build();
		
		Cake c = Cake.builder()
				.name("케이크")
				.store(store)
				.build();
		
		cakeRepositroy.save(c);
		
		System.out.println(c.getStore());
		
	}
	
	@Test
		public void findByStoreTest() {
			Store s = Store.builder()
					.name("가게")
					.build();
			storeRepository.save(s);
			Store s1 = Store.builder()
					.name("가게")
					.build();
			storeRepository.save(s1);
			for (int i = 0; i < 5; ++i) {
				Cake c = Cake.builder()
						.store(s)
						.minPrice(100)
						.name("케잌")
						.build();
				cakeRepositroy.save(c);
			}
			for (int i = 0; i < 5; ++i) {
				
				Cake c = Cake.builder()
						.store(s1)
						.minPrice(100)
						.name("케잌")
						.build();
				cakeRepositroy.save(c);
			}
			List<Cake> byStore = cakeRepositroy.findByStore(s);
			assertEquals(byStore.size(), 5);
		}
	
}