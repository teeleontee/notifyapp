package edu.java.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.commands.AddLinkCommand;
import edu.java.bot.commands.Command;
import edu.java.bot.commands.HelpCommand;
import edu.java.bot.commands.ListCommand;
import edu.java.bot.commands.RemoveLinkCommand;
import edu.java.bot.commands.StartCommand;
import edu.java.bot.commands.TrackCommand;
import edu.java.bot.commands.UntrackCommand;
import edu.java.bot.dao.DataAccess;
import java.util.List;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.Utility.getChatId;
import static edu.java.bot.utility.Utility.getTextFromReplyMessage;
import static edu.java.bot.utility.Utility.isMessageReply;

@Component
public class UserMessageHandler implements UserMessageProcessor {
    private final DataAccess dao;
    private final List<? extends Command> commands;

    public UserMessageHandler(DataAccess dataAccess) {
        this.dao = dataAccess;
        this.commands = List.of(
            new HelpCommand(),
            new StartCommand(),
            new TrackCommand(),
            new UntrackCommand(),
            new ListCommand(this.dao)
        );
    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        if (isMessageReply(update)) {
            return processRepliedMessage(update);
        }
        long chatId = getChatId(update);
        Command command = getCommandOnUpdate(update);
        if (command != null) {
            return command.handle(update);
        }
        return new SendMessage(chatId, invalidCommand)
            .parseMode(ParseMode.HTML);
    }

    private SendMessage processRepliedMessage(Update update) {
        long chatId = getChatId(update);
        String botText = getTextFromReplyMessage(update);
        Command cmd = null;
        if (botText.contains("добавить")) {
            cmd = new AddLinkCommand(dao);
        }
        if (botText.contains("удалить")) {
            cmd = new RemoveLinkCommand(dao);
        }
        if (cmd != null) {
            return cmd.handle(update);
        }
        return new SendMessage(chatId, invalidCommand)
            .parseMode(ParseMode.HTML);
    }

    private Command getCommandOnUpdate(Update update) {
        for (Command cmd : commands) {
            if (cmd.supports(update)) {
                return cmd;
            }
        }
        return null;
    }

    private final String invalidCommand = "<i>Неизвестная команда, возможно Вы опечатались</i>";
}
