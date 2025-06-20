package com.nbu.graduation.spring.model.dto;

import com.nbu.graduation.spring.model.enums.Department;
import com.nbu.graduation.spring.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {
    @NotBlank(message = "Потребителското име е задължително.")
    @Size(min = 5, max = 20, message = "Потребителското име трябва да е между 5 и 20 символа.")
    private String username;

    @NotBlank(message = "Паролата е задължителна.")
    @Size(min = 6, max = 20, message = "Паролата трябва да е между 6 и 20 символа.")
    private String password;

    @NotBlank(message = "Потвърждаването на паролата е задължително.")
    @Size(min = 6, max = 20, message = "Потвърдете паролата с между 6 и 20 символа.")
    private String confirmPassword;

    @NotBlank(message = "Името е задължително.")
    @Size(min = 3, max = 20, message = "Името трябва да е между 3 и 20 символа.")
    private String firstName;

    @NotBlank(message = "Фамилията е задължителна.")
    @Size(min = 3, max = 20, message = "Фамилията трябва да е между 3 и 20 символа.")
    private String lastName;

    @NotBlank(message = "Имейлът е задължителен.")
    @Email(message = "Моля, въведете валиден имейл адрес.")
    @Size(min = 8, max = 50, message = "Имейлът трябва да е между 8 и 50 символа.")
    private String email;
    private Role role;
    private Department department;

    public UserDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
}
