package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.ChangeStatusRequestDTO;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.dto.DemandSaveDTO;
import pnu.problemsolver.myorder.repository.CakeRepositroy;
import pnu.problemsolver.myorder.repository.CustomerRepository;
import pnu.problemsolver.myorder.repository.DemandRepository;
import pnu.problemsolver.myorder.repository.StoreRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DemandService {
	private final DemandRepository demandRepository;
	
	private final StoreRepository storeRepository;
	private final CustomerRepository customerRepository;
	private final CakeRepositroy cakeRepositroy;
	
	public void save(DemandDTO dto) {
		Demand demand = Demand.toEntity(dto);
		demandRepository.save(demand);
		dto.setUuid(demand.getUuid());
//        System.out.println("실험2 : " + dto.getUuid());
	}
	
	/**
	 * save할 때는 T-> Entity이다.
	 * find할 때는 Entity -> T임.
	 * 제네릭을 사용하면 넘어온 dto 클래스에 uuid를 설정못해준다. setUuid()자체가 없음. Object로 취급. 그대로 dto클래스를 반환한다고 해도 해결안됨.
	 * ->uuid만 반환하면 된다.
	 */
	public <T> UUID saveWithFunction(Function<T, Demand> func, T dto) {
		Demand demand = func.apply(dto);
		demandRepository.save(demand);
		return demand.getUuid();
	}
	
	public <T> UUID saveDemandSaveDTO(DemandSaveDTO dto) {//TODO : 얘를 안써도 되는지 모르겠다.
		Optional<Store> storeOpt = storeRepository.findById(dto.getStoreUUID());
		Optional<Customer> customerOpt = customerRepository.findById(dto.getCustomerUUID());
		Optional<Cake> cakeOpt = cakeRepositroy.findById(dto.getCakeUUID());
		if (!storeOpt.isPresent()) {//어짜피 자동으로 예외 띄우기 때문에 내가 굳이 처리할 필요 없다.
			throw new NullPointerException("storeRepository.findById() return null");
		}
		if (!customerOpt.isPresent()) {
			throw new NullPointerException("customerRepository.findById() return null");
		}
		if (!cakeOpt.isPresent()) {
			throw new NullPointerException("cakeRepository.findById() return null");
		}
		Demand demand = Demand.toEntity(dto, null, storeOpt.get(), customerOpt.get(), cakeOpt.get());
		demandRepository.save(demand);
		return demand.getUuid();
	}
	
	public <T> List<T> findByCustomerIdAndDemandStatusPageable(Function<Demand, T> func, UUID uuid, DemandStatus status, Pageable pageable) {//이정도는 Repository에서 테스트 했다면 검사 안해도 된다. TDD도 아니니까.
		Customer c = Customer.builder()
				.uuid(uuid)
				.build();
		List<Demand> byCustomer = demandRepository.findByCustomerAndStatus(c, status, pageable);
		List<T> resList = new ArrayList<>();
		for (Demand i : byCustomer) {
			resList.add(func.apply(i));
		}
		return resList;
	}
	
	public <T> List<T> findByStoreIdAndDemandStatusPageable(Function<Demand, T> func, UUID uuid, DemandStatus status, Pageable pageable) {
		Store s = Store.builder()//service에선 엔티티를 생서해서 다룰 수 있다. 그래서 편하고 좋다.
				.uuid(uuid)
				.build();
		List<Demand> byCustomer = demandRepository.findByStoreAndStatus(s, status, pageable);
		
		List<T> resList = new ArrayList<>();
		for (Demand i : byCustomer) {
			resList.add(func.apply(i));
		}
		return resList;
	}
	
	public <T> List<T> findByCustomerPageable(Function<Demand, T> func, UUID uuid, Pageable pageable) {
		Customer customer = Customer.builder()
				.uuid(uuid)
				.build();
		List<Demand> byCustomer = demandRepository.findByCustomer(customer, pageable);
		return byCustomer.stream().map(i -> func.apply(i)).collect(Collectors.toList());
	}
	
	
	/**
	 * @param func demand->want type converter넣어주면 된다.
	 * @param uuid
	 * @param <T>
	 * @return 없으면 null반환.
	 */
	public <T> T findById(Function<Demand, T> func, UUID uuid) {
		Optional<Demand> demandOp = demandRepository.findById(uuid);
		if (!demandOp.isPresent()) {
			return null;
		}
		Demand demand = demandOp.get();
		return func.apply(demand);
	}
	
	public DemandDTO changeStatus(ChangeStatusRequestDTO dto) {
		UUID demandId = dto.getDemandId();
		Optional<Demand> demandOpt = demandRepository.findById(demandId);//select 호출안됨. 영속성 컨텍스트에 있기 때문이다.
		if (!demandOpt.isPresent()) {
			throw new NullPointerException("demandRepository.findById() not found!");
		}
		
		Demand demand = demandOpt.get();
		DemandStatus changeStatusTo = dto.getChangeStatusTo();
		if (changeStatusTo == DemandStatus.ACCEPTED) {
			demand.changeToAccepted();
		} else if (changeStatusTo == DemandStatus.WAITING) {//waiting으로 기다리는 것은 말이 안된다.
			throw new IllegalArgumentException("change to WAITING doesn't make sense!");
		} else if (changeStatusTo == DemandStatus.COMPLETED) {
			demand.changeToCompleted();
		} else if (changeStatusTo == DemandStatus.REJECTED) {
			demand.changeToRejected();
		} else {
			throw new IllegalArgumentException("ENUM에 정의되어 있지 않음.");
		}
		return DemandDTO.toDTO(demand);
	}
	
	public <T> List<T> findAll(Function<Demand, T> func) {
		List<Demand> all = demandRepository.findAll();
		return all.stream().map(i -> func.apply(i)).collect(Collectors.toList());
		
	}
	
	public void setFilePath(UUID uuid, Path imgPath) {
		Optional<Demand> byId = demandRepository.findById(uuid);
		if (!byId.isPresent()) {
			throw new NullPointerException("findById() not found!!");
		}
		Demand demand = byId.get();
		demand.setFilePath(imgPath);
		
	}
	
	
}
