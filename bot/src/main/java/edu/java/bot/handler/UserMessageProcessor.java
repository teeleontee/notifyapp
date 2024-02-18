package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.Command;
import java.util.List;
import java.util.Map;

public interface UserMessageProcessor {
    List<? extends Command> commands();

    SendMessage process(Update update);
}
