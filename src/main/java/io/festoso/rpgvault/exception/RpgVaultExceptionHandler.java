package io.festoso.rpgvault.exception;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@CommonsLog
@Component
@Order(-2)
public class RpgVaultExceptionHandler extends AbstractErrorWebExceptionHandler {

    @Autowired
    @Lazy
    RpgVaultErrorAttributes rpgVaultErrorAttributes;

    public RpgVaultExceptionHandler(RpgVaultErrorAttributes rpgVaultErrorAttributes, ApplicationContext applicationContext,
                                    ServerCodecConfigurer serverCodecConfigurer) {
        super(rpgVaultErrorAttributes, new ResourceProperties(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
        this.rpgVaultErrorAttributes = rpgVaultErrorAttributes;
    }


    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(final ServerRequest request) {

        final Map<String, Object> errorPropertiesMap = getErrorAttributes(request, false);

        log.error("START:renderErrorResponse: " + errorPropertiesMap.get("message"));

        if(errorPropertiesMap.containsKey("errors")) {
            StringBuilder errorMessages = new StringBuilder();
            List<FieldError> fieldErrors = (List) errorPropertiesMap.get("errors");
            fieldErrors.forEach(e -> errorMessages.append(e.getField() + " " + e.getDefaultMessage() + "."));
            errorPropertiesMap.remove("errors");
            log.info("Adding validation messages.");
            errorPropertiesMap.replace("message", errorMessages.toString());
        }
        log.error("END:renderErrorResponse");
        return ServerResponse
            .status((Integer)errorPropertiesMap.get("status"))
            .body(BodyInserters.fromObject(errorPropertiesMap));
    }

}
