package io.festerson.rpgvault.campaigns;

import io.festerson.rpgvault.WebClientConfig;
import io.festerson.rpgvault.domain.Campaign;
import io.festerson.rpgvault.exception.*;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;


@Service
@CommonsLog
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private ExceptionService exceptionService;

    @Value( "${rest.country-service.endpoint-url}" )
    private String restEndpointUrl;

    private WebClientConfig webClientConfig;

    public Flux<Campaign> getCampaigns(){
        return campaignRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Campaign> getCampaignsByPlayerId(String id){
        return campaignRepository.getCampaignsByPlayerId(id).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Campaign> getCampaignById(String id){
        return campaignRepository.findById(id).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Campaign> createCampaign(Campaign campaign){
        return campaignRepository.save(campaign).map(campaign1 -> campaign1).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Campaign> updateCampaign(String id, Campaign campaign){
        return campaignRepository.findById(id)
            .flatMap(found -> {
                if(campaign.getName() != null && !campaign.getName().isEmpty())
                    found.setName(campaign.getName());
                if(campaign.getStartDate() != null)
                    found.setStartDate(campaign.getStartDate());
                if(campaign.getEndDate() != null)
                    found.setEndDate(campaign.getEndDate());
                if(campaign.getPlayerIds() != null)
                    found.setPlayerIds(campaign.getPlayerIds());
                if(campaign.getCharacterIds() != null)
                    found.setCharacterIds(campaign.getCharacterIds());
                if(campaign.getNpcIds() != null)
                    found.setNpcIds(campaign.getNpcIds());
                if(campaign.getMonsterIds() != null)
                    found.setMonsterIds(campaign.getMonsterIds());
                if(campaign.getDmId() != null && !campaign.getDmId().isEmpty())
                    found.setDmId(campaign.getDmId());
                if(campaign.getDescription() != null && !campaign.getDescription().isEmpty())
                    found.setDescription(campaign.getDescription());
                if(campaign.getImageUrl() != null && !campaign.getImageUrl().isEmpty())
                    found.setImageUrl(campaign.getImageUrl());
                return Mono.just(found);
            })
            .flatMap(updatedCampaign -> campaignRepository.save(updatedCampaign))
            .onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<Void> deleteCampaign(String id){
        return campaignRepository.findById(id)
            .flatMap(toDelete -> campaignRepository.delete(toDelete))
            .onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Mono<CampaignRequest> getCampaignDataRestService(CampaignRequest input) {
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactiveRestClient();
        return reactiveRestClient.get()
            .uri(this.restEndpointUrl+"/{name}", input)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(CampaignRequest.class);
                }
                else if (response.statusCode().is4xxClientError()){
                    return Mono.error(
                        new ClientException(
                            "CLIENT EXCEPTION in CountryService.",
                            response.rawStatusCode())) ;
                } else if (response.statusCode().is5xxServerError()){
                    return Mono.error(
                        new ServiceException(
                            "EXTERNAL SERVICE EXCEPTION in CountryService.",
                            response.rawStatusCode()));
                } else {
                    return Mono.error( new ServiceException(
                        "SERVICE EXCEPTION in CountryService. Unexpected response. Retrying...",
                        response.rawStatusCode()));
                }})
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e, input);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException, input)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process CountryService.getCountry due to client error.",
                            clientException))));
    }

    private RpgMgrException handleException(Throwable t){
        log.error("Exception in CampaignService:", t);
        return new RpgMgrException(t);
    }

    private Mono<Void> handleServiceException(ServiceException e, CampaignRequest input){
        System.out.println("Handling service exception in CountryService.");
        return recordException(e, input.toString());
    }

    private Mono<Void> handleClientException(ClientException e, CampaignRequest input){
        System.out.println("Handling client exception in CountryService.");
        return recordException(e, input.toString());
    }

    private Mono<Void> recordException(Exception e, String payload){
        System.out.println("Recording exception in CountryService.");
        return exceptionService.recordExceptionEvent(buildExceptionEvent(e, payload));
    }

    private ExceptionEvent buildExceptionEvent(Exception e, String payload){
        return ExceptionEvent.builder()
            .message(e.getMessage())
            .service("CountryService")
            .exception(e.getClass().getSimpleName())
            .payload(payload)
            .datetime(ZonedDateTime.now(ZoneOffset.UTC).toString()) //UTC timestamp as string
            .build();
    }

}
