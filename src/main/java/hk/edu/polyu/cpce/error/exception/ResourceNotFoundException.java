package hk.edu.polyu.cpce.error.exception;

public class ResourceNotFoundException extends BaseBusinessException {

    public ResourceNotFoundException(ExceptionDefinition exceptionDefinition, Object... args) {
        super(exceptionDefinition, args);
    }

    public ResourceNotFoundException(ExceptionDefinition exceptionDefinition, String messageTemplate, Object... args) {
        super(exceptionDefinition, messageTemplate, args);
    }

}