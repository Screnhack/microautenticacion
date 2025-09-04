package co.com.bancolombia.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest implements Serializable {
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private Long salarioBase;
    private String password;
    private String nombreRol;
}
