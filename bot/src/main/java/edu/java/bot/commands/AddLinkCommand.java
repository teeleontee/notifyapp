package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.DataAccess;
import org.springframework.stereotype.Component;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import static edu.java.bot.utility.Utility.getChatId;
import static edu.java.bot.utility.Utility.getTextFromUpdate;

/**
 * Private command for adding Urls to Dao
 */
@Component
public class AddLinkCommand implements Command {

    private final DataAccess dao;

    public AddLinkCommand(DataAccess dao) {
        this.dao = dao;
    }

    @Override
    public String command() {
        return "add";
    }

    @Override
    public String description() {
        return "добавляет ссылку в бд";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = getChatId(update);
        String link = getTextFromUpdate(update);
        try {
            URI url = new URL(link).toURI();
            dao.addLink(chatId, url);
            return new SendMessage(
                chatId,
                String.format("Успешно добавили %s в список отслеживемых ссылок", url));
        } catch (URISyntaxException | MalformedURLException e) {
            return new SendMessage(chatId, "Ошибка в введенном url, возможно, Вы опечатались");
        }
    }
}
