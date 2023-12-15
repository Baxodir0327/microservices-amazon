package com.amazon.appnotificationservice.service;

import com.amazon.appnotificationservice.client.AuthClient;
import com.amazon.appnotificationservice.payload.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final AuthClient authClient;


    @Cacheable(value = "users",key = "#token")
    public UserDTO getUserDTO(String token){
        System.out.println("Borib keldi");
        return authClient.getUserMeByToken(token);
    }
}
