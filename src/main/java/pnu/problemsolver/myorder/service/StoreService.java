package pnu.problemsolver.myorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.domain.Store;
import pnu.problemsolver.myorder.domain.constant.PusanLocation;
import pnu.problemsolver.myorder.domain.constant.SNSType;
import pnu.problemsolver.myorder.dto.EditImpossibleDateDTO;
import pnu.problemsolver.myorder.dto.GeneralOAuthDTO;
import pnu.problemsolver.myorder.dto.StoreDTO;
import pnu.problemsolver.myorder.dto.StoreEditDTO;
import pnu.problemsolver.myorder.repository.StoreRepository;
import pnu.problemsolver.myorder.util.Mapper;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional //service에서 필수!
@Slf4j
public class StoreService {
	private final StoreRepository storeRepository;
	
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
	
	//id로 찾아서 원하는 형태로 반환한다. service에서는 이런 형태가 편함.!
	public <T> T findById(Function<Store, T> func, UUID uuid) {
		Optional<Store> store = storeRepository.findById(uuid);//없으면 null
		T res = null;
		if (store.isPresent()) {
			res = func.apply(store.get());
		}
		return res;
	}
	
	public void saveOnlyNotNUll(StoreEditDTO storeEditDTO, Path storeMainImg) {
		Optional<Store> storeOptional = storeRepository.findById(storeEditDTO.getUuid());
		if (!storeOptional.isPresent()) {
			throw new NullPointerException("findById() got null");
		}
		Store store = storeOptional.get();
		store.setOnlyNotNull(storeEditDTO, storeMainImg);//여기서 다시 save호출할 필요 없다.
	}
	
	public void save(StoreDTO storeDTO) {
		Store store = Store.toEntity(storeDTO);
		storeRepository.save(store);
		storeDTO.setUuid(store.getUuid());//저장하기전에는 uuid가 없다.
	}

//    public StoreDTO save(StoreDTO storeDTO) {
//        Store store = Store.toEntity(storeDTO);
//        Store save = storeRepository.save(store);
//        return StoreDTO.toDTO(save);
//    }
//
//

//    public List<StoreDTOForListPreflight> getAllPreflights() {
//        List<Store> li = storeRepository.findAll();
//        List<StoreDTOForListPreflight> liDTO = new ArrayList<>();
//        for (Store i : li) {
//            liDTO.add(StoreDTOForListPreflight.toDTO(i));
//        }
//        return liDTO;
//    }

//    public List<StoreListResponseDTO> findAllInUUIDList(List<UUID> param) {
//        List<Store> li = storeRepository.findAllInUUIDList(param);
//        List<StoreListResponseDTO> resList = new ArrayList<>();
//        for (Store i : li) {
//            resList.add(StoreListResponseDTO.toDTO(i)); //filePath->encodeing->byte[]로 만든다.
//        }
//        return resList;
//    }
	
	public <T> T findBySnsTypeAndSnsIdentifyKey(Function<Store, T> func, SNSType snsType, String snsIdKey) {
		
		List<Store> resList = storeRepository.findBySnsTypeAndSnsIdentifyKey(snsType, snsIdKey);
		
		if (resList.size() == 1) {
			return func.apply(resList.get(0));
		} else if (resList.isEmpty()) {
			return null;
		}
		
		throw new RuntimeException("snsType and snsIdentifyKey 중복!");
	}
	
	public <T> List<T> findAll(Function<Store, T> function) {//첫 함수형 프로그래밍.
		
		List<Store> li = storeRepository.findAll();
		List<T> resList = new ArrayList<>();
		for (Store i : li) {
			resList.add(function.apply(i));
		}
		return resList;
	}
	
	public <T> List<T> findByLocation(Function<Store, T> function, PusanLocation loc, int limit, int offset) {//내위치,limit, offset,
		List<Store> li = storeRepository.findByLocation(loc.latitude, loc.longitude, limit, offset);
		List<T> res = new ArrayList<>();
		for (Store i : li) {
			res.add(function.apply(i));
		}
		return res;
	}
	
	public <T> List<T> findByName(Function<Store, T> func, String name) {
		List<Store> byName = storeRepository.findByName(name);
		return byName.stream().map(i -> func.apply(i)).collect(Collectors.toList());
	}
	
	public void setFcmToken(UUID uuid, String token) {
		Optional<Store> byId = storeRepository.findById(uuid);
		if (!byId.isPresent()) {
			throw new NullPointerException("findById() return null");
		}
		Store store = byId.get();
		store.setFcmToken(token);
		
	}
	
	public void editImpossibleDate(UUID id, List<EditImpossibleDateDTO> list) throws JsonProcessingException {
		Optional<Store> byId = storeRepository.findById(id);
		if (!byId.isPresent()) {
			throw new NullPointerException("findById return null!");
		}
		Store store = byId.get();
		store.updateImpossibleDate(Mapper.objectMapper.writeValueAsString(list));
	}
	
	public UUID login(GeneralOAuthDTO dto) {
		List<Store> list = storeRepository.findBySnsTypeAndSnsIdentifyKey(dto.getSnsType(), dto.getSnsIdentifyKey());
		if (list.size() == 1) {//원래 회원
			return list.get(0).getUuid();
		} else if (list.size() == 0) {//처음가입
			Store store = Store.toEntity(dto);
			storeRepository.save(store);
			return store.getUuid();
		} else {
			throw new RuntimeException("sns identifyKey is duplicated");
		}
	}
	
}
