package co.com.bancolombia.usecase.exception;

public class ExcepcionCorreoExistente extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int codigo;

    public ExcepcionCorreoExistente(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }
}
