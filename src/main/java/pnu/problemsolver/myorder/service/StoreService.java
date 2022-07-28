package pnu.problemsolver.myorder.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreDTOForList;
import pnu.problemsolver.myorder.dto.StoreEditDTO;
import pnu.problemsolver.myorder.repository.StoreRepository;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
@Transactional //service에서 필수!
@Slf4j
public class StoreService {
    private final StoreRepository storeRepository;
//    private final ModelMapper mapper;

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
        Optional<Store> store = storeRepository.findById(uuid);//없으면 null
        StoreDTO storeDTO = null;
        if (store.isPresent()) {
            storeDTO = StoreDTO.toDTO(store.get());
        }
        return storeDTO;
    }

    public void saveOnlyNotNUll(StoreEditDTO storeEditDTO, Path storeMainImg) {
        Optional<Store> storeOptional = storeRepository.findById(storeEditDTO.getUuid());
        if (!storeOptional.isPresent()) {
            throw new NullPointerException("findById() got null");
        }
        Store store = storeOptional.get();
        store.setOnlyNotNull(storeEditDTO, storeMainImg);
    }

    public StoreDTO save(StoreDTO storeDTO) {
        Store store = Store.toEntity(storeDTO);
        store = storeRepository.save(store);
        return StoreDTO.toDTO(store);
    }

//    public List<StoreDTOForListPreflight> getAllPreflights() {
//        List<Store> li = storeRepository.findAll();
//        List<StoreDTOForListPreflight> liDTO = new ArrayList<>();
//        for (Store i : li) {
//            liDTO.add(StoreDTOForListPreflight.toDTO(i));
//        }
//        return liDTO;
//    }

    public List<StoreDTOForList> findAllInUUIDList(List<UUID> param) {
        List<Store> li = storeRepository.findAllInUUIDList(param);
        List<StoreDTOForList> resList = new ArrayList<>();
        for (Store i : li) {
            resList.add(StoreDTOForList.toDTO(i)); //filePath->encodeing->byte[]로 만든다.
        }
        return resList;
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
    public StoreDTO findBySnsTypeAndSnsIdentifyKey(StoreDTO storeDTO) {
        List<Store> resList = storeRepository.findBySnsTypeAndSnsIdentifyKey(storeDTO.getSnsType(), storeDTO.getSnsIdentifyKey());
        if (resList.size() == 1) {
            return StoreDTO.toDTO(resList.get(0));
        } else if (resList.isEmpty()) {
            return null;
        }

        throw new RuntimeException("snsType and snsIdentifyKey 중복!");
    }

    public <T> List<T> findAll(Function<Store, T> function) {

        List<Store> li = storeRepository.findAll();
        List<T> resList = new ArrayList<>();
        for (Store i : li) {
            resList.add(function.apply(i));
        }
        return resList;
    }
//    public List<StoreDTOForList> findAll() {
//
//        List<Store> li = storeRepository.findAll();
//        List<StoreDTOForList> resList = new ArrayList<>();
//        for (Store i : li) {
//            resList.add(StoreDTOForList.toDTO(i));
//        }
//        return resList;
//    }

}
