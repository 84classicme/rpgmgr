package io.festerson.rpgvault.characters;

import io.festerson.rpgvault.domain.PlayerCharacter;
import io.festerson.rpgvault.exception.RpgMgrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public Flux<PlayerCharacter> getCharacters(){
        return characterRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<PlayerCharacter> getCharactersByPlayerId(String id){
        return characterRepository.getCharactersByPlayerId(id).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<PlayerCharacter> getCharacterById(String id){
        return characterRepository.findById(id).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<PlayerCharacter> createCharacter(PlayerCharacter playerCharacter){
        return characterRepository.save(playerCharacter).map(campaign1 -> campaign1).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<PlayerCharacter> updateCharacter(String id, PlayerCharacter playerCharacter){
        return characterRepository.findById(id)
            .flatMap(found -> {
                found.setId(id);
                if(playerCharacter.getAc() != null && !playerCharacter.getAc().toString().isEmpty())
                    found.setAc(playerCharacter.getAc());
                if(playerCharacter.getCclass() != null)
                    found.setCclass(playerCharacter.getCclass());
                if(playerCharacter.getCharisma() != null && !playerCharacter.getCharisma().toString().isEmpty())
                    found.setCharisma(playerCharacter.getCharisma());
                if(playerCharacter.getCharisma() != null)
                    found.setCrace(playerCharacter.getCrace());
                if(playerCharacter.getCtype() != null)
                    found.setCtype(playerCharacter.getCtype());
                if(playerCharacter.getDexterity() != null && !playerCharacter.getDexterity().toString().isEmpty())
                    found.setDexterity(playerCharacter.getDexterity());
                if(playerCharacter.getHp() != null && !playerCharacter.getHp().toString().isEmpty())
                    found.setHp(playerCharacter.getHp());
                if(playerCharacter.getImageUrl() != null && !playerCharacter.getImageUrl().isEmpty())
                    found.setImageUrl(playerCharacter.getImageUrl());
                if(playerCharacter.getIntelligence() != null && !playerCharacter.getIntelligence().toString().isEmpty())
                    found.setIntelligence(playerCharacter.getIntelligence());
                if(playerCharacter.getName() != null && !playerCharacter.getName().isEmpty())
                    found.setName(playerCharacter.getName());
                if(playerCharacter.getPlayerId() != null && !playerCharacter.getPlayerId().isEmpty())
                    found.setPlayerId(playerCharacter.getPlayerId());
                if(playerCharacter.getLevel() != null && !playerCharacter.getLevel().toString().isEmpty())
                    found.setLevel(playerCharacter.getLevel());
                if(playerCharacter.getStrength() != null && !playerCharacter.getStrength().toString().isEmpty())
                    found.setStrength(playerCharacter.getStrength());
                if(playerCharacter.getWisdom() != null && !playerCharacter.getWisdom().toString().isEmpty())
                    found.setWisdom(playerCharacter.getWisdom());
                return Mono.just(found);
            })
            .flatMap(updatedCharacter -> characterRepository.save(updatedCharacter))
            .onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Void> deleteCharacter(String id){
        return characterRepository.findById(id)
            .flatMap(toDelete -> characterRepository.delete(toDelete))
            .onErrorResume(t -> Mono.error(handleException(t)));
    }

    private RpgMgrException handleException(Throwable t){
        log.error("Exception in CharacterService:", t);
        return new RpgMgrException(t);
    }
}
