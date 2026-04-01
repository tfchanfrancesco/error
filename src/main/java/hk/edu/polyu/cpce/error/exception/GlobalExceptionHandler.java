package hk.edu.polyu.cpce.error.exception;

import hk.edu.polyu.cpce.error.util.EspStringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String ERROR_LOG_PATTERN = "[API ERROR] httpStatus={} requestLine={} exceptionClass={} message={}";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest webRequest) {
        String requestLine = webRequest instanceof ServletWebRequest servletWebRequest ? formatRequestLine(servletWebRequest.getRequest()) : webRequest.getDescription(false);
        log.warn(ERROR_LOG_PATTERN, statusCode.value(), requestLine, ex.getClass().getName(), ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, webRequest);
    }

    @ExceptionHandler(BaseBusinessException.class)
    public ProblemDetail handleBusinessException(BaseBusinessException exception, HttpServletRequest httpRequest) {
        int httpStatus = exception.getExceptionDefinition().getHttpStatus().value();
        String requestLine = formatRequestLine(httpRequest);
        log.warn(ERROR_LOG_PATTERN, httpStatus, requestLine, exception.getClass().getName(), exception.getMessage(), exception);
        return createProblemDetail(
                exception.getExceptionDefinition().getHttpStatus(),
                exception.getMessage(),
                httpRequest
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleException(Exception exception, HttpServletRequest httpRequest) {
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String requestLine = formatRequestLine(httpRequest);
        log.error(ERROR_LOG_PATTERN, httpStatus, requestLine, exception.getClass().getName(), exception.getMessage(), exception);
        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error, please try again",
                httpRequest);
    }

    private ProblemDetail createProblemDetail(HttpStatus httpStatus, String detail, HttpServletRequest httpRequest) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, detail);
        problemDetail.setTitle(httpStatus.getReasonPhrase());
        problemDetail.setInstance(URI.create(httpRequest.getRequestURI()));
        return problemDetail;
    }

    private String formatRequestLine(HttpServletRequest httpRequest) {
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String query = httpRequest.getQueryString();
        if (EspStringUtils.isNotBlank(query)) {
            return method + " " + uri + "?" + query;
        }
        return method + " " + uri;
    }

}