package com.quickbite.authservice.Dto;

public record SignupRequest (
        String firstname,
        String lastname,
        String password,

        String email,
        int age
) {
}