package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreDTOForListPreflight;
import pnu.problemsolver.myorder.dto.StoreEditDTO;
import pnu.problemsolver.myorder.repository.StoreRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional //service에서 필수!
public class StoreService {
    private final StoreRepository storeRepository;
//    private final ModelMapper mapper;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * findById()를 오버로딩. 필요에 따라 2개 필요할 것 같아서! 그리고 다른 함수를 사용해서 작성하는게 수정에 유리.
     *
     * @param storeDTO
     * @return
     */
    public StoreDTO findById(StoreDTO storeDTO) {
        return findById(storeDTO.getUuid());
    }

    public StoreDTO findById(UUID uuid) {
        Optional<Store> store = storeRepository.findById(uuid);
        StoreDTO storeDTO = null;
        if (store.isPresent()) {
            storeDTO = StoreDTO.toDTO(store.get());
        }
        return storeDTO;
    }

    public StoreDTO save(StoreDTO storeDTO) {
        Store store = Store.toEntity(storeDTO);
        store = storeRepository.save(store);
        return StoreDTO.toDTO(store);
    }

    public List<StoreDTOForListPreflight> getAllPreflights() {
        List<Store> li = storeRepository.findAll();
        List<StoreDTOForListPreflight> liDTO = new ArrayList<>();
        for (Store i : li) {
            liDTO.add(StoreDTOForListPreflight.toDTO(i));
        }
        return liDTO;
    }

    //성공하면 email반환. 실패하면 null반환.
//    public String login(StoreDTO storeDto) {
//        Optional<Store> store = storeRepository.findById(storeDto.getUuid());
//        String email = null;
//
//        if (store.isPresent()) {
//            if (store.get().getPw().equals(storeDto.getPw())) {
//                email = storeDto.getEmail();
//            } else {
//                log.error("PW 불일치!");
//            }
//        }
//        else{
//            log.error("email이 없습니다!");
//        }
//        return email;
//
//    }
}
