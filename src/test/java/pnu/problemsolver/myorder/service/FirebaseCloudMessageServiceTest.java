package pnu.problemsolver.myorder.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FirebaseCloudMessageServiceTest {
	
	private FirebaseCloudMessageService firebaseCloudMessageService = new FirebaseCloudMessageService();
	
	@Test
	public void getAccessTokenTest() throws IOException {
		String accessToken = firebaseCloudMessageService.getAccessToken();
		assertEquals(accessToken.length() > 20, true);
		System.out.println(accessToken);
		
	}
	
	@Test
	public void sendMessageToTest() throws IOException {
		String accessToken = firebaseCloudMessageService.getAccessToken();
		//TODO : 여기에 accessToken을 넣는게 아니다!
//		ResponseEntity<?> body = firebaseCloudMessageService.sendMessageTo(accessToken, " title", "body");
//		System.out.println(body);
	
		
	}
}