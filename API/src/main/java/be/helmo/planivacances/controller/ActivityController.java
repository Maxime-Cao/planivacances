package be.helmo.planivacances.controller;

import be.helmo.planivacances.model.dto.ActivityDTO;
import be.helmo.planivacances.model.firebase.dto.DBActivityDTO;
import be.helmo.planivacances.model.dto.GroupDTO;
import be.helmo.planivacances.service.ActivityService;
import be.helmo.planivacances.service.FcmService;
import be.helmo.planivacances.service.GroupService;
import be.helmo.planivacances.service.PlaceService;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    @Autowired
    private GroupService groupServices;

    @Autowired
    private ActivityService activityServices;

    @Autowired
    private PlaceService placeServices;

    @Autowired
    private FcmService fcmServices;

    @Operation(summary = "Crée une activité dans le groupe demandé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Activité bien créé, retourne une String"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la création de l'activité",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/{gid}")
    public String createActivity(@PathVariable("gid") String gid, @RequestBody ActivityDTO activity) {

        try {
            String pid = placeServices.createOrUpdatePlace(gid, activity.getPlace());

            DBActivityDTO activityDTO = new DBActivityDTO(
                    activity.getTitle(),
                    activity.getDescription(),
                    activity.getStartDate(),
                    activity.getDuration(),
                    pid);

            GroupDTO group = groupServices.getGroup(gid);

            fcmServices.sendActivityAddedNotification(gid, group.getGroupName(), activity.getTitle());

            return activityServices.createGroupActivity(gid, activityDTO);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la création de l'activité");
        } catch (FirebaseMessagingException e) {
            System.out.printf("Notification de l'activité %s non distribuée pour le groupe %s", activity.getTitle(), gid);
        }

        return null;
    }

    @Operation(summary = "Met à jour une activité dans le groupe demandé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Activité bien mise à jour, retourne un boolean"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la mise à jour de l'activité",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/{gid}/{aid}")
    public boolean updateGroupActivity(@PathVariable("gid") String gid,
                                      @PathVariable("aid") String aid,
                                      @RequestBody ActivityDTO activity) {

        try {
            String pid = placeServices.createOrUpdatePlace(gid, activity.getPlace());

            DBActivityDTO activityDTO = new DBActivityDTO(
                    activity.getTitle(),
                    activity.getDescription(),
                    activity.getStartDate(),
                    activity.getDuration(),
                    pid);

            return activityServices.updateGroupActivity(gid, aid, activityDTO);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la mise à jour de l'activité");
        }
    }

    @Operation(summary = "Récupère l'activité demandée dans le groupe demandé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Activité bien récupérée, retourne un ActivityDTO"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la récupération de l'activité",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{gid}/{aid}")
    public ActivityDTO getGroupActivity(@PathVariable("gid") String gid, @PathVariable("aid") String aid)
            throws ResponseStatusException {

        try {
            ActivityDTO activity = activityServices.getGroupActivity(gid, aid);
            if(activity != null) {
                return activity;
            } else throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération de l'activité");
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération de l'activité");
        }

    }

    @Operation(summary = "Récupère les activités dans le groupe demandé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Activités bien récupérée, retourne une Map<String, ActivityDTO>"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la récupération des activités",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{gid}")
    public Map<String, ActivityDTO> getGroupActivities(@PathVariable("gid") String gid)
            throws ResponseStatusException {

        try {
            return activityServices.getGroupActivities(gid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération des activités");
        }

    }

    @Operation(summary = "Supprime l'activités demandée dans le groupe demandé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Activités bien supprimée, retourne une String")
    })
    @DeleteMapping("/{gid}/{aid}")
    public String deleteActivity(@PathVariable("gid") String gid, @PathVariable("aid") String aid) {
        return activityServices.deleteActivity(gid, aid);
    }

    @Operation(summary = "Exporte les activités sous forme de calendrier .ics dans le groupe demandé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Calendrier .ics bien récupérée, retourne une String"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de l'exportation du calendrier",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping(value = "/calendar/{gid}",produces = "text/calendar")
    public String exportCalendar(@PathVariable("gid") String gid)
            throws ResponseStatusException {

        try {
            return activityServices.exportCalendar(gid);
        } catch (ExecutionException | InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de l'exportation du calendrier");
        }

    }
}
