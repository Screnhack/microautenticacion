package co.com.bancolombia.usecase.validator;

import co.com.bancolombia.usecase.exception.ExcepcionArgumentos;

import java.util.regex.Pattern;

public class argumentValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private static final int SOLICITUD_INCORRECTA = 400;


    private argumentValidator() {
    }

    public static void validarRequeridos(Object valor, String mensaje) {
        if (valor == null) {
            throw new ExcepcionArgumentos(SOLICITUD_INCORRECTA, mensaje);
        }
        if (valor instanceof String && ((String) valor).isEmpty()) {
            throw new ExcepcionArgumentos(SOLICITUD_INCORRECTA, mensaje);
        }
    }

    public static <T extends Number & Comparable<T>> void validarRango(T valor, T min, T max, String mensaje) {
        if (valor == null || valor.compareTo(min) < 0 || valor.compareTo(max) > 0) {
            throw new ExcepcionArgumentos(SOLICITUD_INCORRECTA, mensaje);
        }
    }

    public static void validarFormatoEmail(String email, String mensaje) {
        if (email == null || !pattern.matcher(email).matches()) {
            throw new ExcepcionArgumentos(SOLICITUD_INCORRECTA, mensaje);
        }
    }

}
