package com.corpseed.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.regex.Pattern;

@Component
public class TrailingSlashRedirectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String fullURL = ServletUriComponentsBuilder
                .fromRequest(request)
                .build()
                .toString();
        String URI = request.getRequestURI();
//        System.out.println(URI);
        Principal principal = request.getUserPrincipal();

        if (principal == null && !URI.contains("/assets/") && !URI.contains("/css/")
                && !URI.contains("/images/") && !URI.contains("/js/")
                && !URI.contains("/chat_boat/") && !URI.contains("rssFeed")) {
            if (URI.equals("")) {
                response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
                response.setHeader(HttpHeaders.LOCATION, fullURL + "/");
            }else if(URI.endsWith("/")&&URI.length()>1){
//                System.out.println(URI+"\n"+fullURL.substring(0,fullURL.length()-1));
                response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
                response.setHeader(HttpHeaders.LOCATION, fullURL.substring(0,fullURL.length()-1));
            } else if (Pattern.matches("(.*[A-Z].*)", URI)) {
                response.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
                response.setHeader(HttpHeaders.LOCATION, fullURL.toLowerCase());
            }else {
                filterChain.doFilter(request, response);
            }
        } else filterChain.doFilter(request, response);
    }

}
