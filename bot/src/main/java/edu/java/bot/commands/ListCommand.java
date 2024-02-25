package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dao.DataAccess;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import static edu.java.bot.utility.Utility.getChatId;

@Component
public class ListCommand implements Command {

    private final DataAccess dao;

    public ListCommand(DataAccess access) {
        this.dao = access;
    }

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = getChatId(update);
        var array = getButtonArray(chatId);
        if (array != null) {
            return new
                SendMessage(chatId, "Текущий список ссылок")
                .replyMarkup(new InlineKeyboardMarkup(array));
        }
        return new
            SendMessage(chatId, "Текущий список ссылок пуст");
    }

    private InlineKeyboardButton[] getButtonArray(long chatId) {
        var list = dao.getList(chatId);
        if (list == null) {
            return null;
        }
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        for (Object url : list) {
            String uri = (String) url;
            buttons.add(new InlineKeyboardButton(uri).url(uri));
        }
        InlineKeyboardButton[] array = new InlineKeyboardButton[buttons.size()];
        return buttons.toArray(array);
    }

}
