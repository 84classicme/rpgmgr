package io.festerson.rpgvault.players;

import io.festerson.rpgvault.domain.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Flux<Player> getPlayers(){
        return playerRepository.findAll();
    }

    public Mono<Player> getPlayerById(String id){
        return playerRepository.findById(id);
    }

    public Mono<Player> createPlayer(Player player){
        return playerRepository.save(player).map(player1 -> player1);
    }

    public Mono<Player> updatePlayer(String id, Player player){
        return playerRepository.findById(id)
            .flatMap(found -> {
                player.setId(id);
                if(player.getName() != null && !player.getName().isEmpty())
                    found.setName(player.getName());
                if(player.getEmail() != null && !player.getEmail().isEmpty())
                    found.setEmail(player.getEmail());
                if(player.getImageUrl() != null && !player.getImageUrl().isEmpty())
                    found.setImageUrl(player.getImageUrl());
                return Mono.just(found);
            })
            .flatMap(updatedPlayer -> playerRepository.save(updatedPlayer));
    }

    public Mono<Void> deletePlayer(String id){
        return playerRepository.findById(id)
            .flatMap(toDelete ->
                playerRepository.delete(toDelete));
    }

}
