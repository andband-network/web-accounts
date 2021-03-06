package com.andband.accounts;

import com.andband.accounts.util.CustomClientCredentialsAccessTokenProvider;
import com.andband.accounts.util.RestApiTemplate;
import com.andband.accounts.web.AccountMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableCircuitBreaker
@EnableEurekaClient
@EnableOAuth2Client
@SpringBootApplication
public class AccountsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountsApplication.class, args);
    }

    @Bean
    public AccountMapper accountMapper() {
        return Mappers.getMapper(AccountMapper.class);
    }

    @LoadBalanced
    @Bean("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Primary
    public RemoteTokenServices remoteTokenServices(@Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate,
                                                   @Value("${andband.auth.oauth.check-token-uri}") String checkTokenEndpoint,
                                                   @Value("${andband.auth.client.internal-api.client-id}") String clientId,
                                                   @Value("${andband.auth.client.internal-api.client-secret}") String clientSecret) {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(checkTokenEndpoint);
        tokenServices.setClientId(clientId);
        tokenServices.setClientSecret(clientSecret);
        tokenServices.setRestTemplate(restTemplate);
        return tokenServices;
    }

    @Bean
    protected OAuth2ProtectedResourceDetails resource(@Value("${andband.auth.oauth.access-token-uri}") String accessTokenUri,
                                                      @Value("${andband.auth.client.internal-api.client-id}") String clientID,
                                                      @Value("${andband.auth.client.internal-api.client-secret}") String clientSecret) {
        ClientCredentialsResourceDetails resource = new ClientCredentialsResourceDetails();
        resource.setAccessTokenUri(accessTokenUri);
        resource.setClientId(clientID);
        resource.setClientSecret(clientSecret);
        return resource;
    }

    @LoadBalanced
    @Bean("oAuth2RestTemplate")
    public OAuth2RestTemplate oAuth2RestTemplate(OAuth2ProtectedResourceDetails resourceDetails,
                                                 @Qualifier("loadBalancedRestTemplate") RestTemplate restTemplate) {
        OAuth2ClientContext context = new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
        OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails, context);

        AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(
                Collections.singletonList(
                        new CustomClientCredentialsAccessTokenProvider(restTemplate)
                )
        );
        oAuth2RestTemplate.setAccessTokenProvider(accessTokenProvider);

        return oAuth2RestTemplate;
    }

    @Bean("notificationApi")
    public RestApiTemplate notificationRestTemplate(@Qualifier("oAuth2RestTemplate") RestTemplate restTemplate,
                                                    @Value("${andband.notification-service.endpoint}") String notificationEndpoint) {
        return createRestApiTemplate(restTemplate, notificationEndpoint);
    }


    @Bean("authApi")
    public RestApiTemplate authRestTemplate(@Qualifier("oAuth2RestTemplate") RestTemplate restTemplate,
                                            @Value("${andband.auth-api.endpoint}") String authApiEndpoint) {
        return createRestApiTemplate(restTemplate, authApiEndpoint);
    }

    private RestApiTemplate createRestApiTemplate(RestTemplate restTemplate, String apiEndpoint) {
        RestApiTemplate restApiTemplate = new RestApiTemplate(restTemplate, apiEndpoint);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restApiTemplate.setHttpHeaders(headers);
        return restApiTemplate;
    }


}
