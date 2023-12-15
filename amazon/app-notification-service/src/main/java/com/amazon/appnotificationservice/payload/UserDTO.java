package com.amazon.appnotificationservice.payload;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDTO implements Serializable {

    private UUID id;

    private String username;

    private Set<String> permissions;
}
