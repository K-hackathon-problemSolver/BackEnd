package pnu.problemsolver.myorder.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)//이게 있어야 BeforeAll가능. 기본값은 PER_METHOD라고 함!
class DemandRepositoryTest {
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	StoreRepository storeRepository;
	
	@Autowired
	DemandRepository demandRepository;
	
	@Autowired
	CakeRepositroy cakeRepositroy;
	
	@Autowired
	TestRepository testRepository;
	
	
	@BeforeAll
	public void beforeAll() {
		testRepository.insertAll();
	}
	
	@Test
	public void setterTest() {
		List<Cake> all = cakeRepositroy.findAll();
		Cake cake = all.get(0);
		List<Store> all1 = storeRepository.findAll();
		Store s = all1.get(0);
		Cake newCake = Cake.builder()
				.uuid(cake.getUuid())
				.store(s)
				.minPrice(3)
				.name("새로만듦")
				.build();
		cakeRepositroy.save(newCake); //description이 없어진다. null이 대입됨. update하려면 도메인 객체에 null아닌 것만 set하는 함수를 만들어야 한다.
		Optional<Cake> byId = cakeRepositroy.findById(newCake.getUuid());
		assertEquals(byId.isPresent(), true);
		assertEquals(byId.get().getDescription(), null);
		
		List<Customer> all2 = customerRepository.findAll();
		Customer customer = all2.get(0);
		Demand demand = Demand.builder()
				.customer(customer)
				.cake(cake)
				.store(s)
				.status(DemandStatus.WAITING)
				.build();
		
		demandRepository.save(demand);
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		demand.changeToAccepted();//주문수락.
//		assertEquals(demand.getCreated().equals(demand.getModified()), false);//트랜잭션이 안일어나서 그런듯.
//		demandRepository.save(demand); //저장하지 않고 set만 호출해도 된다. set만 호출해야 update됨.
		Optional<Demand> d = demandRepository.findById(demand.getUuid());
		assertEquals(d.isPresent(), true);
		assertEquals(d.get().getStatus(), DemandStatus.ACCEPTED);
	}
	
	@Test
	public void findByCustomerTest() {
		List<Customer> all = customerRepository.findAll();
		Customer customer = all.get(0);
		PageRequest of = PageRequest.of(0, 5, Sort.by("created").descending());//sort.by()에는 필드이름을 넣으면 된다.
		System.out.println(customer);
		List<Demand> demandList = demandRepository.findByCustomerAndStatus(customer, DemandStatus.WAITING, of);
		assertEquals( 5, demandList.size());
		LocalDateTime max = demandList.get(0).getCreated();
		for (Demand d : demandList) {
			int i = max.compareTo(d.getCreated());
			assertEquals(i == 1 || i == 0, true);
			max = d.getCreated();
			assertEquals(d.getCustomer().equals(customer), true);
		}
	}
	@Test
	public void findByStoreTest() {
		List<Store> all = storeRepository.findAll();
		Store st = all.get(0);
		PageRequest of = PageRequest.of(0, 5, Sort.by("created").descending());
		
		List<Demand> demandList = demandRepository.findByStoreAndStatus(st, DemandStatus.WAITING, of);
		assertEquals(demandList.size(), 5);
		
		
		LocalDateTime max = demandList.get(0).getCreated();
		for (Demand d : demandList) {
			int i = max.compareTo(d.getCreated());
			assertEquals(i == 1 || i == 0, true);
			assertEquals(d.getStore().equals(st), true);
			
		}
	}
	
	@Test
	public void findByStatusAndCreatedBetweenTest() {
		//말일 구하기..
		Calendar cal = Calendar.getInstance();
		cal.set(2019, 7, 1);//0이 1월임.
		int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		LocalDateTime a = LocalDateTime.of(2022, Month.of(8), 1, 0, 0, 0);
		LocalDateTime b = LocalDateTime.of(2022, Month.of(8), actualMaximum, 0, 0, 0);
		
		List<Demand> byStatusAndCreatedBetween = demandRepository.findByStatusAndCreatedBetween(DemandStatus.WAITING, a, b);
		
		for (Demand i : byStatusAndCreatedBetween) {
			System.out.println("1");
			System.out.println(i);
		}
		
	}
	
	@Test
	    public void setTest() {
	    
	    }
	
	
}