package hu.readdeo.money.management.svc.exception;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class ErrorResponse extends RuntimeException {
  private String error;
  private String errorId;
  private String message;
  private String detail;
  private HttpStatus httpStatusCode;

  public ErrorResponse(HttpStatus httpStatusCode) {
    this.httpStatusCode = httpStatusCode;
  }

  public ErrorResponse(String msg, HttpStatus httpStatusCode) {
    this.message = msg;
    this.httpStatusCode = httpStatusCode;
  }

  public ErrorResponse(String msg, UUID errorId, HttpStatus httpStatusCode) {
    this.message = msg;
    this.errorId = errorId.toString();
    this.httpStatusCode = httpStatusCode;
  }
}
