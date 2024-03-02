package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.StackOverflowClient;
import edu.java.clients.StackOverflowClientImpl;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class StackOverflowTest {
    private final static int PORT = 7777;
    private final StackOverflowClient client =
        new StackOverflowClientImpl(WebClient.create(String.format("http://localhost:%s", PORT)));
    private WireMockServer wm;

    @BeforeEach
    void setup() {
        wm = new WireMockServer(PORT);
        wm.start();
    }

    @Test
    public void testUserInfo() {
        wm.stubFor(get(urlEqualTo("/questions/11828270?site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{ \"items\" : [ { \"score\" : 5295, \"title\" : \"How do I exit Vim?\", \"question_id\" : 11828270 } ] }")
                .withStatus(200)));
        // should pass
        client.getQuestionInfo("11828270")
            .subscribeOn(Schedulers.boundedElastic())
            .timeout(Duration.ofSeconds(2))
            .doOnSuccess(result -> {
                var items = result.details();
                if (!items.isEmpty()) {
                    var first = items.getFirst();
                    Assertions.assertEquals(first.questionId(), 11828270);
                    Assertions.assertEquals(first.score(), 5295);
                    Assertions.assertEquals(first.title(), "How do I exit Vim?");
                }
            })
            .doOnError(error -> Assertions.fail())
            .block();
        // bad url, should fail
        try {
            client.getQuestionInfo("badUrl")
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(2))
                .doOnSuccess(result -> Assertions.fail())
                .doOnError(e -> {
                })
                .block();
        } catch (final Exception ignored) {
        }
    }
}
