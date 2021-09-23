package io.festerson.rpgvault.integration.players;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.integration.CucumberTest;
import io.festerson.rpgvault.players.PlayerRepository;
import io.festerson.rpgvault.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class PlayerDefinitions extends CucumberTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    PlayerRepository repository;

    @DataTableType
    public Player playerEntry(Map<String, String> entry) {
        return new Player(
            entry.get("id"),
            entry.get("firstName"),
            entry.get("lastName"),
            entry.get("email"),
            entry.get("imageUrl"));
    }

    private Player player;
    private Flux<Player> playerFlux;
    private Flux<Player> updatedFlux;
    private Flux<Player> deletedFlux;
    private String url;

    @Before
    public void setUp() {
        playerFlux = TestUtils.buildPlayers();
        updatedFlux = TestUtils.buildPlayersToUpdate();
        deletedFlux = TestUtils.buildPlayersToDelete();
    }

    @After
    public void tearDown() {
        repository.deleteAll(playerFlux).then().block();
        repository.deleteAll(updatedFlux).then().block();
        repository.deleteAll(deletedFlux).then().block();
    }

    @Given("player information is available in the database")
    public void addPlayers(){
        repository.saveAll(playerFlux).then().block();
    }

    @Given("player data exists that needs to be updated")
    public void addPlayersToUpdate(){
        repository.saveAll(updatedFlux).then().block();
    }

    @Given("player data exists that need to be deleted")
    public void addPlayersToDelete(){
        repository.saveAll(deletedFlux).then().block();
    }

    @When("I include the {word} in the url of the request")
    public void getPlayer(String id){
        url = "/players/" + id;
    }

    @When("I request information about all players by using a GET url")
    public void getAllPlayers(){
        url = "/players";
    }

    @When("I request to create a new player by POSTING their first name {word} and last name {word} and email {word} and image url {word}")
    public void createPlayer(String firstname, String lastname, String email, String imageurl){
        player = new Player(firstname, lastname, email, imageurl);
        url = "/players";
    }

    @When("I have new {word} {word} {word} or {word} data for a {word}")
    public void updatePlayerEmail(String firstname, String lastname, String email, String imageUrl, String id){
        player = new Player(id,firstname, lastname, email, imageUrl);
        url = "/players/" + id;
    }

    @And("I PUT a request into the system")
    public void setPutUrl(){ url = "/players/" + player.getId(); }

    @When("an invalid {word} {word} or {word} is used in player data")
    public void createPlayer(String firstname, String lastname, String email){
        if(firstname.equals("empty")) {firstname = "";}
        else if(firstname.equals("null")) {firstname = null;}

        if(lastname.equals("empty")) {lastname = "";}
        else if(lastname.equals("null")) {lastname = null;}

        player = new Player(firstname, lastname, email, "https://www.example.com/my-image.jpg");
    }

    @When("I create a new player")
    public void initPlayer(){ player = new Player(); }

    @And("first name is {word}")
    public void addPlayerFirstName(String fristName){ player.setFirstName(fristName); }

    @And("last name is {word}")
    public void addPlayerLastName(String lastName){ player.setLastName(lastName); }

    @And("email is {word}")
    public void addPlayerEmail(String email){ player.setEmail(email); }

    @And("image url is {word}")
    public void addPlayerImageUrl(String imageUrl){
        player.setImageUrl(imageUrl);
    }

    @And("I POST a request into the system")
    public void setPostUrl(){ url = "/players"; }

    @Then("the response will return http status ok and player first name {word} and last name {word} and email {word} and image url {word}")
    public void verifyPlayer(String firstname, String lastname, String email, String imageurl){
        this.webTestClient
            .get().uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.firstname").isEqualTo(firstname)
            .jsonPath("$.lastname").isEqualTo(lastname)
            .jsonPath("$.email").isEqualTo(email)
            .jsonPath("$.img").isEqualTo(imageurl);
    }

    @Then("the response will return http status ok and data for all players")
    public void verifyAllPlayers(List<Player> players){
        webTestClient
            .get().uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Player.class).hasSize(players.size())
            .consumeWith(response -> {
               List<Player> list = response.getResponseBody();
               list.sort(new Comparator<Player>() {
                   @Override
                   public int compare(Player p1, Player p2) {
                       return p1.getFirstName().compareTo(p2.getFirstName());
                   }
               });
                players.sort(new Comparator<Player>() {
                    @Override
                    public int compare(Player p1, Player p2) {
                        return p1.getFirstName().compareTo(p2.getFirstName());
                    }
                });
                for (int i = 0; i<list.size(); i++){
                    Assertions.assertEquals(players.get(i).getId(), list.get(i).getId());
                    Assertions.assertEquals(players.get(i).getFirstName(), list.get(i).getFirstName());
                    Assertions.assertEquals(players.get(i).getLastName(), list.get(i).getLastName());
                    Assertions.assertEquals(players.get(i).getEmail(), list.get(i).getEmail());
                    Assertions.assertEquals(players.get(i).getImageUrl(), list.get(i).getImageUrl());
                }
            });
    }

    @Then("the system creates the record and responds with http status code 201 and returns the new player with a unique id")
    public void verifyNewPlayer(){
        webTestClient
            .post().uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(player), Player.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Player.class)
            .consumeWith(response -> {
                Player newplayer =  response.getResponseBody();
                Assertions.assertNotNull(newplayer);
                Assertions.assertNotNull(newplayer.getId());
                Assertions.assertEquals(player.getFirstName(), newplayer.getFirstName());
                Assertions.assertEquals(player.getLastName(), newplayer.getLastName());
                Assertions.assertEquals(player.getEmail(), newplayer.getEmail());
                Assertions.assertEquals(player.getImageUrl(), newplayer.getImageUrl());
                Player found = repository.findById(newplayer.getId()).block();
                Assertions.assertNotNull(found);
                Assertions.assertEquals(player.getFirstName(), found.getFirstName());
                Assertions.assertEquals(player.getLastName(), found.getLastName());
                Assertions.assertEquals(player.getEmail(), found.getEmail());
                Assertions.assertEquals(player.getImageUrl(), found.getImageUrl());
                repository.delete(newplayer).block();
            });
    }

    @Then("the system updates the record and responds with http status code 200 and returns the updated player")
    public void verifyUpdatedPlayer(){
        webTestClient
            .put().uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(player), Player.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Player.class)
            .consumeWith(response -> {
                Player updated =  response.getResponseBody();
                Assertions.assertNotNull(updated);
                Assertions.assertNotNull(player.getId());
                Assertions.assertEquals(player.getFirstName(), updated.getFirstName());
                Assertions.assertEquals(player.getLastName(), updated.getLastName());
                Assertions.assertEquals(player.getEmail(), updated.getEmail());
                Assertions.assertEquals(player.getImageUrl(), updated.getImageUrl());
                Player found = repository.findById(updated.getId()).block();
                Assertions.assertNotNull(found);
                Assertions.assertEquals(player.getFirstName(), found.getFirstName());
                Assertions.assertEquals(player.getLastName(), found.getLastName());
                Assertions.assertEquals(player.getEmail(), found.getEmail());
                Assertions.assertEquals(player.getImageUrl(), found.getImageUrl());
            });
    }

    @Then("the system deletes the record and responds with http status no content")
    public void verifyDeletedPlayer(){
        webTestClient
            .delete().uri(url)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNoContent();
    }

    @And("I can no longer get the player data by using the {word}")
    public void verifyGetDeletedPlayerFails(String id){
        webTestClient
            .get().uri("/players/" + id)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }

    @Then("the system responds with http status code 400 and a {string} about each invalid field in the POST")
    public void verifyCreatedPlayer(String message){
        webTestClient
            .post().uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(player), Player.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .consumeWith(response -> {
                byte[] messageBytes =  response.getResponseBody();
                String errorMessage = new String(messageBytes);
            });
    }

    @And("the system responds with http status code 400 and a {string} about each invalid field in the PUT")
    public void verifyUpdatedPlayer(String message){
        webTestClient
            .put().uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(player), Player.class)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is4xxClientError()
            .expectBody()
            .consumeWith(response -> {
                byte[] messageBytes =  response.getResponseBody();
                String errorMessage = new String(messageBytes);
            });
    }
}
