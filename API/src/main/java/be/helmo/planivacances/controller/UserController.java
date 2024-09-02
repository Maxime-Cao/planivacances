package be.helmo.planivacances.controller;

import be.helmo.planivacances.model.dto.UserDTO;
import be.helmo.planivacances.model.dto.FormContactDTO;
import be.helmo.planivacances.service.FcmService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import be.helmo.planivacances.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userServices;

    @Autowired
    private FcmService fcmServices;

    @Operation(summary = "Abonnement d'un utilisateur à un topic")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "L'abonnement a bien été efféctué, retourne une boolean"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la sauvegarde du token d'enregistrement fcm",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/{token}/{topic}")
    public boolean setUserRegistrationToken(HttpServletRequest request,
                                            @PathVariable("token") String registrationToken,
                                            @PathVariable("topic") String topic) {

        String uid = (String) request.getAttribute("uid");

        try {
            return fcmServices.subscribeToTopic(topic == null ? uid : topic, registrationToken);
        } catch (FirebaseMessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la sauvegarde du token d'enregistrement fcm");
        }
    }

    @Operation(summary = "Récupère les détails de l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Les détails de l'utilisateur ont bien été récupérés, retourne un USerDTO"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors du chargement de l'utilisateur",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping
    public UserDTO getUserDetails(HttpServletRequest request) {
        String uid = (String) request.getAttribute("uid");
        try {
            return userServices.getUser(uid);
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors du chargement de l'utilisateur");
        }
    }

    @Operation(summary = "Récupère le nombre d'utilisateur en vacances par pays")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Les statistiques on bien été récupérées, retourne une Map<String, Integer>"),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la récupération du nombre d'utilisateur par pays",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/country/{givenDate}")
    public Map<String, Integer> getNumberOfUsersPerCountry(@PathVariable("givenDate") String givenDate) {
        try {
            return userServices.getUserCountPerCountry(givenDate);
        } catch (ExecutionException | InterruptedException | ParseException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la récupération du nombre d'utilisateur par pays");
        }
    }

    @Operation(summary = "Récupère le nombre d'utilisateur [Flux SSE]")
    @GetMapping("/number/flux")
    public SseEmitter getNumberOfUsersStream() {
        SseEmitter sseEmitter = userServices.getNumberUsersStream();
        userServices.sendSSEUpdateToSomeone(sseEmitter);
        return sseEmitter;
    }

    @Operation(summary = "Déclenche la mise à jour du nombre d'utilisateur sur le flux SSE")
    @GetMapping("/number")
    public void refreshNumberOfUsersStream() {
        userServices.sendSSEUpdateToEveryone();
    }

    @Operation(summary = "Envoi un mail de contact aux administrateurs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Le mail a bien été envoyé, retourne une String"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - Erreur lors de l'envoi du mail",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur serveur lors de l'envoi du mail de contact",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("admin/message")
    public String contactAdmin(@RequestBody FormContactDTO form) {
        try {
            return userServices.contactAdmin(form);
        } catch (EmailException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erreur lors de l'envoi du mail");
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur serveur lors de l'envoi du mail de contact");
        }
    }

    @Operation(summary = "Supprime l'utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Utilisateur supprimé, retourne une boolean"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - Token invalide",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la suppression de l'utilisateur",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @DeleteMapping
    public boolean deleteSelfUser(HttpServletRequest request) throws ResponseStatusException {

        String uid = (String) request.getAttribute("uid");
        try {

            if (uid != null) {
                if (userServices.deleteUser(uid)) {
                    userServices.sendSSEUpdateToEveryone();
                    return true;
                }
            } else throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide");

        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erreur lors de la suppression de l'utilisateur");
        }

        return false;
    }
}
