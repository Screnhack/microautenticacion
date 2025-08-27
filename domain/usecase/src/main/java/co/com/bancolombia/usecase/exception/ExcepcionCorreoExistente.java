package co.com.bancolombia.usecase.exception;

public class ExcepcionCorreoExistente extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExcepcionCorreoExistente(String mensaje) {
        super(mensaje);
    }
}
