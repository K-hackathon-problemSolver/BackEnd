package pnu.problemsolver.myorder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pnu.problemsolver.myorder.domain.Demand;
import pnu.problemsolver.myorder.domain.constant.DemandStatus;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.dto.DemandDTO;
import pnu.problemsolver.myorder.dto.DemandListDTO;
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
		
		//DB에 저장
		demandService.save(i -> Demand.toEntity(i, imgPath), d);
		return "success";
		
	}
	
	@PostMapping("/{status}")
	public List<DemandListDTO> customerDemandList(@PathVariable("status") DemandStatus status, @RequestBody UUID id, @RequestHeader MemberType memberType) {//@RequestHeader도 있다!
		List<DemandListDTO> resList = null;
		if (memberType == MemberType.CUSTOMER) {
			resList = demandService.findByCustomer(i -> DemandListDTO.toDTO(i), id, status);//TODO : 한번에 넘겨줄지 아닐지..지금 이 함수는 한번에 다 가져오는 것임.
		} else if (memberType == MemberType.STORE) {
			resList = demandService.findByStore(i -> DemandListDTO.toDTO(i), id, status);
			
		} else {
			log.error("GUEST can't access to /demand/{status}");
		}
//		demandService
		return resList;
	}
	
//	@PostMapping("/detailed")
//	public byte[] detailed() {
//
//	}
}
