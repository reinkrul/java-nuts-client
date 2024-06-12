package nl.reinkrul.nuts.v6;

import com.sun.net.httpserver.HttpServer;
import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.Configuration;
import nl.reinkrul.nuts.vdr.v2.DidApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VdrTest {

    private static HttpServer server;

    @BeforeAll
    public static void setup() throws IOException {
        int port;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
        }

        server = HttpServer.create(new InetSocketAddress("localhost", port), 10);
        server.createContext("/internal/vdr/v2/did/did:nuts:abcdefghijklmnop", exchange -> {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, 0);
                    try (InputStream inputStream = VdrTest.class.getResourceAsStream("/did-resolution-result.json")) {
                        exchange.getResponseBody().write(inputStream.readAllBytes());
                    }
                    exchange.close();
                }
        );
        server.setExecutor(null);
        server.start();
    }

    @AfterAll
    public static void tearDown() {
        server.stop(0);
    }

    @Test
    public void didDocument() throws ApiException {
        var apiClient = Configuration.getDefaultApiClient();
        apiClient.setBasePath("http://localhost:" + server.getAddress().getPort());
        var didApi = new DidApi(apiClient);

        var result = didApi.resolveDID("did:nuts:abcdefghijklmnop");

        assertEquals("did:nuts:abcdefghijklmnop", result.getDocument().getId().toString());
    }
}
