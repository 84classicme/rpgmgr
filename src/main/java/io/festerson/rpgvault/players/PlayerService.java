package io.festerson.rpgvault.players;

import com.generated.CountriesPort;
import com.generated.CountriesPortService;
import com.generated.GetCountryRequest;
import com.generated.GetCountryResponse;
import io.festerson.rpgvault.domain.Player;
import io.festerson.rpgvault.exception.RpgMgrException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.xml.ws.BindingProvider;

import static io.festerson.rpgvault.MdcConfig.logOnNext;

@Slf4j
@Service
public class PlayerService {

   @Autowired
    private PlayerRepository playerRepository;

    @Value( "${soap.country-service.endpoint-url}" )
    private String webserviceEndpointUrl;

    public Flux<Player> getPlayers(){
        return playerRepository.findAll()
            .onErrorResume(t -> Mono.error(handleException(t)))
            .doOnEach(logOnNext(r -> log.info("found player {} {}", r.getFirstName(), r.getLastName())));
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
                player.set_id(id);
                if(player.getFirstName() != null && !player.getFirstName().isEmpty())
                found.setFirstName(player.getFirstName());
                if(player.getLastName() != null && !player.getLastName().isEmpty())
                found.setLastName(player.getLastName());
                if(player.getTimezone() != null && !player.getTimezone().isEmpty())
                found.setTimezone(player.getTimezone());
                if(player.getEmail() != null && !player.getEmail().isEmpty())
                found.setEmail(player.getEmail());
                if(player.getImageUrl() != null)
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
        log.error("Exception in PlayerService:", t);
        return new RpgMgrException(t);
    }

    public Mono<GetCountryResponse> getCountryFromWebService(GetCountryRequest input) {
        return Mono.create(sink -> getPortType().getCountryAsync(input, ReactorAsyncHandler.into(sink)));
    }

    private CountriesPort getPortType(){
        CountriesPortService service = new CountriesPortService();
        CountriesPort portType = service.getCountriesPortSoap11();
        BindingProvider bp = (BindingProvider)portType;
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webserviceEndpointUrl);
        return portType;
    }
}
