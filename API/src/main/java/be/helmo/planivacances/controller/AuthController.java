package be.helmo.planivacances.controller;


import be.helmo.planivacances.model.dto.LoginUserDTO;
import be.helmo.planivacances.model.dto.RegisterUserDTO;
import be.helmo.planivacances.service.AuthService;
import be.helmo.planivacances.service.UserService;
import com.google.firebase.auth.FirebaseAuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authServices;
    @Autowired
    private UserService userServices;

    /**
     * [Post] Crée un utilisateur à partir d'un mail, mot de passe et nom d'utilisateur
     *
     * @param authUser (AuthUser) objet contenant le mail, mot de passe et le nom d'utilisateur
     * @return (String) token du nouvel utilisateur
     * @throws ResponseStatusException Bad_request si utilisateur non créé ou InternalServerError
     */
    @Operation(summary = "Crée un utilisateur à partir d'un nom d'utilisateur, mail et mot de passe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Utilisateur bien enregistré et retourne un custom Token"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - Utilisateur non créé, données d'entrée eronnée",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la création de l'utilisateur",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/register")
    public String createUser(@Valid @RequestBody RegisterUserDTO authUser) throws ResponseStatusException {
        try {

            String token = authServices.createUser(authUser);

            if (token != null) {
                userServices.sendSSEUpdateToEveryone();
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Utilisateur non créé");
            }

            return token;

        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la creation du compte");
        }
    }

    /**
     * [Post] Recupère le token d'identification de l'utilisateur correspondant au mail et au mot de passe
     *
     * @param authUser (AuthUser) objet contenant le mail, mot de passe et le nom d'utilisateur
     * @return (String) token d'identification
     * @throws ResponseStatusException BadRequest Mail ou mot de passe invalide ou InternalServer
     */
    @Operation(summary = "Authentifie un utilisateur avec mail et mot de passe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Utilisateur bien authentifié et retourne un custom Token"),
            @ApiResponse(responseCode = "400",
                    description = "Bad Request - Utilisateur non authentifié, Mail ou mot de passe invalide",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500",
                    description = "Internal Error - Erreur lors de la connexion",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody LoginUserDTO authUser)
            throws ResponseStatusException {
        try {
            String token = authServices.loginUser(authUser.getMail(), authUser.getPassword());

            if (token == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mail ou mot de passe invalide");
            }

            return token;
        } catch (FirebaseAuthException | GeneralSecurityException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la connexion");
        }
    }

    /**
     * [Post] Vérifie le jeton d'authentification et renvoie l'uid de l'utilisateur correspondant
     *
     * @param authorizationHeader (String) jeton d'authentification au format : "Bearer auth_token"
     * @return (String) uid
     */
    @Operation(summary = "Vérifie le jeton d'authentification et vrai si le token est valide sinon faux")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Retourne True si jeton valide, dans tous les autres cas faux")
    })
    @PostMapping("/token")
    public boolean verifyToken(
            @RequestHeader("Authorization") String authorizationHeader) {
        return authServices.verifyToken(authorizationHeader) != null;
    }

}