package edu.java.bot.controller;

import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.UpdateLinkRequest;
import edu.java.bot.service.Bot;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BotController {

    private final Bot notifyBot;

    public BotController(Bot notifyBot) {
        this.notifyBot = notifyBot;
    }

    @PostMapping("/updates")
    public ResponseEntity<?> postUpdates(@RequestBody UpdateLinkRequest request) {
        String message = "Attention, " + request.uri() + " has been updated\n\n" + request.description();
        request.tgChatIds().forEach(id -> {
            SendMessage req = new SendMessage(id, message);
            notifyBot.execute(req);
        });
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
