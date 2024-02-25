package edu.java.bot.utility;

import com.pengrad.telegrambot.model.Update;

public final class Utility {

    private Utility() {
    }

    public static boolean isMessageReply(Update update) {
        return update.message().replyToMessage() != null;
    }

    public static long getChatId(Update update) {
        return update.message().chat().id();
    }

    public static String getTextFromUpdate(Update update) {
        return update.message().text();
    }

    public static String getTextFromReplyMessage(Update update) {
        return update.message().replyToMessage().text();
    }

}
