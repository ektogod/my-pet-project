package com.tinkoff_lab.dto;

import jakarta.validation.constraints.Email;

public record EmailUserDTO(@Email(message = "Invalid email") String email, long chatId) {
}
