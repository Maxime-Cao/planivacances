package be.helmo.planivacances.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

public class GroupInviteDTO {

    @Schema(name = "Gid", example = "gid", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String gid;
    @Schema(name = "Nom du groupe", example = "nom du groupe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String groupName;

    public GroupInviteDTO(String gid, String groupName) {
        this.gid = gid;
        this.groupName = groupName;
    }

    //getters

    public String getGid() {
        return gid;
    }

    public String getGroupName() {
        return groupName;
    }

    //setters

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
