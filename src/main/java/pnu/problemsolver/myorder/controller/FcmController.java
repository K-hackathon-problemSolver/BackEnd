package pnu.problemsolver.myorder.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pnu.problemsolver.myorder.domain.constant.MemberType;
import pnu.problemsolver.myorder.dto.FcmTokenRequest;
import pnu.problemsolver.myorder.service.CustomerService;
import pnu.problemsolver.myorder.service.StoreService;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@Slf4j
public class FcmController {
	
	@Autowired
	StoreService storeService;
	@Autowired
	CustomerService customerService;
	
	
	@PostMapping("/fcm-token")
	public String fcmToken(@RequestBody FcmTokenRequest fcmTokenRequest, HttpServletRequest request) {
		MemberType memberType = (MemberType) request.getAttribute("memberType");
		UUID id = fcmTokenRequest.getUuid();
		String fcmToken = fcmTokenRequest.getToken();
		if (memberType == MemberType.CUSTOMER) {
			//찾고 fcm만 넣어서 update
			customerService.setFcmToken(id, fcmToken);
			
		}
		else if (memberType == MemberType.STORE) {
			storeService.setFcmToken(id, fcmToken);
			
		}
		else{
			log.error("memberType error!!");
		}
		
		return "success";
	}
}
