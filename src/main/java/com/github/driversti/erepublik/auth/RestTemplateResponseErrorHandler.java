package com.github.driversti.erepublik.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

@Slf4j
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return serverError(response) || clientError(response);
  }

  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    if (serverError(response)) {
      final String text = parseBody(response);
      log.error(text);
      throw new RuntimeException("Server Error");
    }
    if (clientError(response)) {
      log.debug("CLIENT ERROR:\n\n{}", response.getStatusText());
      throw new RuntimeException("Client Error");
    }
  }

  private String parseBody(ClientHttpResponse response) throws IOException {
    return new BufferedReader(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))
        .lines()
        .collect(Collectors.joining("\n"));
  }

  private boolean serverError(ClientHttpResponse response) throws IOException {
    return response.getStatusCode().is5xxServerError();
  }

  private boolean clientError(ClientHttpResponse response) throws IOException {
    return response.getStatusCode().is4xxClientError();
  }
}
