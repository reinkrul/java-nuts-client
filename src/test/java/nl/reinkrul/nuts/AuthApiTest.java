package nl.reinkrul.nuts;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import nl.reinkrul.nuts.auth.AuthApi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
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
        var client = new ApiClient();
        client.updateBaseUri("http://" + server.getAddress().getHostName() + ":" + server.getAddress().getPort());
        var authApi = new AuthApi(client);
        var response = authApi.introspectAccessToken("some-token");

        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", actualRequestHeaders.getFirst("Content-Type"));
        assertEquals("token=some-token", new String(actualRequestBody));
        assertEquals("admin", response.getSub());
    }
}
