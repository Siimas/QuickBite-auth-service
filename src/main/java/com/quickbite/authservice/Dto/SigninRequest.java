package com.quickbite.authservice.Dto;

public record SigninRequest (
        String email,
        String password
) {
}