package edu.java.controller;

import edu.java.dao.TgChatService;
import edu.java.exceptions.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class TelegramChatController {

    private final TgChatService service;

    public TelegramChatController(TgChatService service) {
        this.service = service;
    }

    @Operation(summary = "Зарегистрировать чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат зарегистрирован", content = {
            @Content(schema = @Schema(hidden = true))
        }),
    })
    @PostMapping("/{id}")
    public ResponseEntity<?> registerChat(@PathVariable long id) {
        service.register(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Удалить чат")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Чат успешно удален", content = {
            @Content(schema = @Schema(hidden = true))
        }),
        @ApiResponse(responseCode = "404", description = "Чат не существует", content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                     schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteChat(@PathVariable long id) {
        service.unregister(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
