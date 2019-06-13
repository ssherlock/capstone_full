package com.stackroute.authenticationservice.service;

import com.stackroute.authenticationservice.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class SecurityTokenGeneratorImpl implements ISecurityTokenGenerator {
    @Value("${auth.shared.key}")
    private String SHARED_KEY;

    @Override
    public Map<String, String> generateToken(final User user) {
        LocalDate localDate = LocalDate.now();

        //Asked not to include an expiration date (kept here for possible future use) - Added back in at request from Front end team
        //Gets todays date plus 5 days (to be used as expiration date)
//        Date datePlus5Days = java.sql.Date.valueOf(localDate.plusDays(5));
//        final String jwtToken = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(java.sql.Date.valueOf(localDate))
//            .signWith(SignatureAlgorithm.HS512,SHARED_KEY).setExpiration(datePlus5Days).compact();
        final String jwtToken = Jwts.builder().setSubject(user.getUserName()).setIssuedAt(java.sql.Date.valueOf(localDate))
                .signWith(SignatureAlgorithm.HS512,SHARED_KEY).compact();
        Map<String,String> map = new HashMap<>();
        map.put("token",jwtToken);
        map.put("message", "User Successfully logged in");

        return map;
    }
}

