package be.helmo.planivacances.controller;

import be.helmo.planivacances.model.dto.PlaceDTO;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/place")
public class PlaceController {

    @Autowired
    private PlaceService placeServices;

    @Operation(summary = "Crée le lieu pour le groupe donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le lieu a bien été créé, retourne une String"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la creation du lieu",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/{gid}")
    public String createPlace(
            @RequestBody PlaceDTO place,
            @PathVariable("gid") String gid)
            throws ResponseStatusException {

        try {
            return placeServices.createOrUpdatePlace(gid, place);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la creation du lieu");
        }

    }

    @Operation(summary = "Récupère le lieu donné pour le groupe donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le lieu a bien été récupéré, retourne une PlaceDTO"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la recuperation du lieu de vacances",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{gid}/{pid}")
    public PlaceDTO getGroupPlace(
            @PathVariable("gid") String gid,
            @PathVariable("pid") String pid)
            throws ResponseStatusException {

        try {
            return placeServices.getPlace(gid, pid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la recuperation du lieu de vacances");
        }

    }

    @Operation(summary = "Récupère les lieux pour un groupe donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le lieu a bien été récupéré, retourne une List<PlaceDTO>"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la recuperation des lieux",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/list/{gid}")
    public List<PlaceDTO> getPlaces(
            @PathVariable("gid") String gid)
            throws ResponseStatusException {

        try {
            return placeServices.getGroupPlaces(gid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la recuperation des lieux");
        }

    }

    @Operation(summary = "Supprime le lieu donné pour un groupe donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le lieu a bien été supprimé, retourne une String")
    })
    @DeleteMapping("/{gid}/{pid}")
    public String deletePlace(
            @PathVariable("gid") String gid,
            @PathVariable("pid") String pid) throws ResponseStatusException {

            return placeServices.deletePlace(gid, pid);

    }
}
