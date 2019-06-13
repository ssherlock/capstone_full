package com.stackroute.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends GenericFilterBean {

    private String SHARED_KEY;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //Added because @Value doesn't work for getting application.yml variables at this stage.
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(JWTFilter.class);
        ConfigurableEnvironment env = context.getEnvironment();
        this.SHARED_KEY = env.getRequiredProperty("auth.shared.key");

        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;
        final String authHeader = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        } else {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header");
            }

            final String jwtToken = authHeader.substring(7);

            final Claims claims = Jwts.parser().setSigningKey(this.SHARED_KEY).parseClaimsJws(jwtToken).getBody();
            final String subjectFromBody = Jwts.parser().setSigningKey(this.SHARED_KEY).parseClaimsJws(jwtToken).getBody().getSubject();
//            final Date expDate = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(jwtToken).getBody().getExpiration();

            //Extra checks
            claims.setSubject(subjectFromBody);
//            claims.setExpiration(expDate);

            request.setAttribute("claims", claims);
            filterChain.doFilter(request, response);
        }
    }
}
