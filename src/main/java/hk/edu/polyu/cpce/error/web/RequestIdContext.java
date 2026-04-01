package hk.edu.polyu.cpce.error.web;

import hk.edu.polyu.cpce.error.util.EspStringUtils;

import java.util.UUID;

public final class RequestIdContext {

    public static final String MDC_REQUEST_ID_KEY = "requestId";

    public static final String HEADER = "X-Request-Id";

    private RequestIdContext() {
    }

    public static String parseHeader(String value) {
        if (EspStringUtils.isBlank(value)) {
            return UUID.randomUUID().toString();
        }
        try {
            return UUID.fromString(value.trim()).toString();
        } catch (IllegalArgumentException ignored) {
            return UUID.randomUUID().toString();
        }
    }

}