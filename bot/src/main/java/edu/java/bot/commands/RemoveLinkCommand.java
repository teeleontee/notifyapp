package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.DataAccess;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.Utility.getChatId;
import static edu.java.bot.utility.Utility.getTextFromUpdate;

@Component
public class RemoveLinkCommand implements Command {

    private final DataAccess dao;

    public RemoveLinkCommand(DataAccess dao) {
        this.dao = dao;
    }

    @Override
    public String command() {
        return "remove";
    }

    @Override
    public String description() {
        return "Удаляет url из бд";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = getChatId(update);
        String link = getTextFromUpdate(update);
        try {
            URI url = new URL(link).toURI();
            boolean removed = dao.removeLink(chatId, url);
            if (removed) {
                return new SendMessage(
                    chatId,
                    String.format("Успешно удалили %s из списка отслеживемых ссылок", url)
                );
            }
            return new SendMessage(chatId, "Не удалось удалить из списка url, возможно, его там и не было");
        } catch (URISyntaxException | MalformedURLException e) {
            return new SendMessage(chatId, "Ошибка в введенном url, возможно, Вы опечатались");
        }
    }
}
