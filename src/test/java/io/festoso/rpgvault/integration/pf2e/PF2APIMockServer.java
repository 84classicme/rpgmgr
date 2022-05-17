package io.festoso.rpgvault.integration.pf2e;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class PF2APIMockServer {

    public static final String WIREMOCK_PORT = "8086";
    private static final WireMockServer wireMockServer =
        new WireMockServer(WireMockConfiguration.options().port(Integer.parseInt(WIREMOCK_PORT)));

    public static WireMockServer setupWireMock() {
        if (!wireMockServer.isRunning()) {
            wireMockServer.start();
            WireMock.configureFor("localhost", Integer.parseInt(WIREMOCK_PORT));
        }
        return wireMockServer;
    }
}
