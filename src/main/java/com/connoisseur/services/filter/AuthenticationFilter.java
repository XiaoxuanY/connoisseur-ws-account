package com.connoisseur.services.filter;

import com.connoisseur.services.manager.UserManager;
import com.connoisseur.services.model.CnsUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//
///**
// * Created by Ray Xiao on 4/21/17.
// */
public class AuthenticationFilter extends GenericFilterBean {

    private final static Logger logger = Logger.getLogger(AuthenticationFilter.class);
    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";
    @Autowired
    private UserManager userManager;
    final private List<String> protectedUrls = new ArrayList<String>();

    {
//        protectedUrls.add("/user");
//        protectedUrls.add("/authTokens");

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        String token = httpRequest.getHeader("X-Auth-Token");

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        if (resourcePath.startsWith("/user/loginsession")) {
            chain.doFilter(request, response);
            return;
        }

        if (protectedUrls.stream().anyMatch(url -> resourcePath.startsWith(url))) {
            logger.debug("access " + resourcePath + " is protected, validate user session");
            try {
                if (token != null) {
                    CnsUser user = userManager.validateToken(token);
                    if (user != null) {
                        logger.debug("Token validated " + user.getUserName());
                        chain.doFilter(request, response);
                        return;
                    } else {
                        logger.info("Token invalid!!!! " + token);
                        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalid or expired");
                    }
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No authentication information in the request");
                }
            } catch (Exception e) {
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unknown authentication error");
            } finally {
                return;
            }
        } else {
            chain.doFilter(request, response);
            return;
        }
    }


    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

}
