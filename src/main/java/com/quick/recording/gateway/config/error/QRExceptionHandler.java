package com.quick.recording.gateway.config.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.recording.gateway.config.MessageUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@RequiredArgsConstructor
public class QRExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    private final ObjectMapper jacksonObjectMapper;
    private final MessageUtil messageUtil;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(messageUtil.create("error.validation.field", fieldName, errorMessage));
        });
        String messageError = messageUtil.create("error.validation.class",
                ex.getParameter().getExecutable().getDeclaringClass().getSimpleName(),
                ex.getParameter().toString(),
                ex.getParameter().getParameter().toString()
        );
        ApiError build = ApiError.builder().message(messageError).errors(errors).service(serviceName).build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(
                        messageUtil.create("exception.missing.parameter.request",
                                ex.getMethodParameter().getParameter().getName(),
                                ex.getMethodParameter().getParameter().getType().getName(),
                                ex.getMethodParameter().getExecutable().toString()
                                )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(
                        messageUtil.create("exception.missing.parameter.path",
                                ex.getParameter().getParameter().getName(),
                                ex.getParameter().getParameter().getType().getName(),
                                ex.getParameter().getExecutable().toString()
                        )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                    HttpHeaders headers,
                                                                    HttpStatusCode status,
                                                                    WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.getMessage())
                .message(
                        messageUtil.create("exception.not.found.resource",
                                ex.getResourcePath(),
                                ex.getHttpMethod().name()
                        )
                )
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex,
                                                       HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        Optional<ByteBuffer> byteBuffer = ex.responseBody();
        if (byteBuffer.isPresent() && byteBuffer.get().hasArray()) {
            result = new String(byteBuffer.get().array());
        }
        ApiError apiError = null;
        if (!result.isEmpty()) {
            try {
                apiError = jacksonObjectMapper.readValue(result, ApiError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        String messageError = messageUtil.create("error.in.endpoint", request.getServletPath());
        ApiError build = ApiError.builder().message(messageError).service(serviceName).parentError(apiError).build();
        if (Objects.isNull(apiError)) {
            build.setDebugMessage(ex.getMessage());
        }
        return new ResponseEntity<Object>(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.toString())
                .message(ex.getMessage())
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ApiError build = ApiError.builder()
                .debugMessage(ex.toString())
                .message(messageUtil.create("error.access.denied"))
                .service(serviceName)
                .build();
        return new ResponseEntity(build, HttpStatus.FORBIDDEN);
    }

}
