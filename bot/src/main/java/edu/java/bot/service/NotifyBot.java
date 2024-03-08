package edu.java.bot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.dao.DataAccess;
import edu.java.bot.handler.UserMessageProcessor;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NotifyBot implements Bot {
    private final TelegramBot bot;

    private final UserMessageProcessor handler;

    private final DataAccess dataAccess;

    public NotifyBot(
        @Qualifier("applicationConfig") ApplicationConfig config,
        UserMessageProcessor handler,
        DataAccess dataAccess
    ) {
        bot = new TelegramBot(config.getTelegramToken());
        this.handler = handler;
        this.dataAccess = dataAccess;
        setup();
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse>
    void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            if (update.message() != null) {
                SendMessage message = handler.process(update);
                execute(message);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void setup() {
        bot.setUpdatesListener(this, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                System.err.println(e.getMessage());
            }
        });
    }

    public void close() {
        bot.shutdown();
    }
}
