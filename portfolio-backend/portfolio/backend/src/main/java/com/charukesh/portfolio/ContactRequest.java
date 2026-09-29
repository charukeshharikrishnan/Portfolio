package com.charukesh.portfolio;
import jakarta.validation.constraints.*;
public record ContactRequest(
  @NotBlank @Size(max=80) String name,
  @NotBlank @Email @Size(max=120) String email,
  @NotBlank @Size(max=120) String subject,
  @NotBlank @Size(min=10,max=2000) String message) {}
