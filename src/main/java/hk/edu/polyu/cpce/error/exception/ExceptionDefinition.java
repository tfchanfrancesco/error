package hk.edu.polyu.cpce.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionDefinition {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found, id: {0}."),
    STUDENT_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "Student record not found, id: {0}."),
    INVALID_BUSINESS_OPERATION(HttpStatus.UNPROCESSABLE_CONTENT, "Invalid business operation.");

    private final HttpStatus httpStatus;

    private final String messageTemplate;

    ExceptionDefinition(HttpStatus httpStatus, String messageTemplate) {
        this.httpStatus = httpStatus;
        this.messageTemplate = messageTemplate;
    }

}