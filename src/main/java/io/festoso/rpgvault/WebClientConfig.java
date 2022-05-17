package io.festoso.rpgvault;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private static final int APPLICATION_TIMEOUT_MILLIS = 10000;

    @Bean
    public WebClient getReactiveRestClient(){
        TcpClient tcpClient = TcpClient.create();

        tcpClient
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, APPLICATION_TIMEOUT_MILLIS)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(APPLICATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(APPLICATION_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS));
            });

        WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient).wiretap(true)))
            .build();

        return webClient;
    }

    @Bean
    public WebClient getReactivePF2eClient(){
        TcpClient tcpClient = TcpClient.create();

        tcpClient
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, APPLICATION_TIMEOUT_MILLIS)
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(APPLICATION_TIMEOUT_MILLIS * 50, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(APPLICATION_TIMEOUT_MILLIS * 50, TimeUnit.MILLISECONDS));
            });

        WebClient webClient = WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))
            .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient).wiretap(true)))
            .defaultHeader("Authorization", "4c79b5ad-260f-4bcb-bf7a-0d55a1e0e17a")
            .build();

        return webClient;
    }

}
