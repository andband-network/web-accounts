package com.andband.accounts.client.notification;

import com.andband.accounts.util.RestApiTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private RestApiTemplate notificationApi;

    public NotificationService(@Qualifier("notificationApi") RestApiTemplate notificationApi) {
        this.notificationApi = notificationApi;
    }

    public void resetPassword(String email, String userName, String tokenString) {
        NotificationRequest request = new NotificationRequest();
        request.setEmail(email);
        request.setToProfileName(userName);
        request.setText(tokenString);
        notificationApi.post("/notification/reset-password", request, Void.class);
    }

}
