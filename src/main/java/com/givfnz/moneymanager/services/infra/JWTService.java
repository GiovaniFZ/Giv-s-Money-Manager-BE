package com.givfnz.moneymanager.services.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.givfnz.moneymanager.model.entities.UserModel;
import com.givfnz.moneymanager.services.TokenService;

@Service
public class JWTService implements TokenService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public String generate(UserModel user) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        String token = JWT.create()
                .withClaim("sub", user.getId().toString())
                .withIssuer("auth0")
                .sign(algorithm);
        return token;
    }

}
