package io.festerson.rpgvault.players;

import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.exception.RpgMgrException;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@CommonsLog
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public Flux<Player> getPlayers(){
        return playerRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Player> getPlayerById(String id){
        return playerRepository.findById(id).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Player> createPlayer(Player player){
        return playerRepository.save(player).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Player> updatePlayer(String id, Player player){
        return playerRepository.findById(id)
            .map(found -> {
                player.setId(id);
                if(player.getFirstName() != null && !player.getFirstName().isEmpty())
                    found.setFirstName(player.getFirstName());
                if(player.getEmail() != null && !player.getEmail().isEmpty())
                    found.setEmail(player.getEmail());
                if(player.getImageUrl() != null && !player.getImageUrl().isEmpty())
                    found.setImageUrl(player.getImageUrl());
                return found;
            })
            .flatMap(updatedPlayer -> playerRepository.save(updatedPlayer).thenReturn(updatedPlayer))
            .onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Void> deletePlayer(String id){
        return playerRepository.findById(id)
            .flatMap(toDelete -> playerRepository.delete(toDelete))
            .onErrorResume(t -> Mono.error(handleException(t)));
    }

    private RpgMgrException handleException(Throwable t){
        log.error("Exception in PlayerController:", t);
        return new RpgMgrException(t);
    }
}
