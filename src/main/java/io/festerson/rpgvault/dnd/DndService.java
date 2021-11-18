package io.festerson.rpgvault.dnd;

import io.festerson.rpgvault.domain.dnd.*;
import io.festerson.rpgvault.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@Slf4j
public class DndService {

    @Autowired
    private ExceptionService exceptionService;

    @Autowired
    private DndRaceRepository dndRaceRepository;

    @Autowired
    private DndClassRepository dndClassRepository;

    @Autowired
    private DndSkillRepository dndSkillRepository;

    @Autowired
    private DndBackgroundRepository dndBackgroundRepository;

    @Autowired
    private DndLanguageRepository dndLanguageRepository;

    @Autowired
    private DndProficiencyRepository dndProficiencyRepository;

    @Autowired
    private DndAlignmentRepository dndAlignmentRepository;

    public Flux<Race> getAllRaces(){
        return dndRaceRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<CClass> getAllClasses(){
        return dndClassRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Background> getAllBackgrounds(){
        return dndBackgroundRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Skill> getAllSkills(){
        return dndSkillRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Proficiency> getAllProficiencies(){
        return dndProficiencyRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Proficiency> getProficienciesByType(String type){
        return dndProficiencyRepository.findAllByType(type).onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Language> getAllLanguages(){
        return dndLanguageRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
    }

    public Flux<Alignment> getAllAlignments(){
        return dndAlignmentRepository.findAll().onErrorResume(t -> Mono.error(handleException(t)));
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

    private RpgMgrException handleException(Throwable t){
        log.error("Exception in DndService:", t);
        return new RpgMgrException(t);
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
            .service("DndService")
            .exception(e.getClass().getSimpleName())
            .datetime(ZonedDateTime.now(ZoneOffset.UTC).toString()) //UTC timestamp as string
            .build();
    }
}
