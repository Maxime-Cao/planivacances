package be.helmo.planivacances.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class LoginUserDTO {

    @Schema(name = "Mail", example = "addresse@mail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Email(message = "Addresse mail invalide")
    private String mail;
    @Schema(name = "Mot de passe", example = "mot de passe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 8, message = "Le mot de passe doit faire minimum 8 caractères")
    private String password;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
