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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    
    public <T> List<T> findByStoreUUID(Function<Cake, T> func, UUID storeID) {
        Store s = Store.builder()
                .uuid(storeID)
                .build();
        List<Cake> byStore = cakeRepositroy.findByStore(s);
        return byStore.stream().map(i -> func.apply(i)).collect(Collectors.toList());
    }
    
    public <T> T findById(Function<Cake, T> func, UUID uuid) {
    
        Optional<Cake> byId = cakeRepositroy.findById(uuid);
        if (!byId.isPresent()) {
            return null;
        }
        Cake cake = byId.get();
        return func.apply(cake);
    }
    

}
