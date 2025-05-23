package com.example.memecommerceback.global.exception;

import com.example.memecommerceback.domain.files.exception.FileCustomException;
import com.example.memecommerceback.domain.users.exception.UserCustomException;
import com.example.memecommerceback.global.exception.ProfanityFilterCustomException;
import com.example.memecommerceback.global.exception.dto.CommonResponseDto;
import com.example.memecommerceback.global.exception.dto.Error;
import com.example.memecommerceback.global.exception.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleAccessDenied(
      AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                Error.AUTH_DENIED_ERROR.getCode(),
                Error.AUTH_DENIED_ERROR.getMessage()),
            HttpStatus.FORBIDDEN.getReasonPhrase() + " : "
                + ex.getMessage(),
            HttpStatus.FORBIDDEN.value()));
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleMissingPart(
      MissingServletRequestPartException ex) {

    String missingPart = ex.getRequestPartName();
    String detailMessage =
        "요청에 필요한 파트 '" + missingPart + "'가 포함되어 있지 않습니다.";

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                Error.MISSING_REQUEST_PART.getCode(), detailMessage),
            HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " +
                Error.MISSING_REQUEST_PART.getMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    // 요청 본문에 포함된 검증 오류들
    List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

    // 오류 메시지들을 하나의 문자열로 합침
    StringBuilder violationMessages = new StringBuilder();
    for (FieldError fieldError : fieldErrors) {
      violationMessages.append(fieldError.getDefaultMessage()).append(", ");
    }

    // 마지막에 추가된 ", " 제거
    if (!violationMessages.isEmpty()) {
      violationMessages.setLength(violationMessages.length() - 2);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(Error.INVALID_DTO_MAPPING_ERROR.getCode(),
                violationMessages.toString()),
            HttpStatus.BAD_REQUEST.getReasonPhrase() + " : "
                + Error.INVALID_DTO_MAPPING_ERROR.getMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleConstraintViolationException(
      ConstraintViolationException ex) {
    // 제약 조건 위반 상세 정보 추출
    Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

    // 위반된 제약 조건 메시지들을 하나의 문자열로 합침
    StringBuilder violationMessages = new StringBuilder();
    for (ConstraintViolation<?> violation : violations) {
      violationMessages.append(violation.getMessage()).append(", ");
    }

    // 마지막에 추가된 ", " 제거
    if (!violationMessages.isEmpty()) {
      violationMessages.setLength(violationMessages.length() - 2);
    }

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                Error.INVALID_PARAMETER_ERROR.getCode(),
                violationMessages.toString()),
            HttpStatus.BAD_REQUEST.getReasonPhrase() + " : "
                + Error.INVALID_PARAMETER_ERROR.getMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(DateTimeParseException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleDateTimeParse(
      DateTimeParseException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                Error.DATE_TIME_PARSE_ERROR.getCode(),
                "날짜 형식이 올바르지 않습니다: " + ex.getParsedString()),
            HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " +
                Error.DATE_TIME_PARSE_ERROR.getMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleInvalidEnum(Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                Error.INVALID_ENUM_VALUE.getCode(),
                "잘못된 입력을 하셨습니다. : " + e.getMessage()),
            HttpStatus.BAD_REQUEST.getReasonPhrase() + " : " +
                Error.INVALID_ENUM_VALUE.getMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleMaxSizeException(
      MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                Error.UPLOAD_EXCEED_FILE_SIZE.getCode(),
                ex.getMessage()),
            Error.UPLOAD_EXCEED_FILE_SIZE.getMessage(),
            HttpStatus.BAD_REQUEST.value()));
  }

  @ExceptionHandler(
      {UserCustomException.class, FileCustomException.class,
          ProfanityFilterCustomException.class})
  public ResponseEntity<CommonResponseDto<ErrorResponseDto>> handleCustomException(
      CustomException ex) {
    String category;
    if (ex instanceof UserCustomException) {
      category = "회원 오류";
    } else if (ex instanceof FileCustomException) {
      category = "파일 오류";
    } else if (ex instanceof ProfanityFilterCustomException) {
      category = "비속어 오류";
    } else {
      category = "기타 오류";
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new CommonResponseDto<>(
            ErrorResponseDto.of(
                ex.getErrorCode(), ex.getMessage()),
            category, HttpStatus.BAD_REQUEST.value()));
     }
}
