package com.amazon.appnotificationservice.aop;

import com.amazon.appnotificationservice.payload.UserDTO;
import com.amazon.appnotificationservice.service.CacheService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@RequiredArgsConstructor
public class CheckUserExecutor {

    private final CacheService cacheService;

    @Before("@annotation(checkUser)")
    public void blaCheck(CheckUser checkUser) {
        HttpServletRequest request = getCurrentRequest();
        String token = request.getHeader("Authorization");

        UserDTO userDTO = cacheService.getUserDTO(token);


        if (checkUser.permissions().length != 0 && hasPermission(checkUser, userDTO))
            throw new RuntimeException("Oka huquqiz yo'q");


        request.setAttribute("currentUser", userDTO);
        System.out.println("HAMMASI YAXSHI");
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null)
            throw new RuntimeException();

        return requestAttributes.getRequest();
    }
    private static boolean hasPermission(CheckUser checkUser, UserDTO userDTO) {
        return Arrays.stream(checkUser.permissions()).noneMatch(p -> userDTO.getPermissions().contains(p));
    }

}
