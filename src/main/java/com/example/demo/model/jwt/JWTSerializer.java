package com.example.demo.model.jwt;

import com.example.demo.model.user.User;

public interface JWTSerializer {

    String jwtFromUser(User user);

}
