package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component public class StartCommand implements Command {

    @Override public String command() {
        return "/start";
    }

    @Override public String description() {
        return "Начать общение с Notify Bot";
    }

    @Override public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        String user = update.message().chat().firstName();
        return new SendMessage(
            chatId,
            String.format("Привет, <b>%s</b>\nПросто отправь мне ссылку "
                + "через команду /track , чтобы получать уведомление в случае обновлений", user)
        ).parseMode(ParseMode.HTML);
    }
}
