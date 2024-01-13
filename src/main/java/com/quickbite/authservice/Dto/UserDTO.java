package com.quickbite.authservice.Dto;

public record UserDTO(
        Long id,
        String firstname,
        String lastname,
        String email,
        int age,
        String tag
) {
}
