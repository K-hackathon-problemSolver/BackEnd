package pnu.problemsolver.myorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.dto.DemandDetailResponseDTO;
import pnu.problemsolver.myorder.dto.DemandListResponseDTO;
import pnu.problemsolver.myorder.dto.DemandListRequestDTO;
import pnu.problemsolver.myorder.dto.DemandSaveDTO;
import pnu.problemsolver.myorder.service.DemandService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
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
	public String saveDemand(@RequestBody DemandSaveDTO d) {
		//사진저장
		byte[] bytes = Base64.getDecoder().decode(d.getFile());
		
		File dir = new File(demandUploadPath + File.separator + d.getCustomerUUID().toString());
		if (!dir.exists()) {//존재하지 않으면 생성.
			dir.mkdirs();
		}
		Path imgPath = Paths.get(dir.toPath() + File.separator + d.getUuid().toString() + "." + d.getExtension());
		try {
			Files.write(imgPath, bytes);
			
		} catch (IOException e) {
			log.error("img write error!");
			e.printStackTrace();
		}
		
		//DB에 저장. uuid를 반환하지만 사실 필요없다.
		 demandService.saveWithFunction(i -> Demand.toEntity(i, imgPath), d);
		return "success";
		
	}
	
	@PostMapping("/{status}")//TODO : memberType이 header로 오는게 아닐텐데..알아보자.
	public List<DemandListResponseDTO> customerDemandList(@PathVariable("status") DemandStatus status, @RequestBody DemandListRequestDTO dto, @RequestHeader MemberType memberType) {//@RequestHeader도 있다!
		List<DemandListResponseDTO> resList = null;
		String sortStr = dto.getSort();
		PageRequest pageRequest = PageRequest.of(dto.getSize(), dto.getPage(), Sort.by(sortStr == null ? "created" : sortStr).descending());//기본값은 최신순!
		
		if (memberType == MemberType.CUSTOMER) {
			resList = demandService.findByCustomer(i -> DemandListResponseDTO.toDTO(i), dto.getUuid(), status, pageRequest);
		} else if (memberType == MemberType.STORE) {
			resList = demandService.findByStore(i -> DemandListResponseDTO.toDTO(i), dto.getUuid(), status, pageRequest);
			
		} else {
			log.error("GUEST can't access to /demand/{status}");
		}
//		demandService
		return resList;
	}
	
	@PostMapping("/detailed")
	public DemandDetailResponseDTO detailed(@RequestBody UUID demandId) {
		return demandService.findById(i -> DemandDetailResponseDTO.toDTO(i), demandId);
	}
	
}
