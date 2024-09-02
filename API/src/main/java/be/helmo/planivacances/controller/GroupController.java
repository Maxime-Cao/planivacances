package be.helmo.planivacances.controller;

import be.helmo.planivacances.model.dto.GroupDTO;
import be.helmo.planivacances.service.GroupService;
import be.helmo.planivacances.service.PlaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private GroupService groupServices;

    @Autowired
    private PlaceService placeServices;

    @Operation(summary = "Crée un groupe de vacances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le groupe a bien été créé, retourne une String"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la creation du groupe",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping
    public String createGroup(HttpServletRequest request,
                              @RequestBody GroupDTO group) throws ResponseStatusException {

        String uid = (String) request.getAttribute("uid");

        try {
            group.setOwner(uid);
            return groupServices.createGroup(group);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la creation du groupe");
        }

    }

    @Operation(summary = "Récupère un groupe de vacances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le groupe a bien été récupéré, retourne un GroupDTO"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la récupération du groupe",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{gid}")
    public GroupDTO getGroup(@PathVariable("gid") String gid) throws ResponseStatusException {
        try {
            return groupServices.getGroup(gid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération du groupe");
        }
    }

    @Operation(summary = "Récupère la liste de groupe de vacances de l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Les groupe ont bien été récupérés, retourne un List<GroupDTO>"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la récupération des groupes",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/list")
    public List<GroupDTO> getUserGroups(HttpServletRequest request) throws ResponseStatusException {
        String uid = (String) request.getAttribute("uid");

        try {
            return groupServices.getGroups(uid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la recuperation des groupes");
        }

    }

    @Operation(summary = "Modifie un groupe de vacances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le groupe a bien été modifié, retourne une String"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la mise à jour du groupe",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{gid}")
    public String updateGroup(@PathVariable("gid") String gid,
                              @RequestBody GroupDTO group) throws ResponseStatusException {

        try {
            String pid = placeServices.createOrUpdatePlace(gid, group.getPlace());

            return groupServices.updateGroup(gid, group, pid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la mise à jour du groupe");
        }

    }

    @Operation(summary = "Supprime un groupe de vacances")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le groupe a bien été supprimé, retourne une String")
    })
    @DeleteMapping("/{gid}")
    public String deleteGroup(@PathVariable("gid") String gid) throws ResponseStatusException {
        return groupServices.deleteGroup(gid);
    }
}
