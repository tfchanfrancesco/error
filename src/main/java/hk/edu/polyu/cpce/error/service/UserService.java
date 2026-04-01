package hk.edu.polyu.cpce.error.service;

import hk.edu.polyu.cpce.error.dto.CreateUserRequest;
import hk.edu.polyu.cpce.error.exception.BusinessViolationException;
import hk.edu.polyu.cpce.error.exception.ExceptionDefinition;
import hk.edu.polyu.cpce.error.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    public Map<String, Object> getUserById(Long id) {
        if (id == 1L) {
            return Map.of("id", 1L, "name", "Tom", "email", "tom@example.com");
        }

        throw new ResourceNotFoundException(ExceptionDefinition.USER_NOT_FOUND, id);
    }

    /**
     * id 為 1 時返回示例数据，否则抛出 {@link ExceptionDefinition#STUDENT_RECORD_NOT_FOUND}。
     */
    public Map<String, Object> getStudentRecordById(Long studentId) {
        if (studentId == 1L) {
            return Map.of("studentId", 1L, "name", "Alice");
        }
        throw new ResourceNotFoundException(ExceptionDefinition.STUDENT_RECORD_NOT_FOUND, studentId);
    }

    /**
     * 用于演示 {@link BusinessViolationException} / {@link ExceptionDefinition#INVALID_BUSINESS_OPERATION}。
     */
    public void triggerBusinessViolation() {
        throw new BusinessViolationException(ExceptionDefinition.INVALID_BUSINESS_OPERATION);
    }

    /** 触发 {@link RuntimeException}，由 {@code @ExceptionHandler(Exception.class)} 兜底。 */
    public void triggerPlainRuntimeException() {
        throw new RuntimeException("Deliberate RuntimeException for testing global Exception handler");
    }

    /** 触发 {@link IllegalStateException}，同样走全局 {@code Exception} 处理。 */
    public void triggerIllegalStateException() {
        throw new IllegalStateException("Deliberate IllegalStateException for testing global Exception handler");
    }

    /** 触发 {@link NullPointerException}（非业务自定义异常）。 */
    public void triggerNullPointerException() {
        throw new NullPointerException("Deliberate NullPointerException for testing global Exception handler");
    }

    public Map<String, Object> createUser(CreateUserRequest request) {
        return Map.of(
                "id", 2L,
                "name", request.getName(),
                "email", request.getEmail()
        );
    }
}
