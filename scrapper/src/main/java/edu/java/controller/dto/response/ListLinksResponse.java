package edu.java.controller.dto.response;

import java.util.List;

public record ListLinksResponse(int size, List<LinkResponse> links) {
}
