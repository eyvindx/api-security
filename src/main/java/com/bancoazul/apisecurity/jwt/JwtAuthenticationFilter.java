package com.bancoazul.apisecurity.jwt;

import com.bancoazul.apisecurity.rsa.RSAKeyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailServiceImpl;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RSAKeyProperties rsaKeyProp;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, ServletException {
        String token = httpServletRequest.getHeader(jwtTokenUtil.getHeader());
        if(!StringUtils.isEmpty(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token, rsaKeyProp.getPublicKey());

            // If the user information can be correctly extracted from JWT, and the user is not authorized
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
                if(jwtTokenUtil.validateToken(token, userDetails, rsaKeyProp.getPublicKey())) {
                    // Authorize the user using the JWT token
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    // It will be handed over to spring security management, and it will not be intercepted for secondary authorization in subsequent filters
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
