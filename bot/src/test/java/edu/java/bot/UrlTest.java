package edu.java.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.AddLinkCommand;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.RemoveLinkCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UrlTest {

    @Autowired AddLinkCommand addLinkCommand;

    @Autowired RemoveLinkCommand removeLinkCommand;

    private <E extends Command> void testUrl(E command, String url, String expected) {
        Update update = Utility.createUpdate(url);
        SendMessage sendMessage = command.handle(update);
        String helpMessage = sendMessage.getParameters().get("text").toString();
        Assertions.assertEquals(helpMessage, expected);
    }

    @Test
    public void testValidUrls() {
        String[] validUrls = {
            "https://github.com/pengrad/java-telegram-bot-api?tab=readme-ov-file#inline-mode"
            , "https://core.telegram.org/bots/features#inline-requests"
            , "https://core.telegram.org/bots/api#inlinekeyboardmarkup"
            , "https://stackoverflow.com/questions/59341135/telegram-bot-process-user-response-based-on-last-bot-question"
            , "https://chat2desk.com/en/knowledge-base/messenger-configuration/how-to-change-a-bots-description-in-telegram"
            , "https://www.baeldung.com/mockito-annotations"
        };
        for (String url : validUrls) {
            String expectedAdd = String.format("Успешно добавили %s в список отслеживемых ссылок", url);
            String expectedDelete = String.format("Успешно удалили %s из списка отслеживемых ссылок", url);
            testUrl(addLinkCommand, url, expectedAdd);
            testUrl(removeLinkCommand, url, expectedDelete);
        }
    }

    @Test
    public void testInvalidUrls() {
        String expected = "Ошибка в введенном url, возможно, Вы опечатались";
        String[] invalidUrls = { "http//example.com"
            , "https://example.com/path\\with\\backslashes"
            , "file:///C|/path/to/file.txt"
            , "tel:123-456-7890"
            , "http://example.com%\n0amaliciouscode"
        };
        for (String url : invalidUrls) {
            testUrl(addLinkCommand, url, expected);
            testUrl(removeLinkCommand, url, expected);
        }
    }
}
