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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DemandService {
    private final DemandRepository demandRepository;
    public void save(DemandDTO dto) {
        Demand demand = Demand.toEntity(dto);
        demandRepository.save(demand);
    }

}
