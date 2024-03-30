package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.clients.GithubClient;
import edu.java.clients.GithubClientImpl;
import edu.java.clients.StackOverflowClient;
import edu.java.clients.StackOverflowClientImpl;
import java.time.Duration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ClientsTest {
    private final static int PORT_STACKOVERFLOW = 7777;
    private final static int PORT_GITHUB = 9999;
    private final StackOverflowClient stackOverflowClient =
        new StackOverflowClientImpl(WebClient.create(String.format("http://localhost:%s", PORT_STACKOVERFLOW)));

    private final GithubClient client =
        new GithubClientImpl(WebClient.create(String.format("http://localhost:%s", PORT_GITHUB)));

    @Test
    public void testStackOverflow() {
        WireMockServer stackOverflowServer = new WireMockServer(PORT_STACKOVERFLOW);
        stackOverflowServer.start();
        stackOverflowServer.stubFor(get(urlEqualTo("/questions/11828270?site=stackoverflow"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody(
                    "{ \"items\" : [ { \"score\" : 5295, \"title\" : \"How do I exit Vim?\", \"question_id\" : 11828270 } ] }")
                .withStatus(200)));
        // should pass
        stackOverflowClient.getQuestionInfo("11828270")
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
            stackOverflowClient.getQuestionInfo("badUrl")
                .subscribeOn(Schedulers.boundedElastic())
                .timeout(Duration.ofSeconds(2))
                .doOnSuccess(result -> Assertions.fail())
                .doOnError(e -> {
                })
                .block();
        } catch (final Exception ignored) {
        }
    }

    @Test
    public void testGithub() {
        WireMockServer githubServer = new WireMockServer(PORT_GITHUB);
        githubServer.start();
        githubServer.stubFor(get(urlEqualTo("/repos/teeleontee/notifyapp"))
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
