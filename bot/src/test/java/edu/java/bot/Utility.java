package edu.java.bot;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class Utility {

    static Update createUpdate(String text) {
        Update update = Mockito.mock(Update.class);
        Chat chat = Mockito.mock(Chat.class);
        Message message = Mockito.mock(Message.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(message.chat().firstName()).thenReturn("user");

        when(message.text()).thenReturn(text);
        when(update.message().text()).thenReturn(text);
        return update;
    }
}
