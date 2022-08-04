package pnu.problemsolver.myorder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pnu.problemsolver.myorder.dto.FcmMessage;
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
		googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
				.createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
		googleCredentials.refreshIfExpired();
		String token = googleCredentials.getAccessToken().getTokenValue();
		int i = token.indexOf("....");
		String substring = token.substring(0, i);
		return substring;
	}
	
	public ResponseEntity<?> sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body, null);
		
		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.add("Content-Type", "application/json; charset=utf-8");
		headers.add("Authorization", "Bearer " + getAccessToken());
		
		HttpEntity<String> entity = new HttpEntity<>(message, headers);
		System.out.println(entity);
		
		ResponseEntity<String> exchange = Request.restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);
		return exchange;
//        OkHttpClient client = new OkHttpClient();
//        RequestBody requestBody = RequestBody.create(message,
//                MediaType.get("application/json; charset=utf-8"));
//        Request request = new Request.Builder()
//                .url(API_URL)
//                .post(requestBody)
//                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
//                .build();

//        Response response = client.newCall(request).execute();
//
//        System.out.println(response.body().string());
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
