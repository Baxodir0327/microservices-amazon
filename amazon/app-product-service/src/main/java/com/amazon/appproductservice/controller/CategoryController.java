package com.amazon.appproductservice.controller;

import com.amazon.appproductservice.aop.CheckAuth;
import com.amazon.appproductservice.collection.Category;
import com.amazon.appproductservice.config.AuthClient;
import com.amazon.appproductservice.payload.CategoryDTO;
import com.amazon.appproductservice.payload.UserDTO;
import com.amazon.appproductservice.service.CategoryService;
import com.amazon.appproductservice.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(CategoryController.BASE_PATH)
@RequiredArgsConstructor

public class CategoryController {
    public static final String ID = "/{id}";
    private final CategoryService categoryService;
    private final AuthClient authClient;
    public static final String BASE_PATH = AppConstants.BASE_PATH + "/category";


    @PostMapping
    public Mono<CategoryDTO> add(@RequestBody Category category, @RequestHeader("Authorization") String authorization) {
        if (authorization == null)
            return Mono.error(RuntimeException::new);

        //todo ADD_CATEGORY
        return authClient.getUserMe(authorization)
                .flatMap(userDTO -> {
                    System.out.println(userDTO);
                    if (!userDTO.getPermissions().contains("ADD_CATEGORY"))
                        return Mono.error(RuntimeException::new);
                    return categoryService.create(category);
                });
    }

    //    @CheckAuth(permissions={"ADD_ORDER"})
    @PostMapping("/second")
    public Mono<CategoryDTO> add2(@RequestBody Category category, @RequestHeader("Authorization") String authorization) {
        if (authorization == null)
            return Mono.error(RuntimeException::new);

        System.out.printf("dasdasdasda4545");
        //todo ADD_CATEGORY
        return authClient.getUserMe(authorization)
                .flatMap(userDTO -> {
                    System.out.println(userDTO);
                    if (!userDTO.getPermissions().contains("ADD_CATEGORY"))
                        return Mono.error(RuntimeException::new);
                    return categoryService.create(category);
                });
    }

    //    @CheckAuth
//    @GetMapping
//    public Flux<CategoryDTO> list(@RequestHeader(value = "Authorization", required = false) String authorization) {
//        if (authorization == null) {
//            return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
//        }
//        return authClient.getUserMe(authorization)
//                .flatMapMany(userDTO -> {
//                    if (userDTO != null) {
//                        return categoryService.list();
//                    } else {
//                        return Flux.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
//                    }
//                });
//    }
    @CheckAuth(permissions = "EDIT_CATEGORY")
    @GetMapping
    public Flux<CategoryDTO> list() {
        return categoryService.list();
    }
    @CheckAuth
    @GetMapping("/test")
    public void bla(){
        System.out.println("OK");
    }

    @GetMapping(ID)
    public Mono<CategoryDTO> one(@PathVariable String id) {
        return categoryService.byId(id);
    }
}
