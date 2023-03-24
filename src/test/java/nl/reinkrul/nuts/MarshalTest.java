package nl.reinkrul.nuts;

import com.sun.net.httpserver.HttpServer;
import nl.reinkrul.nuts.vdr.DidApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MarshalTest {

    private static HttpServer server;

    @BeforeAll
    public static void setup() throws IOException {
        int port;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
        }

        server = HttpServer.create(new InetSocketAddress("localhost", port), 10);
        server.createContext("/internal/vdr/v1/did/did:nuts:abcdefghijklmnop", exchange -> {
                    exchange.sendResponseHeaders(200, 0);
                    try (InputStream inputStream = MarshalTest.class.getResourceAsStream("/did-resolution-result.json")) {
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
        var apiClient = new ApiClient();
        apiClient.updateBaseUri("http://localhost:" + server.getAddress().getPort());
        var didApi = new DidApi(apiClient);

        var result = didApi.getDID("did:nuts:abcdefghijklmnop", null, null);

        assertEquals("did:nuts:abcdefghijklmnop", result.getDocument().getId().toString());
    }
}
