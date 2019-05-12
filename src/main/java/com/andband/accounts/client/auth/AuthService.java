package com.andband.accounts.client.auth;

import com.andband.accounts.util.RestApiTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private RestApiTemplate authApi;

    public AuthService(@Qualifier("authApi") RestApiTemplate authApi) {
        this.authApi = authApi;
    }

    public void updatedPassword(String accountId, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("accountId", accountId);
        params.put("password", password);
        authApi.post("/user/password?accountId={accountId}&password={password}", params, Void.class);
    }

}
