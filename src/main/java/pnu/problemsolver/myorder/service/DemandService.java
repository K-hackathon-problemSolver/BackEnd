package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.repository.CakeRepositroy;
import pnu.problemsolver.myorder.repository.DemandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DemandService {
    private final DemandRepository demandRepository;
//    public void save(DemandDTO dto) {
//        Demand demand = Demand.toEntity(dto);
//        demandRepository.save(demand);
//    }
    
    /**
     * save할 때는 T-> Entity이다.
     * find할 때는 Entity -> T임.
     */
    public <T> void save(Function<T, Demand> func, T dto) {
        Demand demand = func.apply(dto);
        demandRepository.save(demand);
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

}
