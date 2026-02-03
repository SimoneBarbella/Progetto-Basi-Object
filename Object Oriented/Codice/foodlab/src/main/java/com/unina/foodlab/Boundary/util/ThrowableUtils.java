package com.unina.foodlab.Boundary.util;

@Deprecated
public final class ThrowableUtils {

    private ThrowableUtils() {
    }

    @Deprecated
    public static Throwable rootCause(Throwable throwable) {
        return UtilEccezioni.rootCause(throwable);
    }

    @Deprecated
    public static String rootCauseMessageOrNull(Throwable throwable) {
        return UtilEccezioni.rootCauseMessageOrNull(throwable);
    }

    @Deprecated
    public static String messageOrFallback(Throwable throwable, String fallback) {
        return UtilEccezioni.messageOrFallback(throwable, fallback);
    }
}
