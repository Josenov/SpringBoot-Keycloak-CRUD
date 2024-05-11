package com.api.rest.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value
@RequiredArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String email;
    private String firstName;
    private String password;
    private Set<String> roles;


}