package com.connoisseur.services.filter;

import com.connoisseur.services.manager.UserManager;
import com.connoisseur.services.model.CnsUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerMapping;
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
import java.util.Map;


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
    final private List<String> protectedUrls = new ArrayList<>();

    {
        protectedUrls.add("/");
        protectedUrls.add("/register");
        protectedUrls.add("/loginsession");
        protectedUrls.add("/validsession");
        protectedUrls.add("/user");
        protectedUrls.add("/rating");
        protectedUrls.add("/rating-restaurant");
        protectedUrls.add("/bookmark");
        protectedUrls.add("/bookmark-restaurant");
        protectedUrls.add("/comment");
        protectedUrls.add("/comment-restaurant");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        String token = httpRequest.getHeader("X-Auth-Token");

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        System.out.println("URL: " + httpRequest.getRequestURL());
        System.out.println("TOKEN: " + token);
        System.out.println("QUERY: " + httpRequest.getQueryString());
        System.out.println("UID: " + request.getParameter("uid"));
        System.out.println("PathVariables: " + pathVariables);

        // main page does not require token
        if (resourcePath.equals("/")) {
            chain.doFilter(request, response);
            return;
        }

        // user who did not register doesn't have token yet
        if (resourcePath.equals("/register")) {
            chain.doFilter(request, response);
            return;
        }

        // user who did not login doesn't have token yet
        if (resourcePath.equals("/loginsession")) {
            chain.doFilter(request, response);
            return;
        }

        if (protectedUrls.stream().anyMatch(url -> resourcePath.startsWith(url))) {

            logger.debug("access " + resourcePath + " is protected, validate user session");
            System.out.println("Resource Path: " + resourcePath);

            try {
                if (token != null) {
                    CnsUser user = userManager.validateToken(token);
                    if (user != null) {
                        logger.debug("Token validated " + user.getUserName());
                        chain.doFilter(request, response);
                        return;
                    }
//                    long paramUserId = Long.parseLong(request.getParameter("uid"));
//                    if (resourcePath.startsWith("/validsession")) {
//                        logger.debug("Token validated " + user.getUserName());
//                        chain.doFilter(request, response);
//                        return;
//                    }
//                    else if (paramUserId == user.getId() || user.getId() == 0) {
//                        // extract id from request parameter
//                        System.out.println(paramUserId);
//                        // either token's user id and user id parameter are equal or it is admin's token
//                        logger.debug("Token validated " + user.getUserName());
//                        chain.doFilter(request, response);
//                        return;
//                    }
                    else {
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
