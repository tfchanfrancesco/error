package hk.edu.polyu.cpce.error.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 用于触发 {@code MethodArgumentNotValidException}（交由 {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler} 处理）。
 */
public class ValidationDemoRequest {

    @NotBlank(message = "title 不能為空")
    @Size(min = 3, max = 20, message = "title 長度須在 3～20 之間")
    private String title;

    @NotBlank(message = "contactEmail 不能為空")
    @Email(message = "contactEmail 格式不正確")
    private String contactEmail;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
