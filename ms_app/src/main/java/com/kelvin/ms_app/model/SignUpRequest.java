package com.kelvin.ms_app.model;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class SignUpRequest implements Cloneable{

    @NotEmpty(message = "{FIELD_NAME} is required.")
    private String username;

    @NotEmpty(message = "{FIELD_NAME} is required.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    @NotEmpty(message = "{FIELD_NAME} is required.")
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-zA-Z]).{10,}$", message="Password must contain at least one uppercase, one lower case, one number and be at least 10 characters long")
    private String password;

    @NotEmpty(message = "{FIELD_NAME} is required.")
    private String firstName;

    @NotEmpty(message = "{FIELD_NAME} is required.")
    private String lastName;

    @NotEmpty(message = "{FIELD_NAME} is required.")
    private String phoneNumber;
}
