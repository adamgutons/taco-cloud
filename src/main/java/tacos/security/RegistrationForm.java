package tacos.security;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import tacos.User;

@Data
public class RegistrationForm {

    @NotNull
    @Size(min = 5, message = "Username must be at least 5 characters long")
    private String username;
    @NotNull
    @Size(min = 5, message = "Password must be at least 5 characters long")
    private String password;
    private String fullName;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String phone;

    public User toUser(final PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password),
                fullName, street, city, state, zip, phone);
    }

}
