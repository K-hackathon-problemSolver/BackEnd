package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.repository.CakeRepositroy;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CakeService {
    private final CakeRepositroy cakeRepositroy;


    public CakeDTO save(CakeDTO cakeDTO) {
        Cake cake = Cake.toEntity(cakeDTO);
        log.info(cake.toString());
        cake = cakeRepositroy.save(cake);
        return CakeDTO.toDTO(cake);
    }


}
