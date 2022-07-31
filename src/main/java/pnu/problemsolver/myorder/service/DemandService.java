package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.repository.DemandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DemandService {
    private final DemandRepository demandRepository;
    public void save(DemandDTO dto) {
        Demand demand = Demand.toEntity(dto);
        demandRepository.save(demand);
        dto.setUuid(demand.getUuid());
        System.out.println("실험2 : " + dto.getUuid());
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
    
    public <T> List<T> findByCustomer(Function<Demand, T> func, UUID uuid, DemandStatus status) {//이정도는 Repository에서 테스트 했다면 검사 안해도 된다. TDD도 아니니까.
        Customer c = Customer.builder()
                .uuid(uuid)
                .build();
        List<Demand> byCustomer = demandRepository.findByCustomerAndStatus(c, status);
        List<T> resList = new ArrayList<>();
        for (Demand i : byCustomer) {
            resList.add(func.apply(i));
        }
        return resList;
    }
    
    public <T> List<T> findByStore(Function<Demand, T> func, UUID uuid, DemandStatus status) {
        Store s = Store.builder()//service에선 엔티티를 생서해서 다룰 수 있다. 그래서 편하고 좋다.
                .uuid(uuid)
                .build();
        List<Demand> byCustomer = demandRepository.findByStoreAndStatus(s, status);
        List<T> resList = new ArrayList<>();
        for (Demand i : byCustomer) {
            resList.add(func.apply(i));
        }
        return resList;
    }
    
    /**
     *
     * @param func demand->want type converter넣어주면 된다.
     * @param uuid
     * @return 없으면 null반환.
     * @param <T>
     */
    public <T> T findById(Function<Demand, T> func, UUID uuid) {
        Optional<Demand> demandOp = demandRepository.findById(uuid);
        if (!demandOp.isPresent()) {
            return null;
        }
        Demand demand = demandOp.get();
        return func.apply(demand);
    }
    
}
