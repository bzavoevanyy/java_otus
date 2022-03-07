package ru.otus.server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.UserAuthService;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static ru.otus.server.utils.WebServerHelper.*;

@DisplayName("WebServer test should")
class ClientWebServerWithFilterBasedSecurityTest {

    private static final int WEB_SERVER_PORT = 8989;
    private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
    private static final String LOGIN_URL = "login";

    private static final long DEFAULT_USER_ID = 1L;
    private static final String DEFAULT_USER_LOGIN = "admin";
    private static final String DEFAULT_USER_PASSWORD = "123456";

    private static final Long DEFAULT_CLIENT_ID = 1L;
    private static final Client DEFAULT_CLIENT = new Client(DEFAULT_CLIENT_ID, "username", null, null);
    private static final String INCORRECT_USER_LOGIN = "BadUser";

    private static ClientWebServer webServer;
    private static HttpClient http;

    @BeforeEach
    void setUp() throws Exception {
        http = HttpClient.newHttpClient();

        DBServiceClient dbServiceClient = mock(DBServiceClient.class);
        TemplateProcessor templateProcessor = mock(TemplateProcessor.class);

        UserAuthService userAuthService = mock(UserAuthService.class);

        given(userAuthService.authenticate(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(true);
        given(userAuthService.authenticate(INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(false);
        given(dbServiceClient.getClient(DEFAULT_USER_ID)).willReturn(Optional.of(DEFAULT_CLIENT));
        given(templateProcessor.getPage(anyString(), any())).willReturn("ok");
        webServer = new ClientWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, dbServiceClient,
                templateProcessor, userAuthService);
        webServer.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        webServer.stop();
    }

    @DisplayName(" return 302 when /client/$id without authorization")
    @Test
    void shouldReturnForbiddenStatusForClientRequestWhenUnauthorized() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, "client/1")))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_MOVED_TEMP);
    }

    @DisplayName(" return session id when user data is correct")
    @Test
    void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();
    }

    @DisplayName(" not return session id when user data is not correct")
    @Test
    void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNull();
    }

    @DisplayName(" return status 200 and correct data when authorized")
    @Test
    void shouldReturnCorrectClientWhenAuthorized() throws Exception {
        HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
        assertThat(jSessionIdCookie).isNotNull();

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(buildUrl(WEB_SERVER_URL, "client", String.valueOf(DEFAULT_USER_ID))))
                .setHeader(COOKIE_HEADER, String.format("%s=%s", jSessionIdCookie.getName(), jSessionIdCookie.getValue()))
                .build();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        assertThat(response.statusCode()).isEqualTo(HttpURLConnection.HTTP_OK);
        assertThat(response.body().trim()).isEqualTo("ok");
    }
}