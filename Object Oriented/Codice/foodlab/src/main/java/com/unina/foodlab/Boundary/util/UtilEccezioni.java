package com.unina.foodlab.Boundary.util;

public final class UtilEccezioni {

    private UtilEccezioni() {
    }

    public static Throwable rootCause(Throwable throwable) {
        Throwable cause = throwable;
        while (cause != null && cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }

    public static String rootCauseMessageOrNull(Throwable throwable) {
        Throwable root = rootCause(throwable);
        return root != null ? root.getMessage() : null;
    }

    public static String messageOrFallback(Throwable throwable, String fallback) {
        Throwable root = rootCause(throwable);
        if (root != null && root.getMessage() != null && !root.getMessage().isBlank()) {
            return root.getMessage();
        }
        if (throwable != null && throwable.getMessage() != null && !throwable.getMessage().isBlank()) {
            return throwable.getMessage();
        }
        return fallback;
    }
}
