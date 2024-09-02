package be.helmo.planivacances.model.dto;

import be.helmo.planivacances.model.firebase.dto.DBGroupDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class GroupDTO {

    @Schema(name = "GID", example = "gid")
    private String gid;
    @Schema(name = "Nom du groupe", example = "Nom du groupe", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    @Size(min = 3, message = "Le nom du groupe de vacances doit faire minimum 3 caractères")
    private String groupName;
    @Schema(name = "Description", example = "description du groupe de vacances")
    private String description;
    @Schema(name = "Date de début", example = "2023-12-21T01:00:00.000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date startDate;
    @Schema(name = "Date de fin", example = "2023-12-27T01:00:00.000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date endDate;
    @Schema(name = "Lieu", example = "PlaceDTO.class", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private PlaceDTO place;
    @Schema(name = "Créateur", example = "uid")
    private String owner;

    public GroupDTO() {}

    public GroupDTO(DBGroupDTO g, String gid, PlaceDTO place) {
        this.gid = gid;
        this.groupName = g.getGroupName();
        this.description = g.getDescription();
        this.startDate = g.getStartDate();
        this.endDate = g.getEndDate();
        this.place = place;
        this.owner = g.getOwner();
    }


    //getters
    public String getGid() {
        return gid;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PlaceDTO getPlace() {
        return place;
    }

    public String getOwner() {
        return owner;
    }


    //setters
    public void setGid(String gid) {
        this.gid = gid;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

