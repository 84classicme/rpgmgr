package io.festerson.rpgvault.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection="players")
public class Player {

    @Id
    @JsonProperty("id")
    private String _id;

    @NotBlank
    @JsonProperty("firstname")
    private String firstName;

    @NotBlank
    @JsonProperty("lastname")
    private String lastName;

    @Email
    @NotNull
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("timezone")
    private String timezone;

    @JsonProperty("img")
    private String imageUrl;

    public Player(
            String firstName,
            String lastName,
            String email,
            String timezone,
            String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.timezone = timezone;
        this.email = email;
        this.imageUrl = imageUrl;
    }
}
