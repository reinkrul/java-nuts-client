package nl.reinkrul.nuts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import nl.reinkrul.nuts.common.DIDDocument;
import nl.reinkrul.nuts.vdr.DIDCreateRequest;
import nl.reinkrul.nuts.vdr.DidApi;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class ApiClientTest {

    private static final String PATH = "/internal/vdr/v1/did";
    private static final String BEARER_TOKEN = "some-token";
    private static HttpServer server;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", freePort()), 0);
        server.start();
    }

    @AfterAll
    static void stopServer() {
        server.stop(5);
    }

    @AfterEach
    void unregisterContext() {
        server.removeContext(PATH);
    }

    @Test
    public void noAuthTest() throws ApiException {
        server.createContext(PATH, ApiClientTest::sendOKResponse);

        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath("http://" + server.getAddress().toString());

        var didApi = new DidApi(apiClient);
        didApi.createDID(new DIDCreateRequest());
    }

    @Test
    public void bearerAuthTest() throws ApiException {
        server.createContext(PATH, exchange -> {
            var header = exchange.getRequestHeaders().get("Authorization").get(0);
            if (header.equals("Bearer " + BEARER_TOKEN)) {
                sendOKResponse(exchange);
            } else {
                // Fails test
                exchange.sendResponseHeaders(401, 0);
                exchange.close();
            }
        });

        ApiClient apiClient = new ApiClient();
        apiClient.setBearerToken(BEARER_TOKEN);
        apiClient.setBasePath("http://" + server.getAddress().toString());

        var didApi = new DidApi(apiClient);
        didApi.createDID(new DIDCreateRequest());
    }

    private static void sendOKResponse(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.getResponseBody().write(new ObjectMapper().writeValueAsBytes(new DIDDocument()));
        exchange.close();
    }

    private static int freePort() {
        try (ServerSocket s = new ServerSocket(0)) {
            return s.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
