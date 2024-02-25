package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.Utility.getChatId;

@Component
public class HelpCommand implements Command {

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Помощь";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = getChatId(update);
        return new SendMessage(chatId, """
            <b>Что умеет этот бот</b>

            <b><i>Трэкер ссылок</i></b>
            Добавь ссылку и бот Вам пришлет уведомление, когда появится по ссылке обновление!

            <b><i>Команды</i></b>
            /start - <i>Начать общение с ботом</i>
            /track - <i>Добавить url в список отслеживаемых ссылок</i>
            /untrack - <i>Убрать url из списка отслеживаемых ссылок</i>
            /list - <i>Показать список отслеживаемых ссылок</i>
            /help - <i>помощь</i>
            """.trim())
            .parseMode(ParseMode.HTML);
    }
}
