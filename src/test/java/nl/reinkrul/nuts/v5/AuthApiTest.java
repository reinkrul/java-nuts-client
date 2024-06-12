package nl.reinkrul.nuts.v5;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import nl.reinkrul.nuts.ApiClient;
import nl.reinkrul.nuts.ApiException;
import nl.reinkrul.nuts.Configuration;
import nl.reinkrul.nuts.auth.v1.AuthApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.jupiter.api.Assertions.*;

public class AuthApiTest {

    private static HttpServer server;

    private static Headers actualRequestHeaders;
    private static byte[] actualRequestBody;

    @BeforeAll
    public static void setup() throws IOException {
        int port;
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            port = serverSocket.getLocalPort();
        }

        server = HttpServer.create(new InetSocketAddress("localhost", port), 10);
        server.createContext("/internal/auth/v1/accesstoken/introspect", exchange -> {
                    actualRequestHeaders = exchange.getRequestHeaders();
                    actualRequestBody = exchange.getRequestBody().readAllBytes();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, 0);
                    exchange.getResponseBody().write("{\"sub\": \"admin\"}".getBytes());
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
    public void introspectAccessToken() throws ApiException {
        var client = Configuration.getDefaultApiClient();
        client.setBasePath("http://" + server.getAddress().getHostName() + ":" + server.getAddress().getPort());
        var authApi = new AuthApi(client);
        var response = authApi.introspectAccessToken("some-token");

        assertEquals("token=some-token", new String(actualRequestBody));
        assertEquals("admin", response.getSub());
    }
}
