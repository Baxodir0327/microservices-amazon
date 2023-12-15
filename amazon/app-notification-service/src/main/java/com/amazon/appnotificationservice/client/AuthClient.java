package com.amazon.appnotificationservice.client;

import com.amazon.appnotificationservice.payload.UserDTO;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface AuthClient {

    @GetExchange("/api/v1/auth/user/me")
    UserDTO getUserMeByToken(@RequestHeader("Authorization") String token);
}
