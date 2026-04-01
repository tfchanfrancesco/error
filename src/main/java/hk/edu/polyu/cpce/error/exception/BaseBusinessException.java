package hk.edu.polyu.cpce.error.exception;

import hk.edu.polyu.cpce.error.util.EspStringUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Getter
public class BaseBusinessException extends RuntimeException {

    private final ExceptionDefinition exceptionDefinition;

    public BaseBusinessException(@NonNull ExceptionDefinition exceptionDefinition, @Nullable Object... args) {
        super(EspStringUtils.formatString(exceptionDefinition.getMessageTemplate(), args));
        this.exceptionDefinition = exceptionDefinition;
    }

    public BaseBusinessException(@NonNull ExceptionDefinition exceptionDefinition, @NonNull String messageTemplate, @Nullable Object... args) {
        super(EspStringUtils.formatString(messageTemplate, args));
        this.exceptionDefinition = exceptionDefinition;
    }

    public BaseBusinessException(@NonNull ExceptionDefinition exceptionDefinition, @NonNull String messageTemplate, @NonNull Throwable cause, @Nullable Object... args) {
        super(EspStringUtils.formatString(messageTemplate, args), cause);
        this.exceptionDefinition = exceptionDefinition;
    }

}