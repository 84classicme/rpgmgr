package io.festerson.rpgvault.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection="players")
public class Player {

    @Id
    private String id;

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

    @JsonProperty("img")
    private String imageUrl;

    public Player(
            String firstName,
            String lastName,
            String email,
            String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    //Cucumber 6.10.3
    public static Player createPlayer(Map<String, String> entry) {
        Player player = new Player();
        player.setId(entry.get("id"));
        player.setFirstName(entry.get("firstname"));
        player.setLastName(entry.get("lastname"));
        player.setEmail(entry.get("email"));
        player.setImageUrl(entry.get("imageurl"));
        return player;
    }
}
