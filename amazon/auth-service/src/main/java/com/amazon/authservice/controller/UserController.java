package com.amazon.authservice.controller;

import com.amazon.authservice.payload.UserDTO;
import com.amazon.authservice.security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth/user")
public class UserController {

    @GetMapping("/me")
    public Mono<UserDTO> userMe() {
        System.out.println("userme");
        return ReactiveSecurityContextHolder.getContext()
                .log()
                .map(SecurityContext::getAuthentication)
                .log()
                .filter(authentication -> authentication.getPrincipal() instanceof UserPrincipal)
                .log()
                .map(authentication -> mapUserDTO(((UserPrincipal) authentication.getPrincipal())))
                .log();
    }

    private UserDTO mapUserDTO(UserPrincipal userPrincipal) {
        System.out.println("map: " + userPrincipal);
        return new UserDTO(
                userPrincipal.getUser().getId(),
                userPrincipal.getUsername(),
                userPrincipal.getPermissions()
        );
    }
}
