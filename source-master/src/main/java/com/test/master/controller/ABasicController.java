package com.test.master.controller;

import com.test.master.constant.WinWinConstant;
import com.test.master.jwt.WinWinJwt;
import com.test.master.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Objects;

public class ABasicController {
    @Autowired
    private UserServiceImpl userService;

    public long getCurrentUser(){
        WinWinJwt winWinJwt = userService.getAddInfoFromToken();
        return winWinJwt.getAccountId();
    }

    public long getTokenId(){
        WinWinJwt winWinJwt = userService.getAddInfoFromToken();
        return winWinJwt.getTokenId();
    }

    public WinWinJwt getSessionFromToken(){
        return userService.getAddInfoFromToken();
    }

    public boolean isSuperAdmin(){
        WinWinJwt winWinJwt = userService.getAddInfoFromToken();
        if(winWinJwt !=null){
            return winWinJwt.getIsSuperAdmin();
        }
        return false;
    }

    public boolean isShop(){
        WinWinJwt winWinJwt = userService.getAddInfoFromToken();
        if(winWinJwt !=null){
            return Objects.equals(winWinJwt.getUserKind(), WinWinConstant.USER_KIND_MANAGER);
        }
        return false;
    }

    public String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            OAuth2AuthenticationDetails oauthDetails =
                    (OAuth2AuthenticationDetails) authentication.getDetails();
            if (oauthDetails != null) {
                return oauthDetails.getTokenValue();
            }
        }
        return null;
    }
}
