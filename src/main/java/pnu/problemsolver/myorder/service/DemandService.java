package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Customer;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.Store;
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
    
    public <T> List<T> findByCustomer(Function<Demand, T> func, UUID uuid) {
        Customer c = Customer.builder()
                .uuid(uuid)
                .build();
        List<Demand> byCustomer = demandRepository.findByCustomer(c);
        List<T> resList = new ArrayList<>();
        for (Demand i : byCustomer) {
            resList.add(func.apply(i));
        }
        return resList;
    }
    
    public <T> List<T> findByStore(Function<Demand, T> func, UUID uuid) {
        Store s = Store.builder()
                .uuid(uuid)
                .build();
        List<Demand> byCustomer = demandRepository.findByStore(s);
        List<T> resList = new ArrayList<>();
        for (Demand i : byCustomer) {
            resList.add(func.apply(i));
        }
        return resList;
    }

}
