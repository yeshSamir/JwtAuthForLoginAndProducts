package com.jwtauth.filter;

import com.jwtauth.entity.Users;
import com.jwtauth.util.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!hasAuthorizationHeader(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = getAccessToken(request);
        System.out.println("Access Token :" + accessToken);

        if (!jwtTokenUtils.validateJsonToke(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        SetAuthenticationContex(accessToken, request);
        filterChain.doFilter(request, response);
    }

    private void SetAuthenticationContex(String accessToken, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(accessToken);
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(userDetails,
                null,null);
        authenticationToken.setDetails(new
                WebAuthenticationDetailsSource().buildDetails(request));
    }

    private UserDetails getUserDetails(String accessToken) {

        Users user = new Users();
        String[] subjetarray = jwtTokenUtils.getSubjet(accessToken).split(",");
        user.setEmail(subjetarray[0]);
        user.setId(Integer.valueOf(subjetarray[1]));
        return user;
    }


    private boolean hasAuthorizationHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println("header " + header);
        if (ObjectUtils.isEmpty(header) || header.startsWith("Bearer")) {
            return false;
        }
        return true;
    }

    private String getAccessToken(HttpServletRequest servletRequest) {
        String header = servletRequest.getHeader("Authorization");
        String jwtToken = header.split(" ")[1].trim();
        return jwtToken;
    }
}
