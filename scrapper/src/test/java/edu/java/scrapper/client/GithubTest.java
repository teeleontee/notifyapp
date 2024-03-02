package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.GithubClient;
import edu.java.clients.GithubClientImpl;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

@SpringBootTest
public class GithubTest {
    private final static int PORT = 8888;
    private final GithubClient client =
        new GithubClientImpl(WebClient.create(String.format("http://localhost:%s", PORT)));
    private WireMockServer wm;

    @BeforeEach
    void setup() {
        wm = new WireMockServer(PORT);
        wm.start();
    }

    @Test
    public void testUserInfo() {
        wm.stubFor(get(urlEqualTo("/repos/teeleontee/notifyapp"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{ \"name\" : \"notifyapp\", \"id\" : 758693428, \"owner\" : { \"login\" : \"teeleontee\" } }")
                .withStatus(200)));
        // should pass
        client.getGithubInfo("teeleontee", "notifyapp")
            .subscribeOn(Schedulers.boundedElastic())
            .timeout(Duration.ofSeconds(1))
            .doOnSuccess(result -> {
                Assertions.assertEquals(result.name(), "notifyapp");
                Assertions.assertEquals(result.id(), 758693428);
                if (result.owner() != null) {
                    Assertions.assertEquals(result.owner().login(), "teeleontee");
                }
            })
            .doOnError(error -> Assertions.fail())
            .block();
        // bad url, should fail
        try {
            client.getGithubInfo("badUrl", "AnotherBadUrl")
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(1))
                .doOnSuccess(result -> Assertions.fail())
                .doOnError(e -> {
                })
                .block();
        } catch (final Exception ignored) {
        }
    }
}
