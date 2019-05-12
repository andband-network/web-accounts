package com.andband.accounts.service;

import com.andband.accounts.persistence.token.PasswordResetToken;
import com.andband.accounts.persistence.token.PasswordResetTokenRepository;
import com.andband.accounts.util.TokenUtil;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private TokenUtil tokenUtil;
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public TokenService(TokenUtil tokenUtil, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.tokenUtil = tokenUtil;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public String generatePasswordResetToken(String accountId) {
        String tokenString = tokenUtil.generateToken();
        PasswordResetToken token = new PasswordResetToken();
        token.setAccountId(accountId);
        token.setTokenString(tokenString);
        passwordResetTokenRepository.save(token);
        return tokenString;
    }

    public boolean isPasswordResetTokenValid(String tokenString) {
        PasswordResetToken token = passwordResetTokenRepository.findByTokenString(tokenString);
        return isTokenValid(token);
    }

    public boolean isTokenValid(PasswordResetToken token) {
        return token != null && !tokenIsExpired(token);
    }

    private boolean tokenIsExpired(PasswordResetToken token) {
        return token.getExpiryDate().getTime() < System.currentTimeMillis();
    }

    public PasswordResetToken getPasswordResetToken(String tokenString) {
        return passwordResetTokenRepository.findByTokenString(tokenString);
    }

}
