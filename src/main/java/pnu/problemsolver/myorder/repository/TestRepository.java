package pnu.problemsolver.myorder.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.SNSType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Repository
public class TestRepository {
	
	private final StoreRepository storeRepository;
	private final CustomerRepository customerRepository;
	private final CakeRepositroy cakeRepositroy;
	private final DemandRepository demandRepository;
	
	private Store testStore = null;
	private Customer testCustomer = null;
	
	
	public List<Demand> insertAll() {
		List<Store> storeList = insertStore();
		List<Customer> customers = insertCustomer();
		
		//cake에는 store가 필요함.
		List<Cake> cakes = insertCake(storeList);
		List<Demand> demandList = insertDemand(cakes, customers, storeList);
		return demandList;
	}
	
	//demand는 customer, cake 2개가 필요하다.
	public List<Demand> insertDemand(List<Cake> cakeList, List<Customer> customerList, List<Store> storeList) {
		List<Demand> demandDTOList = new ArrayList<>();
		IntStream.rangeClosed(0, storeList.size() - 1).forEach((i) -> {
			//            int idx = (int) (Math.random() * storeDTOList.size());
			IntStream.rangeClosed(0, 10).forEach(idx -> {
				Path p = Paths.get("src/main/resources/static/" + (idx+1) + ".jpg");
				
				Demand demand = Demand.builder()
						.filePath(p.toString())
						.cake(cakeList.get(i))
						.customer(customerList.get(i))
						.store(storeList.get(i))
						.status(DemandStatus.WAITING)
						.build();
				demandRepository.save(demand);
				demandDTOList.add(demand);
			});
		});
		
		return demandDTOList;
	}
	
	
	public List<Customer> insertCustomer() {
		
		List<Customer> li = new ArrayList<>();
		for (int i = 0; i < 5; ++i) {
			Customer cus = Customer.builder()
					.email("email" + i)
					.name("custmer" + i)
					.phone_num("010-1234-7890")
					.birthYear(1999 + i)
					.snsIdentifyKey("snsid~")
					.snsType(SNSType.NAVER)
					.gender(Gender.MAN)
					.build();
			customerRepository.save(cus);
			li.add(cus);
			
			
		}
		
		for (int i = 0; i < 6; ++i) {
			Customer cus = Customer.builder()
					.email("email" + i)
					.name("custmer" + i)
					.phone_num("010-1234-7890")
					.birthYear(1999 + i)
					.snsIdentifyKey("snsid~")
					.snsType(SNSType.KAKAO)
					.gender(Gender.MAN)
					.build();
			customerRepository.save(cus);
			li.add(cus);
		}
		Customer c = Customer.builder()
				.email("test.pusan.ac.kr")
				.name("테스트 고객")
				.phone_num("010-1234-5678")
				.birthYear(1999)
				.snsIdentifyKey("snsId~")
				.snsType(SNSType.NAVER)
				.gender(Gender.WOMAN)
				.build();
		customerRepository.save(c);
		testCustomer = c;
		li.add(c);
		return li;
	}
	
	public List<Store> insertStore() {
		List<Store> li = new ArrayList<>();
		for (int i = 1; i <= 11; ++i) { //사진이 11개라서 11개만 해야함.
			Store st = Store.builder()
					.snsIdentifyKey("snskey")
					.birthYear(1999)
					.name("store" + i)
					.description("맛있는 가게!")
					.location("부산시 금정구 부산대학로~")
					.email("zhdhfhd33@zsdf")
					.snsType(SNSType.KAKAO)
					.latitude(i)
					.longitude(i)
					.owner_phone_num("010-3391-6486")
					.filePath("src/main/resources/static/" + i + ".jpg")
					.build();
			storeRepository.save(st);
			li.add(st);
		}
		
		Store s = Store.builder()
				.name("솔루션 메이커")
				.snsIdentifyKey("snskey")
				.birthYear(1999)
				.description("맛있는 가게!")
				.location("부산시 금정구 부산대학로~")
				.email("zhdhfhd33@zsdf")
				.snsType(SNSType.KAKAO)
				.latitude(35.163761)
				.longitude(129.11328)
				.owner_phone_num("010-3391-6486")
				.filePath("src/main/resources/static/" + 4 + ".jpg")
				.build();
		storeRepository.save(s);
		testStore = s;
		li.add(s);
		
		return li;
	}
	
	
	public List<Cake> insertCake(List<Store> list) {//cake는 연관관계가 있어서 store를 필요로 한다.
		List<Cake> li = new ArrayList<>();
		String option = "{\"시트 선택\" : [\"기본맛(커스터드)\", \"크림치즈\", \"버터크림\"], \"사이즈\" : [\"1호\", \"2호\", \"3호\"], \"모양 선택\" : [\"원형\", \"네모\", \"하트\", \"별\"], \"보냉 유무\" : [\"유\", \"무]\"], \"문구\" : \"null\"}";
		for (int i = 0; i < list.size(); ++i) {//lise.size() = 11
			Cake cake = Cake.builder()
					.minPrice(14900)
					.name("도시락 케이크")
					.option(option)
					.store(list.get(i)) //연관관계
					.description("생일 선물로 딱맞음!!")
					.filePath("src/main/resources/static/" + (4) + ".jpg")
					.build();
			
			cakeRepositroy.save(cake);
			li.add(cake);
		}
		
		for (int i = 0; i < list.size(); ++i) {//lise.size() = 11
			Cake cake = Cake.builder()
					.minPrice(19900)
					.name("시그니처 케이크")
					.option(option)
					.store(list.get(i)) //연관관계
					.description("생일 선물로 딱맞음!!")
					.filePath("src/main/resources/static/" + (5) + ".jpg")
					.build();
			
			cakeRepositroy.save(cake);
			li.add(cake);
		}
		return li;
		
	}
	
}
