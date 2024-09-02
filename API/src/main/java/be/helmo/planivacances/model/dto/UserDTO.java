package be.helmo.planivacances.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserDTO {
    @Schema(name = "Id de l'utilisateur", example = "uid")
    private final String uid;
    @Schema(name = "Mail", example = "addresse@mail.com")
    private final String email;
    @Schema(name = "Nom de l'utilisateur", example = "nom d'utilisateur")
    private final String displayName;

    public UserDTO(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }
}
