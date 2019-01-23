package com.dhp.util;

import br.com.uol.pagseguro.api.utils.logging.LoggerFactory;
import com.dhp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final DefaultRedirectStrategy strategy = new DefaultRedirectStrategy();
    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
                                        final AuthenticationException exception) throws IOException, ServletException {

        String refererUrl = request.getHeader("Referer");
        String redirect = null;

        if(refererUrl.contains("afiliado"))
            redirect = "/afiliado/login?error=error";
        else
            redirect = "/login?error=error";
        strategy.sendRedirect(request, response, redirect);
    }

}
