package be.helmo.planivacances.model.dto;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class FormContactDTO {
    @Schema(name = "Mail", example = "addresse@mail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Email(message = "Addresse mail invalide")
    private String email;
    @Schema(name = "Sujet", example = "Sujet de contact", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Le champs sujet est obligatoire")
    private String subject;
    @Schema(name = "Message", example = "Message du contact", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Le champs message est obligatoire")
    private String message;

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
