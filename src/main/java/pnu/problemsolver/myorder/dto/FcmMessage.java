package pnu.problemsolver.myorder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FcmMessage {
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
