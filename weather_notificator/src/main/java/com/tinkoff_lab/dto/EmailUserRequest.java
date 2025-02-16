package com.tinkoff_lab.dto;

import jakarta.validation.constraints.Email;

public record EmailUserRequest(@Email(message = "Invalid email") String email, String name, long chatId) {
}
