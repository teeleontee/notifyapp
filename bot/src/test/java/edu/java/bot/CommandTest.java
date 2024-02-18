package edu.java.bot;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.handler.UserMessageProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommandTest {

    @Autowired UserMessageProcessor processor;

    @Test
    public void testHelpCommand() {
        String expectedMessage ="""
        <b>Что умеет этот бот</b>

        <b><i>Трэкер ссылок</i></b>
        Добавь ссылку и бот Вам пришлет уведомление, когда появится по ссылке обновление!

        <b><i>Команды</i></b>
        /start - <i>Начать общение с ботом</i>
        /track - <i>Добавить url в список отслеживаемых ссылок</i>
        /untrack - <i>Убрать url из списка отслеживаемых ссылок</i>
        /list - <i>Показать список отслеживаемых ссылок</i>
        /help - <i>помощь</i>
        """.trim();
        testCommand("/help", expectedMessage);
    }

    @Test
    public void testStartCommand() {
        String expectedMessage =
                "Привет, <b>user</b>\nПросто отправь мне ссылку " +
                    "через команду /track , чтобы получать уведомление в случае обновлений";
        testCommand("/start", expectedMessage);
    }

    @Test
    public void testTrackCommand() {
        String expectedMessage = "Пришлите ссылку, которую Вы желаете <b>добавить</b> ответом на это сообщение";
        testCommand("/track", expectedMessage);
    }

    @Test
    public void testUntrackCommand() {
        String expectedMessage = "Пришлите ссылку, которую вы желаете <b>удалить</b> из списка отслеживемых ссылок";
        testCommand("/untrack", expectedMessage);
    }

    @Test
    public void testUnknownCommands() {
        String expectedMessage = "<i>Неизвестная команда, возможно Вы опечатались</i>";
        String[] badCommands = {"/bad1", "/unknown", "/test", "/track random"};
        for (String cmd : badCommands) {
            testCommand(cmd, expectedMessage);
        }
    }

    private void testCommand(String command, String expected) {
        SendMessage sendMessage = processor.process(Utility.createUpdate(command));
        String helpMessage = sendMessage.getParameters().get("text").toString();
        Assertions.assertEquals(helpMessage, expected);
    }
}
