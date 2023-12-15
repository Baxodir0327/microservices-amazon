package com.amazon.appnotificationservice.controller;

import com.amazon.appnotificationservice.aop.CheckUser;
import com.amazon.appnotificationservice.aop.CheckUserExecutor;
import com.amazon.appnotificationservice.payload.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class MaxfiyController {

    @CheckUser
    @GetMapping("/get-secure-data")
    public String yopiqYul() {
        UserDTO currentUser = (UserDTO) CheckUserExecutor.getCurrentRequest().getAttribute("currentUser");

        return "MAxfiy ma'lumot tanish odam: "+currentUser.getId();
    }

    @CheckUser(permissions = {"ADD_CATEGORY","EDIT_CATEGORY"})
    @GetMapping("/get-secure-data-permission")
    public String yopiqYulPermission() {
        UserDTO currentUser = (UserDTO) CheckUserExecutor.getCurrentRequest().getAttribute("currentUser");

        return "MAxfiy ma'lumot huquqi bor: "+currentUser.getUsername();
    }
}
