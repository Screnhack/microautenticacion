package co.com.bancolombia.usecase.exception;

import lombok.Getter;

@Getter
public class ExcepcionArgumentos extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final int codigo;

    public ExcepcionArgumentos(int codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo ;
    }
}
