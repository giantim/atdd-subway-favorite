package wooteco.subway.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wooteco.subway.service.favorite.exception.DuplicateFavoriteException;
import wooteco.subway.service.member.dto.ErrorResponse;
import wooteco.subway.service.member.exception.DuplicateEmailException;
import wooteco.subway.service.member.exception.InvalidMemberEmailException;
import wooteco.subway.service.member.exception.InvalidMemberIdException;

@ControllerAdvice
public class ExceptionController {
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "예기치 못한 에러가 발생했습니다.\n관리자에게 문의해 주세요.";
    private static final Logger LOGGER = LogManager.getLogger("ExceptionController");

    @ExceptionHandler(value = {DuplicateEmailException.class, InvalidMemberIdException.class,
            InvalidMemberEmailException.class, DuplicateFavoriteException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> getDuplicateKeyException(RuntimeException exception) {
        LOGGER.error("\n-----------에러가 발생했습니다------------\n{}", exception.getMessage(), exception);
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> getInternalServerException(Exception exception) {
        LOGGER.error("\n-----------에러가 발생했습니다------------\n{}", exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResponse(INTERNAL_SERVER_ERROR_MESSAGE), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
