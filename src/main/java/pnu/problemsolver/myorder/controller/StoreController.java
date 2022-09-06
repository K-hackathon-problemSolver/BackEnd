package pnu.problemsolver.myorder.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.service.CakeService;
import pnu.problemsolver.myorder.service.StoreService;
import pnu.problemsolver.myorder.util.MyUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {
	private final StoreService storeService;
	private final CakeService cakeService;
	
	private final String uploadPath;
	
	//final 쓰기 위해 어쩔 수 없이 생성자 만들어야함.
	public StoreController(Environment en, StoreService storeService, CakeService cakeService) {
		this.storeService = storeService;
		this.uploadPath = Objects.requireNonNull(en.getProperty("myorder.upload.store"), "application.properties 로드 실패");
		this.cakeService = cakeService;
	}
	
	@PostMapping("/save")//로그인이 아니라 추가적인 정보를 넣을 때 사용.
	public StoreDTO saveStore(@RequestBody StoreDTO storeDTO) {
		storeService.save(storeDTO); //기존의 값을 다 포함해서 넣어야 한다.
		return storeDTO;
	}
	/**
	 * @param storeEditDTO
	 * @return 유효하지 않은 uuid를 가지고 있으면 null반환. 성공하면 success
	 * @throws IOException
	 */
	@PostMapping("/editMenu")
	public String editStoreMenu(@RequestBody StoreEditDTO storeEditDTO) throws IOException {
		if (!MyUtil.isFileExtensionOk(storeEditDTO.getExtension())) {
			return "\"jpg\", \"jpeg\", \"png\", \"bmp\", \"jfif\"확장자만 가능합니다."; //pdf는 지움.
		}
		//store dir 만들기.
		Path dirPath = Path.of(uploadPath + File.separator + storeEditDTO.getUuid());
		Path mainImgPath = MyUtil.saveFile(dirPath, "mainImg." + storeEditDTO.getExtension(), storeEditDTO.getMainImg());
		
		List<CakeEditDTO> cakeEditDTOList = storeEditDTO.getCakeList();
		
		//cake사진 저장.
		List<Path> cakePathList = new ArrayList<>();
		
		for (CakeEditDTO i : cakeEditDTOList) {
			cakePathList.add(MyUtil.saveFile(dirPath, i.getName() + "." + i.getExtension(), i.getImg()));
		}
		
		//store update
		storeService.saveOnlyNotNUll(storeEditDTO, mainImgPath);
		
		//cake update.
		//TODO 시간 남으면 sql하나로 만들기. 아래처럼 하면 성능 bad.
		for (int i = 0; i < cakeEditDTOList.size(); ++i) {
			cakeService.saveOnlyNotNUll(cakeEditDTOList.get(i), cakePathList.get(i));
		}
		return "success";
	}
	
	
	@PostMapping("/editImpossibleDate")
	public String editImpossibleDate(@RequestBody Map map) throws JsonProcessingException {//map으로 받을 때 무조건 string으로 받는게 아니다. list면 ArrayList로 받음
		ArrayList impossibleDate = (ArrayList) map.get("impossibleDate");
		storeService.editImpossibleDate(UUID.fromString((String) map.get("uuid")), impossibleDate);
		return "success";
		
	}

	
	/**
	 * 사용자 위치기반 추천
	 *
	 * @param d
	 * @return
	 */
	@GetMapping("/list")
	public List<StoreListResponseDTO> list(StoreListRequestDTO d) {
		if (d.getLocation() == null || d.getLimit() == 0) {
			return null;
		}
		List<StoreListResponseDTO> byLocation = storeService.findByLocation(a -> StoreListResponseDTO.toDTO(a), d.getLocation(), d.getLimit(), d.getOffset());
		return byLocation;
	}
	
	
	/**
	 * 가게 하나를 보여줄 때에도 StoreEditDTO를 사용한다.
	 *
	 * @param id
	 * @return
	 */
	@GetMapping("")// /store?id=~ 이런식이니까 아무것도 들어가면 안된다.
	public StoreEditDTO oneStore(@RequestParam UUID id) {
		//store id로 cake모두 찾기
		List<CakeEditDTO> cakeEditDTOList = cakeService.findByStoreUUID(CakeEditDTO::toDTO, id);
		//store 찾기.
		StoreEditDTO storeEditDTO = storeService.findById(i -> StoreEditDTO.toDTO(i), id);
		//cakeList연결
		storeEditDTO.setCakeList(cakeEditDTOList);//cakeList는 toDTO에서 처리 못하기 때문에 따로 설정해줘야 한다.
		return storeEditDTO;
	}
	
	//TODO : 통계만 하면 된다.
	@PostMapping("/statistic")
	public void statistic() {
		//store에서 제공하는 모든 종류를 알아야한다. 이걸 알아야 보여주기 가능!
		//status가 completed이고, 한달단위로 가져온다. sql을 여러번 보내는 것 보다 다 가져오는게 좋아보인다.. 탐색을 너무 많이하게 하면 안좋음.
		//잘 정리해서 보여준다.
		//
	}
	
}
