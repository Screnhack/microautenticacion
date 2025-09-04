package co.com.bancolombia.model.autenticacion;
import lombok.*;
import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Autenticacion {
    private String correo;
    private String password;
}
