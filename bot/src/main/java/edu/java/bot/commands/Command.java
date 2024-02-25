package edu.java.bot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Objects;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.Utility.getTextFromUpdate;

@Component
public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        String text = getTextFromUpdate(update);
        return Objects.equals(text, command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
