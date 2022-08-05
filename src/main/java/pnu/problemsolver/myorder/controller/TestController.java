package pnu.problemsolver.myorder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.dto.LoginResponseDTO;
import pnu.problemsolver.myorder.repository.TestRepository;
import pnu.problemsolver.myorder.security.JwtTokenProvider;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.service.StoreService;

import java.util.List;

@Controller
@RestController
@RequiredArgsConstructor
public class TestController {
	
	private final StoreService storeService;
	private final CakeService cakeService;
	private final CustomerService customerService;
	
	private final DemandService demandService;
	private final TestRepository testRepository;
	
	private final JwtTokenProvider jwtTokenProvider;
	
	
	@GetMapping("/")
	public String setting() {
		//store, customer는 연관관계 없어서 먼저 넣을 수 있다
		testRepository.insertAll();
		return "success";
		
		
	}
	
	@GetMapping("/get-test-customer")//
	public LoginResponseDTO getTestCustomer() {
		List<Customer> all = customerService.findByName(i->i, "진윤정");
		Customer customer = all.get(0);
		LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
				.jwt(jwtTokenProvider.createToken(MemberType.CUSTOMER))
				.uuid(customer.getUuid())
				.build();
		return loginResponseDTO;
		
	}
	
	@GetMapping("/get-test-store")
	public LoginResponseDTO getTestStore() {
		List<Store> all = storeService.findByName(i -> i, "솔루션 메이커(동래점)");
		Store store = all.get(0);
		LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder()
				.jwt(jwtTokenProvider.createToken(MemberType.STORE))
				.uuid(store.getUuid())
				.build();
		return loginResponseDTO;
		
	}
	
}
