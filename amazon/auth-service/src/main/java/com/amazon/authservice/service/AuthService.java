package com.amazon.authservice.service;

import com.amazon.authservice.entity.RolePermission;
import com.amazon.authservice.entity.User;
import com.amazon.authservice.payload.ApiResult;
import com.amazon.authservice.payload.LoginDTO;
import com.amazon.authservice.repository.RolePermissionRepository;
import com.amazon.authservice.repository.UserRepository;
import com.amazon.authservice.security.JWTProvider;
import com.amazon.authservice.security.UserPrincipal;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService implements ReactiveUserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;
    private final RolePermissionRepository rolePermissionRepository;

    public AuthService(@Lazy PasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       @Lazy JWTProvider jwtProvider, RolePermissionRepository rolePermissionRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
                .map(user -> new UserPrincipal(user,
                        rolePermissionRepository.findAllByRoleId(user.getRoleId())
                                .toStream().map(RolePermission::getPermission).collect(Collectors.toSet())));
    }

    public Mono<UserPrincipal> findById(String userId) {
        System.out.println(userId);
        return userRepository.findById(UUID.fromString(userId))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))))
                .flatMap(user ->
                        rolePermissionRepository.findAllByRoleId(user.getRoleId())
                                .map(RolePermission::getPermission)
                                .collect(Collectors.toSet())
                                .map(permissions -> new UserPrincipal(user, permissions))
                );
    }

    public Mono<ApiResult> login(LoginDTO loginDTO) {
        return login(loginDTO.username(), loginDTO.password());
    }

    public Mono<ApiResult> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> new ApiResult(true, jwtProvider.createAccessToken(user.getId().toString())));
    }

    public Mono<String> register(LoginDTO loginDTO) {
       return userRepository.findByUsername(loginDTO.username())
                .flatMap(user -> Mono.error(new RuntimeException("User already exist")))
                .switchIfEmpty(Mono.defer(() -> userRepository.save(new User(
                                loginDTO.username(),
                                passwordEncoder.encode(loginDTO.password()),
                                2
                        ))))
                .cast(User.class)
                .map(user -> jwtProvider.createAccessToken(user.getId().toString()));
    }
}