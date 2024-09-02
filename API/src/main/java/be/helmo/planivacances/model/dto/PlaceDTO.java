package be.helmo.planivacances.model.dto;

import be.helmo.planivacances.model.firebase.dto.DBPlaceDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class PlaceDTO {
    @Schema(name = "Pays", example = "pays", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String country;
    @Schema(name = "Ville", example = "ville")
    private String city;
    @Schema(name = "Rue", example = "rue")
    private String street;
    @Schema(name = "Numéro de maison", example = "numéro")
    @Pattern(regexp = "^\\d*$", message = "Le numéro de boite est invalide")
    private String number;
    @Schema(name = "Code postal", example = "code postal")
    @Size(min = 4, message = "Le code postal doit comprendre au moins 4 chiffres")
    @Pattern(regexp = "^\\d*$", message = "Le code postal est invalide")
    private String postalCode;
    @Schema(name = "Latitude", example = "latitude", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private double lat;
    @Schema(name = "Longitude", example = "longitude", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private double lon;

    public PlaceDTO() {}

    public PlaceDTO(DBPlaceDTO p) {
        this.country = p.getCountry();
        this.city = p.getCity();
        this.street = p.getStreet();
        this.number = p.getNumber();
        this.postalCode = p.getPostalCode();
        this.lat = p.getLat();
        this.lon = p.getLon();
    }

    //getters

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getAddress() {
        return String.format("%s, %s, %s %s, %s", getStreet(), getNumber(), getCity(), getPostalCode(), getCountry());
    }


    //setters

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

}
