package com.test.master.config;

import com.test.master.service.impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class CustomTokenGranter extends AbstractTokenGranter {

    private UserServiceImpl userService;
    private AuthenticationManager authenticationManager;

    protected CustomTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    public CustomTokenGranter(AuthenticationManager authenticationManager,AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType, UserServiceImpl userService) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        return super.getOAuth2Authentication(client, tokenRequest);
    }

    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        String tenant = tokenRequest.getRequestParameters().get("tenant");
        String username = tokenRequest.getRequestParameters().get("username");
        String password = tokenRequest.getRequestParameters().get("password");
        String grantType = tokenRequest.getGrantType();
        try {
            if(grantType.equalsIgnoreCase(SecurityConstant.GRANT_TYPE_DRIVER) ||
                grantType.equalsIgnoreCase(SecurityConstant.GRANT_TYPE_CUSTOMER)){
                String userId = tokenRequest.getRequestParameters().get("userId");
                return userService.getAccessTokenForCustom(client, tokenRequest, username, password, tenant, grantType, userId, this.getTokenServices());
            }else{
                return userService.getAccessTokenForMultipleTenancies(client, tokenRequest, username, password, tenant, this.getTokenServices());
            }
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            throw new InvalidTokenException("account or tenant invalid");
        }
    }

}
