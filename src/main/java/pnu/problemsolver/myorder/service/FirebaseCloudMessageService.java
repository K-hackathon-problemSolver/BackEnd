package pnu.problemsolver.myorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.util.Mapper;
import pnu.problemsolver.myorder.util.Request;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirebaseCloudMessageService {
	private final String API_URL = "https://fcm.googleapis.com/v1/projects/myorder-6e133/messages:send";
	
	
	public String getAccessToken() throws IOException {
		String firebaseConfigPath = "firebase/firebase_service_key.json";
		GoogleCredentials googleCredentials;
		googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())//ClassPathResource라는 객체가 있었다.
				.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));//GoogleGredentials : OAuth를 통해서 인증한 대상을 나타내는 객체
		googleCredentials.refreshIfExpired();
		String token = googleCredentials.getAccessToken().getTokenValue();
		int i = token.indexOf("....");
		String substring = token.substring(0, i);
		return substring;
	}
	
	public ResponseEntity<?> sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body, null);//img에 null을 넣었다.
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Authorization", "Bearer " + getAccessToken());
		
		HttpEntity<String> entity = new HttpEntity<>(message, headers);
		System.out.println(entity);
		
		ResponseEntity<String> exchange = Request.restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
		return exchange;
//
    }
	
	private String makeMessage(String targetToken, String title, String body, String img) throws JsonProcessingException {//img에 넣을 것은 없기에 보통 null을 넣으면 된다.
		FcmMessage fcmMessage = FcmMessage.builder()
				.message(FcmMessage.Message.builder()
						.token(targetToken)
						.notification(FcmMessage.Notification.builder()
								.title(title)
								.body(body)
								.image(img)
								.build()
						).build())
				.validateOnly(false)//false
				.build();
		return Mapper.objectMapper.writeValueAsString(fcmMessage);
	}
}
@Builder
@Data
class FcmMessage {
	private boolean validateOnly;
    private Message message;

    @Builder
    @AllArgsConstructor
    @Data
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Data
    
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}

