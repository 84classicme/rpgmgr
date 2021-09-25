package io.festerson.rpgvault;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

@Slf4j
@Configuration
public class MdcConfig  implements WebFilter{ //implements WebFilter

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestId = getTraceId(request.getHeaders());
        return chain
            .filter(exchange)
            .doOnEach(logOnEach(r -> log.info("{} {}", request.getMethod(), request.getURI())))
            .contextWrite(Context.of("X-B3-TraceId", requestId));
    }

    private String getTraceId(HttpHeaders headers) {
        List<String> requestIdHeaders = headers.get("X-B3-TraceId");
        return requestIdHeaders == null || requestIdHeaders.isEmpty()
            ? ""
            : requestIdHeaders.get(0);
    }

    public static <T> Consumer<Signal<T>> logOnEach(Consumer<T> logStatement) {
        return signal -> {
            Optional<String> username = signal.getContext().getOrEmpty("USER");
            Optional<String> requesturi = signal.getContext().getOrEmpty("X-B3-TraceId");
            MDC.putCloseable("USER", username.orElse(""));
            MDC.putCloseable("X-B3-TraceId", requesturi.orElse(""));
            logStatement.accept(signal.get());
        };
    }

    public static <T> Consumer<Signal<T>> logOnNext(Consumer<T> logStatement) {
        return signal -> {
            if (!signal.isOnNext()) return;
            Optional<String> username = signal.getContext().getOrEmpty("USER");
            Optional<String> requesturi = signal.getContext().getOrEmpty("X-B3-TraceId");
            MDC.putCloseable("USER", username.orElse(""));
            MDC.putCloseable("X-B3-TraceId", requesturi.orElse(""));
            logStatement.accept(signal.get());
            //signal.getContextView()
        };
    }

    private static <T> void putMdcValue(String key, Signal<T> signal, Consumer<T> logStatement){
        Optional<String> value = signal.getContext().getOrEmpty(key);
        value.ifPresentOrElse(tpim -> {
                try (MDC.MDCCloseable cMdc = MDC.putCloseable(key, tpim)) {
                    logStatement.accept(signal.get());
                }
            },
            () -> logStatement.accept(signal.get()));
    }
}