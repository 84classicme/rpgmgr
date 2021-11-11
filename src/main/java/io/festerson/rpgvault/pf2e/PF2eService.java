package io.festerson.rpgvault.pf2e;

import io.festerson.rpgvault.WebClientConfig;
import io.festerson.rpgvault.domain.pf2api.*;
import io.festerson.rpgvault.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Slf4j
public class PF2eService {

    @Autowired
    private ExceptionService exceptionService;

    @Autowired
    private PF2eActionRepository pf2eActionRepository;

    @Autowired
    private PF2eAncestryRepository pf2eAncestryRepository;

    @Autowired
    private PF2eSpellRepository pf2eSpellRepository;

    @Autowired
    private PF2eClassRepository pf2eClassRepository;

    @Autowired
    private PF2eEquipmentRepository  pf2eEquipmentRepository;

    @Autowired
    private PF2eAncestryFeatureRepository pf2eAncestryFeatureRepository;

    @Autowired
    private PF2eArchetypeRepository pf2eArchetypeRepository;

    @Autowired
    private PF2eBackgroundRepository pf2eBackgroundRepository;

    @Autowired
    private PF2eClassFeatureRepository pf2eClassFeatureRepository;

    @Autowired
    private PF2eDeityRepository pf2eDeityRepository;

    @Autowired
    private PF2eFeatRepository pf2eFeatRepository;

    private WebClientConfig webClientConfig;

    @Value( "${rest.campaign-service.endpoint-url}" )
    private String restEndpointUrl;

    private String actionEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/action";
    private String ancestryEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/ancestry";
    private String ancestryFeatureEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/ancestryFeature";
    private String archetypeEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/archetype";
    private String backgroundEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/background";
    private String classEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/class";
    private String classFeatureEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/classFeature";
    private String deityEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/deity";
    private String equipmentEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/equipment";
    private String featEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/feat";
    private String spellEndpointUrl = "https://api.pathfinder2.fr/v1/pf2/spell";

    public Mono<ActionResponse> getActions(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(actionEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(ActionResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getActions due to client error.",
                            clientException))));
    }

    public Mono<AncestryResponse> getAncestries(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(ancestryEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(AncestryResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<AncestryFeatureResponse> getAncestryFeatures(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(ancestryFeatureEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(AncestryFeatureResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<ArchetypeResponse> getArchetypes(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(archetypeEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(ArchetypeResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<BackgroundResponse> getBackgrounds(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(backgroundEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(BackgroundResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<ClassResponse> getClasses(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(classEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(ClassResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<ClassFeatureResponse> getClassFeatures(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(classFeatureEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(ClassFeatureResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<DeityResponse> getDeities(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(deityEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(DeityResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<EquipmentResponse> getEquipment(){
        log.error("getEquipment::START");
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(equipmentEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(EquipmentResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .log()
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<FeatResponse> getFeats(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(featEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(FeatResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<SpellResponse> getSpells(){
        webClientConfig = new WebClientConfig();
        WebClient reactiveRestClient = webClientConfig.getReactivePF2eClient();
        return reactiveRestClient.get()
            .uri(spellEndpointUrl)
            .exchangeToMono(response -> {
                if(response.rawStatusCode() == 200){
                    return response.bodyToMono(SpellResponse.class);
                } else {
                    return Mono.error(handleStatusCode(response));
                }
            })
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> !(throwable instanceof ClientException))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    ServiceException e =
                        new ServiceException(
                            "External Service failed to process after max retries.",
                            500);
                    handleServiceException(e);
                    throw e;
                }))
            .onErrorResume(ClientException.class, clientException ->
                handleClientException(clientException)
                    .then(Mono.error(
                        new ApplicationException(
                            "Cannot process PF2eService.getAncestries due to client error.",
                            clientException))));
    }

    public Mono<Void> saveSpell(SpellResult spell){
        return pf2eSpellRepository.save(spell).then();
    }


    public Mono<Void> saveActions(List<ActionResult> actions){
        return pf2eActionRepository.saveAll(actions).then();
    }

    public Mono<Void> saveAncestries(List<AncestryResult> ancestries){
        return pf2eAncestryRepository.saveAll(ancestries).then();
    }

    public Mono<Void> saveAncestryFeatures(List<AncestryFeatureResult> ancestryFeatures){
        return pf2eAncestryFeatureRepository.saveAll(ancestryFeatures).then();
    }

    public Mono<Void> saveArchetypes(List<ArchetypeResult> archetypes){
        return pf2eArchetypeRepository.saveAll(archetypes).then();
    }

    public Mono<Void> saveBackgrounds(List<BackgroundResult> backgrounds){
        return pf2eBackgroundRepository.saveAll(backgrounds).then();
    }

    public Mono<Void> saveClasses(List<ClassResult> classes){
        return pf2eClassRepository.saveAll(classes).then();
    }

    public Mono<Void> saveClassFeatures(List<ClassFeatureResult> classFeatures){
        return pf2eClassFeatureRepository.saveAll(classFeatures).then();
    }

    public Mono<Void> saveDeities(List<DeityResult> deities){
        return pf2eDeityRepository.saveAll(deities).then();
    }

    public Mono<Void> saveEquipment(List<EquipmentResult> equipment){
        return pf2eEquipmentRepository.saveAll(equipment).then();
    }

    public Mono<Void> saveFeats(List<FeatResult> feats){
        return pf2eFeatRepository.saveAll(feats).then();
    }

    public Mono<Void> saveSpells(List<SpellResult> spells){
        return pf2eSpellRepository.saveAll(spells).then();
    }


    private RuntimeException handleStatusCode(ClientResponse response){
       if (response.statusCode().is4xxClientError()){
            return new ClientException("CLIENT EXCEPTION in PF2eService.", response.rawStatusCode()) ;
        } else if (response.statusCode().is5xxServerError()){
            return new ServiceException("EXTERNAL SERVICE EXCEPTION in PF2eService.", response.rawStatusCode());
        } else {
            return new ServiceException("SERVICE EXCEPTION in PF2eService. Retrying...", response.rawStatusCode());
        }
    }

    private Mono<Void> handleServiceException(ServiceException e){
        log.error("Handling service exception in PF2eService." + e.getMessage() + " " + e.getCause() + " " + e.getStackTrace());
        return Mono.empty();
        //return recordException(e);
    }

    private Mono<Void> handleClientException(ClientException e){
        log.error("Handling client exception in PF2eService. " + e.getMessage() + " " + e.getCause() + " " + e.getStackTrace());
        return Mono.empty();
        //return recordException(e);
    }

    private Mono<Void> recordException(Exception e){
        System.out.println("Recording exception in PF2eService.");
        return exceptionService.recordExceptionEvent(buildExceptionEvent(e));
    }

    private ExceptionEvent buildExceptionEvent(Exception e){
        return ExceptionEvent.builder()
            .message(e.getMessage())
            .service("PF2eService")
            .exception(e.getClass().getSimpleName())
            .datetime(ZonedDateTime.now(ZoneOffset.UTC).toString()) //UTC timestamp as string
            .build();
    }
}
