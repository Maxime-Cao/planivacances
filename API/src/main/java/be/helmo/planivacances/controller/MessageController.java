package be.helmo.planivacances.controller;

import be.helmo.planivacances.model.dto.GroupMessageDTO;
import be.helmo.planivacances.service.AuthService;
import be.helmo.planivacances.service.GroupService;
import be.helmo.planivacances.service.interfaces.MessageListener;
import be.helmo.planivacances.service.MessageService;
import com.pusher.rest.Pusher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Controller
@DependsOn("messageService")
@RequestMapping("/api/chat")
public class MessageController implements MessageListener {
    @Autowired
    private MessageService messageService;

    @Autowired
    private AuthService authService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private Pusher pusher;

    @PostConstruct
    public void init() {
        messageService.addListener(this);
    }

    @PreDestroy
    public void destroy() {
        messageService.removeListener(this);
    }

    @Operation(summary = "Authentification au channel de message du groupe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "L'utilisateur à bien été authentifié', retourne un String"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - L'utilisateur n'a pas accès",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - L'utilisateur n'est pas authorisé",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/")
    public ResponseEntity<String> authenticate(@RequestParam("socket_id") String socketId,
                                               @RequestParam("channel_name") String channelName,
                                               @RequestHeader("Authorization") String authToken) {
        String uid;
        String gid = channelName.replaceAll("private-","");
        if((uid = authService.verifyToken(authToken)) != null) {
            if (groupService.isInGroup(uid, gid)) {
                String auth = pusher.authenticate(socketId,channelName);
                return ResponseEntity.ok(auth);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
    }

    @Operation(summary = "Récupère la liste des 100 précédents messages envoyés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "La liste des messages à bien été récupérée, retourne un List<GroupMessageDTO>"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - L'utilisateur n'a pas accès",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - L'utilisateur n'est pas authorisé",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/messages")
    public ResponseEntity<List<GroupMessageDTO>> sendPreviousMessages(@RequestHeader("Authorization") String authToken,
                                                                      @RequestHeader("GID") String gid) {
        String uid;
        if((uid = authService.verifyToken(authToken)) != null) {
            if(groupService.isInGroup(uid,gid)) {
                List<GroupMessageDTO> previousMessages = messageService.getRecentMessages(gid, 100);
                return ResponseEntity.ok(previousMessages);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "Envoi et sauvegarde le message donné")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Message envoyé et sauvegardé avec succès, retourne un String"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - L'utilisateur n'a pas accès, Erreur durant l'envoi du message",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - L'utilisateur n'est pas authorisé, Erreur durant l'envoi du message",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/message")
    public ResponseEntity<String> handleMessage(@RequestBody GroupMessageDTO message,
                                                @RequestHeader("Authorization") String authToken) {
        String uid;
        if((uid = authService.verifyToken(authToken)) != null) {
            if (groupService.isInGroup(uid, message.getGroupId())) {
                messageService.saveMessage(message);
                return ResponseEntity.ok("Message envoyé avec succès");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erreur durant l'envoi du message");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Erreur durant l'envoi du message");
        }
    }

    @Override
    public void onNewMessage(GroupMessageDTO message) {
        if(message != null) {
            pusher.trigger(String.format("private-%s",message.getGroupId()),"new_messages",message);
        }
    }
}