package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.ChangeStatusRequestDTO;
import pnu.problemsolver.myorder.repository.TestRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AllServiceSpringBootTest {
	
	
	@Autowired
	DemandService demandService;
	
	@Autowired
	StoreService storeService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	CakeService cakeService;
	
	@Autowired
	TestRepository testRepository;
	
	@Test
	public void Demand_changeStatusTest() {
		testRepository.insertAll();
		List<Store> storeList = storeService.findAll(i -> i);
		List<Demand> demandList = demandService.findByStoreIdAndDemandStatus(i -> i, storeList.get(0).getUuid(), DemandStatus.WAITING, 0);
		Demand demand = demandList.get(0);
		ChangeStatusRequestDTO dto = ChangeStatusRequestDTO.builder()
				.changeStatusTo(DemandStatus.ACCEPTED)
				.demandId(demand.getUuid())
//				.storeId(storeList.get(0).getUuid())
				.build();
		demandService.changeStatus(dto);
		Demand byId = demandService.findById(i -> i, demand.getUuid());
		
//		System.out.println(byId);
//				System.out.println(demand);
		
		assertEquals(demand.getStatus(), DemandStatus.WAITING);
		assertEquals(byId.getStatus(), DemandStatus.ACCEPTED);
		
		
	}
}
