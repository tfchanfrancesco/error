package hk.edu.polyu.cpce.error.exception;

public class BusinessViolationException extends BaseBusinessException {

    public BusinessViolationException(ExceptionDefinition exceptionDefinition, Object... args) {
        super(exceptionDefinition, args);
    }

    public BusinessViolationException(ExceptionDefinition exceptionDefinition, String messageTemplate, Object... args) {
        super(exceptionDefinition, messageTemplate, args);
    }

}