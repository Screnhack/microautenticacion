package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor // Genera un constructor con todos los argumentos
@NoArgsConstructor
public class UserLoginRequest {
    private String correo;
    private String password;
}
