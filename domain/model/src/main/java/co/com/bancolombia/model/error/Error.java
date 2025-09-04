package co.com.bancolombia.model.error;

import lombok.*;


@Getter
@Builder
public class Error {
    private int codigo;
    private String mensaje;
}
