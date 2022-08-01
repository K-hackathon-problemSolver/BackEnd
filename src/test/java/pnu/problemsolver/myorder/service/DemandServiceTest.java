package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.repository.DemandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DemandServiceTest {
	
	@Mock
	DemandRepository demandRepository;
	
	@InjectMocks
	DemandService demandService;
	
	@Test
	public void findByStoreTest() {
		//given
		Store s = Store.builder()
				.uuid(UUID.randomUUID())
				.build();
		Cake c = Cake.builder()
				.uuid(UUID.randomUUID())
				.store(s)
				.build();
		Customer customer1 = Customer.builder()
				.uuid(UUID.randomUUID())
				.build();
		Demand d1 = Demand.builder()
				.uuid(UUID.randomUUID())
				.store(s)
				.cake(c)
				.status(DemandStatus.WAITING)
				.customer(customer1)
				.build();
		
		List<Demand> li = new ArrayList<>();
		li.add(d1);
		given(demandRepository.findByCustomerAndStatus(any(), any(DemandStatus.class), any())).willReturn(li);//any(DemandStatus.class)
		
		//when
		PageRequest of = PageRequest.of(0, 5);
		List<Demand> byCustomer = demandService.findByCustomer(i -> i, customer1.getUuid(), DemandStatus.WAITING, of);
		
		//then
		assertEquals(byCustomer.size(), 1);
		assertEquals(byCustomer.get(0), d1);
		
	}
	
	
}