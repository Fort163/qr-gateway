package com.quick.recording.gateway.config.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quick.recording.gateway.config.error.exeption.NotFoundException;
import com.quick.recording.gateway.config.error.exeption.QRInternalServerErrorException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.ByteBuffer;
import java.util.*;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class QRExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundEx(NotFoundException ex, WebRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        ApiError build = ApiError.builder().debugMessage(ex.toString()).message(ex.getMessage()).service(serviceName).build();
        return new ResponseEntity(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({QRInternalServerErrorException.class})
    public ResponseEntity<Object> handleInternalServerError(QRInternalServerErrorException ex, WebRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        ApiError build = ApiError.builder().debugMessage(ex.toString()).message(ex.getMessage()).service(serviceName).build();
        return new ResponseEntity(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("Validation error in field %s message - %s", fieldName, errorMessage));
        });
        String messageError = String.format("Validation error class %s in %s not valid parameter %s",
                ex.getParameter().getExecutable().getDeclaringClass().getName(),
                ex.getParameter().toString(),
                ex.getParameter().getParameter().toString()
        );
        ApiError build = ApiError.builder().message(messageError).debugMessage(ex.getMessage()).errors(errors).service(serviceName).build();
        return new ResponseEntity(build, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException ex,
                                         HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        Optional<ByteBuffer> byteBuffer = ex.responseBody();
        if(byteBuffer.isPresent() && byteBuffer.get().hasArray()){
            result = new String(byteBuffer.get().array());
        }
        ApiError apiError = null;
        if(!result.isEmpty()) {
            try {
                apiError = jacksonObjectMapper.readValue(result, ApiError.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        String messageError = String.format("Error in endpoint %s",
                request.getServletPath()
        );
        ApiError build = ApiError.builder().message(messageError).service(serviceName).parentError(apiError).build();
        if(Objects.isNull(apiError)){
            build.setDebugMessage(ex.getMessage());
        }
        return new ResponseEntity<Object>(build,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
