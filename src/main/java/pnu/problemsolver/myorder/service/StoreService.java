package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.repository.StoreRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional //service에서 필수!
public class StoreService {
    private final StoreRepository storeRepository;
    private final ModelMapper mapper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());



    //
    public StoreDTO findById(String email) {
        Optional<Store> store = storeRepository.findById(email);
        StoreDTO storeDTO = null;
        if (store.isPresent()) {
            storeDTO=mapper.map(store, StoreDTO.class);
        }
        return storeDTO;
    }

    public StoreDTO saveStore(StoreDTO storeDTO) {
        Store store = Store.toEntity(storeDTO);
        store=storeRepository.save(store);
        return mapper.map(store, StoreDTO.class);//삽입된 store를 다시 반환
    }

    //성공하면 email반환. 실패하면 null반환.
    public String login(StoreDTO storeDto) {
        Optional<Store> store = storeRepository.findById(storeDto.getEmail());
        String email = null;

        if (store.isPresent()) {
            if (store.get().getPw().equals(storeDto.getPw())) {
                email = storeDto.getEmail();
            } else {
                log.error("PW불일치!");

            }
        }
        else{
            log.error("email이 없습니다!");
        }
        return email;

    }
}
