package co.com.bancolombia.usecase.user.testdatabuilder;

import co.com.bancolombia.model.user.User;

public class UserUseCaseTestDataBuilder {

    private Long id ;
    private String nombre;
    private String apellido;
    private String fechaNacimiento;
    private String direccion;
    private String telefono;
    private String correoElectronico;
    private Long salarioBase;

    public UserUseCaseTestDataBuilder() {
        this.id = 1L;
        this.nombre = "nombre";
        this.apellido = "apellido";
        this.fechaNacimiento = "fechaNacimiento";
        this.direccion = "direccion";
        this.telefono = "telefono";
        this.correoElectronico = "correoElectronico@example.com";
        this.salarioBase = 2000000L; // Un salario más realista
    }

    public UserUseCaseTestDataBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserUseCaseTestDataBuilder withNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public UserUseCaseTestDataBuilder withApellido(String apellido) {
        this.apellido = apellido;
        return this;
    }

    public UserUseCaseTestDataBuilder withFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
        return this;
    }

    public UserUseCaseTestDataBuilder withDireccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public UserUseCaseTestDataBuilder withTelefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public UserUseCaseTestDataBuilder withCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
        return this;
    }

    public UserUseCaseTestDataBuilder withSalarioBase(Long salarioBase) {
        this.salarioBase = salarioBase;
        return this;
    }

    public User build() {
        return new User(
                this.id,
                this.nombre,
                this.apellido,
                this.fechaNacimiento,
                this.direccion,
                this.telefono,
                this.correoElectronico,
                this.salarioBase
        );
    }
}
