package com.andband.accounts.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenUtil {

    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
