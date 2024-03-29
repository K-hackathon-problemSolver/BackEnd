package pnu.problemsolver.myorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.dto.*;
import pnu.problemsolver.myorder.service.DemandService;
import pnu.problemsolver.myorder.util.MyUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RestController
@RequestMapping("/demand")
@Slf4j
public class DemandController {
	
	private DemandService demandService;
	
	private final String demandUploadPath;
	
	@Autowired
	public DemandController(DemandService demandService, Environment env) {
		this.demandService = demandService;
		this.demandUploadPath = Objects.requireNonNull(env.getProperty("myorder.upload.demand"), "application.properties 로드 실패");
	}
	
	@PostMapping("/save")
	public UUID saveDemand(@RequestBody DemandSaveDTO d) {
		//DB에 저장. 먼저 저장해야 demand의 uuid를 알 수 있다.
		UUID uuid = demandService.saveDemandSaveDTO(d);//TODO : 여기서 저장할 때 uuid만 가지고 나머지는 null인 cake, store, customer를 저장한다. toEntity()를 들어가 봐!
		//사진저장. 사진은 없을 수도 있다. 없으면 위에서 saveWithFunction()으로 끝난 것임.
		if (d.getFile() != null) {
			Path imgPath = MyUtil.saveFile(Path.of(demandUploadPath + File.separator + d.getCustomerUUID().toString()), uuid.toString() + "." + d.getExtension(), d.getFile());
			
			demandService.setFilePath(uuid, imgPath);//저장해야 알 수 있기 때문에 sql두번 날리는 것은 어쩔 수 없다.
		}
		return uuid;
	}
	
	@PostMapping("/{status}")
	public List<DemandListResponseDTO> customerDemandList(@PathVariable("status") DemandStatus status, @RequestBody DemandListRequestDTO dto, HttpServletRequest request) {//@RequestHeader도 있다!
		List<DemandListResponseDTO> resList = null;
		MemberType memberType = (MemberType) request.getAttribute("memberType");
		String sortStr = dto.getSort();
		PageRequest pageRequest = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(sortStr == null ? "created" : sortStr).descending());//기본값은 최신순!
		
		if (memberType == MemberType.CUSTOMER) {//customer의 경우 시간대로 한번에 보여준다.
			resList = demandService.findByCustomerPageable(DemandListResponseDTO::toDTO, dto.getUuid(), pageRequest);
		} else if (memberType == MemberType.STORE) {
			resList = demandService.findByStoreIdAndDemandStatusPageable(i -> DemandListResponseDTO.toDTO(i), dto.getUuid(), status, pageRequest);
		} else {//GUEST인지는 확인안해도 된다. 필터에서 확인함.
			log.error("invalid memberType can't access to /demand/{status}");
		}
		return resList;
	}
	
	
	@GetMapping("/detailed")
	//TODO : 하나씩 제공하는 것 보단 리스트로 제공하는게 더 유연성있다. 필요하면 제공하자.
	public DemandDetailResponseDTO detailed(@RequestParam UUID demandId) {
		return demandService.findById(i -> DemandDetailResponseDTO.toDTO(i), demandId);
	}
	
	@PostMapping("/change-status")//필요한 정보
	public DemandDTO changeStatus(@RequestBody ChangeStatusRequestDTO dto) {
		DemandDTO demandDTO = demandService.changeStatus(dto);
		return demandDTO;
	}
	
	
}
