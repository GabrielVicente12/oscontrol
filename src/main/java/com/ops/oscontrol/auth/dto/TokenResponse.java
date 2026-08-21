package com.ops.oscontrol.auth.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
}
