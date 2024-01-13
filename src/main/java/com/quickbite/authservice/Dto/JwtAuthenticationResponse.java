package com.quickbite.authservice.Dto;

public record JwtAuthenticationResponse (
        String token,
        String refreshToken
) {
}
