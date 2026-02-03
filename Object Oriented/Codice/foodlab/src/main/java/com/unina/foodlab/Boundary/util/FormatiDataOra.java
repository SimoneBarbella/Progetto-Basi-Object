package com.unina.foodlab.Boundary.util;

import java.time.format.DateTimeFormatter;

public final class FormatiDataOra {

    private FormatiDataOra() {
    }

    public static final DateTimeFormatter YYYY_MM_DD_HH_MM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");
    public static final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");
}
