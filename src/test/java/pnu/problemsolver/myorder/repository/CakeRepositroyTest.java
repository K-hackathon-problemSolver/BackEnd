package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Store;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CakeRepositroyTest {
	
	@Autowired
	CakeRepositroy cakeRepositroy;
	
	@Autowired
	StoreRepository storeRepository;
	
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