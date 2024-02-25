package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.Utility.getChatId;

@Component
public class TrackCommand implements Command {

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Добавить url в список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = getChatId(update);
        return new SendMessage(chatId,
            "Пришлите ссылку, которую Вы желаете <b>добавить</b> ответом на это сообщение")
            .parseMode(ParseMode.HTML)
            .replyMarkup(new ForceReply());
    }
}
