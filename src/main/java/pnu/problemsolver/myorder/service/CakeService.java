package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pnu.problemsolver.myorder.domain.Cake;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.CakeDTO;
import pnu.problemsolver.myorder.dto.CakeEditDTO;
import pnu.problemsolver.myorder.repository.CakeRepositroy;

import java.nio.file.Path;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CakeService {
    private final CakeRepositroy cakeRepositroy;


    public void save(CakeDTO cakeDTO) {
        Cake cake = Cake.toEntity(cakeDTO);
        cakeRepositroy.save(cake);
        cakeDTO.setUuid(cake.getUuid());
        
    }

    public void saveOnlyNotNUll(CakeEditDTO cakeEditDTO, Path cakeImgPath) {
        Optional<Cake> cakeOptional = cakeRepositroy.findById(cakeEditDTO.getUuid());
        if (!cakeOptional.isPresent()) {
            throw new NullPointerException("findById() got null");
        }
        Cake cake = cakeOptional.get();
        cake.setOnlyNotNull(cakeEditDTO, cakeImgPath);
    }


}
