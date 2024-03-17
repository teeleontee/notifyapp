package edu.java.controller;

import edu.java.controller.dto.request.AddLinkRequest;
import edu.java.controller.dto.request.RemoveLinkRequest;
import edu.java.controller.dto.response.LinkResponse;
import edu.java.controller.dto.response.ListLinksResponse;
import edu.java.dao.LinkService;
import edu.java.exceptions.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController {

    private final LinkService service;

    public LinksController(LinkService service) {
        this.service = service;
    }

    @Operation(summary = "Получить все отслеживаемые ссылки")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены")
    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long id) {
        List<LinkResponse> list = service.listAll(id)
            .stream()
            .map(link -> new LinkResponse(id, link.url()))
            .toList();
        ListLinksResponse response = new ListLinksResponse(list.size(), list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Добавить отслеживание ссылки")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") long id,
        @RequestBody AddLinkRequest request
    ) {
        service.add(id, request.link());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Убрать отслеживание ссылки")
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана"),
        @ApiResponse(responseCode = "404", description = "Ссылка не найдена", content = {
            @Content(schema = @Schema(implementation = ApiErrorResponse.class))
        })
    })
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") long id,
        @RequestBody RemoveLinkRequest request
    ) {
        service.remove(id, request.link());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
