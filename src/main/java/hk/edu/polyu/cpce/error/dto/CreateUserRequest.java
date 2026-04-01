package hk.edu.polyu.cpce.error.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {

    @NotBlank(message = "name 不能為空")
    @Size(max = 30, message = "name 長度不能超過 30")
    private String name;

    @NotBlank(message = "email 不能為空")
    @Email(message = "email 格式不正確")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
