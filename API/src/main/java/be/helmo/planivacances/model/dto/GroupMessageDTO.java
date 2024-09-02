package be.helmo.planivacances.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class GroupMessageDTO {
    @Schema(name = "Expéditeur", example = "uid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String sender;
    @Schema(name = "Nom de l'expéditeur", example = "nom de l'utilisateur", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String displayName;
    @Schema(name = "Gid", example = "gid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String groupId;
    @Schema(name = "Contenu", example = "message", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String content;
    @Schema(name = "Moment de l'envoi", example = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(0)
    private long time;

    public String getSender() {
        return sender;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getContent() {
        return content;
    }

    public long getTime() {
        return time;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(long time) {
        this.time = time;
    }
}