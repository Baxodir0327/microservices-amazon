package com.amazon.authservice.controller;

import com.amazon.authservice.security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth/test")
public class TestController {

    @PreAuthorize("hasAnyAuthority('LIST_ORDER')")
    @GetMapping("/admin")
    public Mono<String> admin(){
        ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(authentication -> authentication.getPrincipal() instanceof UserPrincipal)
                .map(authentication -> (UserPrincipal) authentication.getPrincipal())
                .subscribe(System.out::println);
        return Mono.just("ADMINDAN hello");
    }

    @PreAuthorize("hasAnyAuthority('CANCEL_MY_ORDER')")
    @GetMapping("/user")
    public Mono<String> user(){
        return Mono.just("USERDAN hello");
    }
}
