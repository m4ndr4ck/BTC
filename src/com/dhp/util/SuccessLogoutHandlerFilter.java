package com.dhp.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.util.WebUtils;


public class SuccessLogoutHandlerFilter implements LogoutSuccessHandler {
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void logout(HttpServletRequest request,
                       HttpServletResponse response, Authentication authentication) {

    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        String refererUrl = request.getHeader("Referer");
        String redirect = null;

        if(refererUrl.contains("afiliado"))
            redirect = "/afiliado/login";
        else
            redirect = "/login";

        redirectStrategy.sendRedirect(request, response, redirect);
    }
}