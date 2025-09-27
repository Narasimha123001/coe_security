package com.techtricks.coe_auth.dtos;
import com.techtricks.coe_auth.models.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private Role role;
}
