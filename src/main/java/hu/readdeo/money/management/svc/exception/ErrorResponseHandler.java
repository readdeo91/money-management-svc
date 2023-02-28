package hu.readdeo.money.management.svc.exception;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorResponseHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler({ErrorResponse.class})
  public final ResponseEntity<String> apiError(ErrorResponse response) {
    JSONObject responseBody = new JSONObject();
    if (!ObjectUtils.isEmpty(response.getError())) responseBody.put("error", response.getError());
    if (!ObjectUtils.isEmpty(response.getErrorId()))
      responseBody.put("errorId", response.getErrorId());
    if (!ObjectUtils.isEmpty(response.getMessage()))
      responseBody.put("message", response.getMessage());
    return new ResponseEntity<>(responseBody.toString(), response.getHttpStatusCode());
  }
}
