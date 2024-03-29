package pnu.problemsolver.myorder.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.Gender;
import pnu.problemsolver.myorder.domain.constant.PusanLocation;
import pnu.problemsolver.myorder.domain.constant.SNSType;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Slf4j
//@Transactional
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
		if (storeList.size() != customerList.size()) {
			throw new RuntimeException("storeList, customerList의 크기가 다릅니다");
		}
		IntStream.rangeClosed(0, storeList.size() - 1).forEach((i) -> {
			//            int idx = (int) (Math.random() * storeDTOList.size());
			IntStream.rangeClosed(0, 10).forEach(idx -> {
				Path p = Paths.get("src/main/resources/static/" + (idx + 1) + ".jpg");
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
				.name("진윤정")
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
		String[] storeName = {"도도 케이크", "일루전 케이크", "짱구네 케이크", "우영우 케이크", "쿠키 케이크", "케이킷", "에마롱 케이크", "두소녀", "세소녀", "다섯소녀", "백설공주와 일곱 난쟁이"};
		
		List<Store> li = new ArrayList<>();
		for (int i = 1; i <= 11; ++i) { //사진이 11개라서 11개만 해야함.
			Store st = Store.builder()
					.snsIdentifyKey("snskey")
					.birthYear(1999)
					.name(storeName[i - 1])
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
				.name("솔루션 메이커(동래점)")
				.snsIdentifyKey("snskey")
				.birthYear(1999)
				.description("맛있는 가게!")
				.location("부산시 금정구 부산대학로~")
				.email("zhdhfhd33@zsdf")
				.snsType(SNSType.KAKAO)
				.latitude(PusanLocation.DONGLAE.latitude)
				.longitude(PusanLocation.DONGLAE.longitude)
				.owner_phone_num("010-3391-6486")
				.filePath("src/main/resources/static/" + 4 + ".jpg")
				.build();
		storeRepository.save(s);
		testStore = s;
		li.add(s);
		
		
		Store s1 = Store.builder()
				.name("솔루션 메이커(수영점)")
				.snsIdentifyKey("snskey")
				.birthYear(1999)
				.description("맛있는 가게!")
				.location("부산시 금정구 부산대학로~")
				.email("zhdhfhd33@zsdf")
				.snsType(SNSType.KAKAO)
				.latitude(PusanLocation.SUYUNG.latitude)
				.longitude(PusanLocation.SUYUNG.longitude)
				.owner_phone_num("010-3391-6486")
				.filePath("src/main/resources/static/" + 5 + ".jpg")
				.build();
		storeRepository.save(s1);
		
		Store s2 = Store.builder()
				.name("솔루션 메이커(광안점)")
				.snsIdentifyKey("snskey")
				.birthYear(1999)
				.description("맛있는 가게!")
				.location("부산시 금정구 부산대학로~")
				.email("zhdhfhd33@zsdf")
				.snsType(SNSType.KAKAO)
				.latitude(PusanLocation.GUMJUNG.latitude)
				.longitude(PusanLocation.GUMJUNG.longitude)
				.owner_phone_num("010-3391-6486")
				.filePath("src/main/resources/static/" + 6 + ".jpg")
				.build();
		storeRepository.save(s2);
		
		return li;
	}
	
	
	public List<Cake> insertCake(List<Store> list) {//cake는 연관관계가 있어서 store를 필요로 한다.
		List<Cake> li = new ArrayList<>();
		String option = "{\"data\" : {\"시트 선택\" : {\"기본맛(커스터드)\":6900, \"크림치즈\":7900, \"버터크림\":7900 }, \"사이즈\" : {\"1호\":1200, \"2호\":1400, \"3호\":1500}, \"모양 선택\" : {\"원형\":1234, \"네모\":2345, \"하트\":1234, \"별\":2345}, \"보냉 유무\" : {\"유\":0, \"무\":2 }}}";
		
		for (int i = 0; i < list.size(); ++i) {//lise.size() = 11
			Cake cake = Cake.builder()
					.minPrice(14900)
					.name("도시락 케이크")
					.option(option)
					.store(list.get(i)) //연관관계
					.description("기념일에 딱 좋은 도식락 케이크")
					.filePath("src/main/resources/static/" + (7) + ".jpg")
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
					.description("주인장이 직접 만든 시그니처 메뉴")
					.filePath("src/main/resources/static/" + (8) + ".jpg")
					.build();
			cakeRepositroy.save(cake);
			li.add(cake);
		}
		return li;
	}
	
	public void deleteAll() {
		demandRepository.deleteAll();
		cakeRepositroy.deleteAll();
		customerRepository.deleteAll();
		storeRepository.deleteAll();
		
	}
	
	
	public List<Store> getTestStore() {
		List<Store> res = storeRepository.findByName("솔루션 메이커(동래점)");
		return res;
	}
	
	public List<Customer> getTestCustomer() {
		List<Customer> byName = customerRepository.findByName("진윤정");
		return byName;
	}
	
}
