package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.ChangeStatusRequestDTO;
import pnu.problemsolver.myorder.repository.TestRepository;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
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
//	@Commit//테스트에서는 commit하지 않기 때문에 변경감지가 안된다. tranactional에서 commit없이 다 돌려버림. commit을 붙이니까 update문이 실행되었다. commit이 없어도 sql만 실행 안되지 영속성 컨텍스트에서는 그대로 남아있다. 그래서 테스트는 통과함.
	public void Demand_changeStatusTest() {
		//given
		testRepository.insertAll();
		List<Store> storeList = storeService.findAll(i -> i);
		PageRequest pageRequest = PageRequest.of(0, 6, Sort.by("created"));
		
		//selet가 실행됨. 영속성 컨텍스트에 불러와짐.
		List<Demand> demandList = demandService.findByStoreIdAndDemandStatusPageable(i -> i, storeList.get(0).getUuid(), DemandStatus.WAITING, pageRequest);
		Demand demand = demandList.get(0);
		ChangeStatusRequestDTO dto = ChangeStatusRequestDTO.builder()
				.changeStatusTo(DemandStatus.ACCEPTED)
				.demandId(demand.getUuid())
//				.storeId(storeList.get(0).getUuid())
				.build();
		
		//when
		demandService.changeStatus(dto);//영속성 컨텍스트에서만 바뀐다. Commit을 하지 않아서 SQL은 실행안됨.
		Demand byId = demandService.findById(i -> i, demand.getUuid());//select문이 실행안됨.
		System.out.println(demand);
		
		assertEquals(demand, byId);//이건 통과한다...
		assertEquals(demand == byId, true);//영속성에서 가져오기 때문에 둘은 주소도 같다.
		assertEquals("a", "a");//주소가 아니라 실제 값을 비교함.
		assertEquals(byId.getStatus(), DemandStatus.ACCEPTED);
	}
}

