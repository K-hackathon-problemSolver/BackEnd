package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.repository.CakeRepositroy;
import pnu.problemsolver.myorder.repository.DemandRepository;

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

}
